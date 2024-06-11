import org.junit.*;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {
    
    @Test
    public void testInsert() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@gmail.com");
        Calendar dateLimite = Calendar.getInstance();
        
        dao.insert(identite, "Form", dateLimite);
        // TODO: Add assertions
    }
    
    @Test
    public void testUpdate() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@gmail.com");
        
        dao.update(identite);
        // TODO: Add assertions
    }
    
    @Test
    public void testDelete() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@gmail.com");
        
        dao.delete(identite);
        // TODO: Add assertions
    }
    
    @Test
    public void testFind() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
        // TODO: Add assertions
    }
    
    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@gmail.com";
        
        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
        // TODO: Add assertions
    }
    
    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@gmail.com";
        
        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
        // TODO: Add assertions
    }
}