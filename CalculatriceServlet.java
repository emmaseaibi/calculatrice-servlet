import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CalculatriceServlet")
public class CalculatriceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            // Récupération des paramètres du formulaire
            double x = Double.parseDouble(request.getParameter("num1"));
            double y = Double.parseDouble(request.getParameter("num2"));
            String operation = request.getParameter("operation");
            
            double resultat = 0;
            String text = "";
            
            // Réaliser le calcul selon l'opération choisie
            switch (operation) {
                case "addition":
                    resultat = x + y;
                    text = "Addition";
                    break;
                case "soustraction":
                    resultat = x - y;
                    text = "Soustraction";
                    break;
                case "multiplication":
                    resultat = x * y;
                    text = "Multiplication";
                    break;
                case "division":
                    if (y != 0) {
                        resultat = x / y;
                    } else {
                        text = "Division (Erreur: division par zéro)";
                    }
                    break;
                default:
                    text = "Opération non reconnue";
            }
            
            // Gestion de la session pour stocker la date du dernier accès
            HttpSession session = request.getSession();
            String dernierAcces = (String) session.getAttribute("dernierAcces");
            
            // Mettre à jour la date du dernier accès
            LocalDateTime maintenant = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dateActuelle = maintenant.format(formatter);
            session.setAttribute("dernierAcces", dateActuelle);
            
            // Configuration de la réponse HTML
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Résultat de la Calculatrice</title>");            
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Résultat de la Calculatrice</h1>");
                // Afficher le calcul et son résultat
                if(operation.equals("division") && y == 0) {
                    out.println("<p>" + text + "</p>");
                } else {
                    out.println("<p>Opération: " + text + "</p>");
                    out.println("<p>" + x + " " + getSymbol(operation) + " " + y + " = " + resultat + "</p>");
                }
                // Afficher la date du dernier accès
                if(dernierAcces != null) {
                    out.println("<p>Dernier accès (avant mise à jour) : " + dernierAcces + "</p>");
                }
                out.println("<p>Accès actuel : " + dateActuelle + "</p>");
                out.println("<br><a href='index.html'>Retour</a>");
                out.println("</body>");
                out.println("</html>");
                } finally {
                } out.close();
            }catch (Exception e) {
                response.getWriter().println("<html><body><h1>Erreur : Entrée invalide</h1><p>Veuillez entrer des nombres valides.</p><a href='index.html'>Retour</a></body></html>");
            }
    }
    
    // Méthode pour obtenir le symbole de l'opération
    private String getSymbol(String operation) {
        return switch (operation) {
            case "addition" -> "+";
            case "soustraction" -> "-";
            case "multiplication" -> "*";
            case "division" -> "/";
            default -> "";
        };
    }
    
    // Vous pouvez rediriger GET vers POST ou l'implémenter de manière similaire
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
