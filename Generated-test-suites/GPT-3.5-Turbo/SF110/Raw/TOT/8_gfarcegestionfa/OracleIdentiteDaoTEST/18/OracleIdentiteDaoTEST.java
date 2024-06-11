import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    
    @Before
    public void setup() {
        dao = new OracleIdentiteDao();
    }
    
    @Test
    public void testInsert() {
        // Typical use case
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@gmail.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(2022, 1, 1);
        
        dao.insert(identite, "FormName", dateLimite);
        
        // Check if the insertion was successful
        Identite[] result = dao.findCandidat("John", "Doe", "Male", "john.doe@gmail.com");
        assertEquals(1, result.length);
        assertEquals("John", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
        assertEquals("john.doe@gmail.com", result[0].getEmail());
        assertEquals("Male", result[0].getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, result[0].getType());
        
        // Edge case - empty values
        Identite identite2 = new Identite();
        identite2.setNom("");
        identite2.setPrenom("");
        identite2.setEmail("");
        identite2.setSex("");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        Calendar dateLimite2 = Calendar.getInstance();
        dateLimite2.set(2023, 1, 1);
        
        dao.insert(identite2, "FormName2", dateLimite2);
        
        // Check if the insertion was successful
        Identite[] result2 = dao.findRespForm("", "", "", "");
        assertEquals(1, result2.length);
        assertEquals("", result2[0].getNom());
        assertEquals("", result2[0].getPrenom());
        assertEquals("", result2[0].getEmail());
        assertEquals("", result2[0].getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, result2[0].getType());
        
        // Error scenario - null values
        Identite identite3 = null;
        String nomForm3 = null;
        Calendar dateLimite3 = null;
        
        try {
            dao.insert(identite3, nomForm3, dateLimite3);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
    
    @Test
    public void testUpdate() {
        // Typical use case
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@gmail.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.update(identite);
        
        // Check if the update was successful
        Identite[] result = dao.findCandidat("John", "Doe", "Male", "john.doe@gmail.com");
        assertEquals(1, result.length);
        assertEquals("John", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
        assertEquals("john.doe@gmail.com", result[0].getEmail());
        assertEquals("Male", result[0].getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, result[0].getType());
        
        // Edge case - empty values
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("");
        identite2.setPrenom("");
        identite2.setEmail("");
        identite2.setSex("");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        dao.update(identite2);
        
        // Check if the update was successful
        Identite[] result2 = dao.findCandidat("", "", "", "");
        assertEquals(1, result2.length);
        assertEquals("", result2[0].getNom());
        assertEquals("", result2[0].getPrenom());
        assertEquals("", result2[0].getEmail());
        assertEquals("", result2[0].getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, result2[0].getType());
    }
    
    @Test
    public void testDelete() {
        // Typical use case
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@gmail.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.delete(identite);
        
        // Check if the deletion was successful
        Identite[] result = dao.findCandidat("John", "Doe", "Male", "john.doe@gmail.com");
        assertEquals(0, result.length);
        
        // Edge case - empty values
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("");
        identite2.setPrenom("");
        identite2.setEmail("");
        identite2.setSex("");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        dao.delete(identite2);
    }
    
    @Test
    public void testFind() {
        // Typical use case
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        List<Identite> result = dao.find(nom, prenom, sex, email, type);
        
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getNom());
        assertEquals("Doe", result.get(0).getPrenom());
        assertEquals("john.doe@gmail.com", result.get(0).getEmail());
        assertEquals("Male", result.get(0).getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, result.get(0).getType());
        
        // Edge case - empty values
        String nom2 = "";
        String prenom2 = "";
        String sex2 = "";
        String email2 = "";
        Identite.TypeIdentite type2 = Identite.TypeIdentite.FORMATEUR;
        
        List<Identite> result2 = dao.find(nom2, prenom2, sex2, email2, type2);
        
        assertEquals(1, result2.size());
        assertEquals("", result2.get(0).getNom());
        assertEquals("", result2.get(0).getPrenom());
        assertEquals("", result2.get(0).getEmail());
        assertEquals("", result2.get(0).getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, result2.get(0).getType());
        
        // Error scenario - null values
        String nom3 = null;
        String prenom3 = null;
        String sex3 = null;
        String email3 = null;
        Identite.TypeIdentite type3 = null;
        
        List<Identite> result3 = dao.find(nom3, prenom3, sex3, email3, type3);
        
        assertEquals(0, result3.size());
    }
    
    @Test
    public void testFindCandidat() {
        // Typical use case
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@gmail.com";
        
        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
        
        assertEquals(1, result.length);
        assertEquals("John", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
        assertEquals("john.doe@gmail.com", result[0].getEmail());
        assertEquals("Male", result[0].getSex());
        
        // Edge case - empty values
        String nom2 = "";
        String prenom2 = "";
        String sex2 = "";
        String email2 = "";
        
        Candidat[] result2 = dao.findCandidat(nom2, prenom2, sex2, email2);
        
        assertEquals(1, result2.length);
        assertEquals("", result2[0].getNom());
        assertEquals("", result2[0].getPrenom());
        assertEquals("", result2[0].getEmail());
        assertEquals("", result2[0].getSex());
        
        // Error scenario - null values
        String nom3 = null;
        String prenom3 = null;
        String sex3 = null;
        String email3 = null;
        
        Candidat[] result3 = dao.findCandidat(nom3, prenom3, sex3, email3);
        
        assertNull(result3);
    }
    
    @Test
    public void testFindRespForm() {
        // Typical use case
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@gmail.com";
        
        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
        
        assertEquals(1, result.length);
        assertEquals("John", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
        assertEquals("john.doe@gmail.com", result[0].getEmail());
        assertEquals("Male", result[0].getSex());
        
        // Edge case - empty values
        String nom2 = "";
        String prenom2 = "";
        String sex2 = "";
        String email2 = "";
        
        RespFormation[] result2 = dao.findRespForm(nom2, prenom2, sex2, email2);
        
        assertEquals(1, result2.length);
        assertEquals("", result2[0].getNom());
        assertEquals("", result2[0].getPrenom());
        assertEquals("", result2[0].getEmail());
        assertEquals("", result2[0].getSex());
        
        // Error scenario - null values
        String nom3 = null;
        String prenom3 = null;
        String sex3 = null;
        String email3 = null;
        
        RespFormation[] result3 = dao.findRespForm(nom3, prenom3, sex3, email3);
        
        assertNull(result3);
    }
}