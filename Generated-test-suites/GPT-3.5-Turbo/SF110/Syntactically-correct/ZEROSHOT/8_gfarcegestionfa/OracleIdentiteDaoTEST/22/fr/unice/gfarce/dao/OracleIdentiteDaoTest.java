import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class OracleIdentiteDaoTest {
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite, "Form1", dateLimite);
        
        List<Identite> resultList = dao.find("John", "Doe", "Male", "johndoe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertTrue(resultList.contains(identite));
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.update(identite);
        
        Identite updatedIdentite = dao.find("John", "Doe", "Male", "johndoe@example.com", Identite.TypeIdentite.CANDIDAT).get(0);
        assertEquals("NewName", updatedIdentite.getNom());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.delete(identite);
        
        List<Identite> resultList = dao.find("John", "Doe", "Male", "johndoe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertFalse(resultList.contains(identite));
    }
    
    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("johndoe@example.com");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setEmail("janedoe@example.com");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite1, "Form1", Calendar.getInstance());
        dao.insert(identite2, "Form2", Calendar.getInstance());
        
        List<Identite> resultList = dao.find("Doe", "Doe", "Male", null, Identite.TypeIdentite.CANDIDAT);
        assertTrue(resultList.contains(identite1));
        assertFalse(resultList.contains(identite2));
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("johndoe@example.com");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setEmail("janedoe@example.com");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite1, "Form1", Calendar.getInstance());
        dao.insert(identite2, "Form2", Calendar.getInstance());
        
        Candidat[] candidats = dao.findCandidat("Doe", "Doe", "Male", null);
        assertTrue(Arrays.asList(candidats).contains(identite1));
        assertFalse(Arrays.asList(candidats).contains(identite2));
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("johndoe@example.com");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        
        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setEmail("janedoe@example.com");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite1, "Form1", Calendar.getInstance());
        dao.insert(identite2, "Form2", Calendar.getInstance());
        
        RespFormation[] respForms = dao.findRespForm("Doe", "Doe", "Male", null);
        assertFalse(Arrays.asList(respForms).contains(identite1));
        assertTrue(Arrays.asList(respForms).contains(identite2));
    }
}