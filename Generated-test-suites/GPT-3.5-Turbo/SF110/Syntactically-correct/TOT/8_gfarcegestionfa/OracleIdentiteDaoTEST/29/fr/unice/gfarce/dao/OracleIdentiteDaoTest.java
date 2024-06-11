import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    
    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
    }
    
    @Test
    public void testInsert() {
        Identite testIdentite = new Identite();
        testIdentite.setId(1);
        testIdentite.setNom("John");
        testIdentite.setPrenom("Doe");
        testIdentite.setEmail("john.doe@example.com");
        testIdentite.setSex("M");
        testIdentite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar testDateLimite = Calendar.getInstance();
        testDateLimite.set(2022, Calendar.JANUARY, 1);
        
        // Typical use case
        dao.insert(testIdentite, "Form1", testDateLimite);
        
        // Verify the insertion
        List<Identite> resultList = dao.findCandidat("Doe", "John", "M", "john.doe@example.com");
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite result = resultList.get(0);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("M", result.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, result.getType());
        
        // Edge case: empty string
        testIdentite.setNom("");
        testIdentite.setPrenom("");
        testIdentite.setEmail("");
        dao.insert(testIdentite, "Form2", testDateLimite);
        
        // Verify the insertion
        resultList = dao.findCandidat("", "", "", "");
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        result = resultList.get(0);
        assertEquals("", result.getNom());
        assertEquals("", result.getPrenom());
        assertEquals("", result.getEmail());
        assertNull(result.getSex());
        assertNull(result.getType());
        
        // Error scenario: null values
        try {
            dao.insert(null, "Form3", testDateLimite);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // exception expected
        }
    }
    
    @Test
    public void testUpdate() {
        // Prepare test data
        Identite testIdentite = new Identite();
        testIdentite.setId(2);
        testIdentite.setNom("Jane");
        testIdentite.setPrenom("Doe");
        testIdentite.setEmail("jane.doe@example.com");
        testIdentite.setSex("F");
        testIdentite.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(testIdentite, "Form4", null);
        
        // Typical use case
        testIdentite.setEmail("new_email@example.com");
        testIdentite.setNom("Doe");
        testIdentite.setPrenom("Jane");
        dao.update(testIdentite);
        
        // Verify the update
        List<Identite> resultList = dao.find("Doe", "Jane", "F", "new_email@example.com", Identite.TypeIdentite.FORMATEUR);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite result = resultList.get(0);
        assertEquals("Doe", result.getNom());
        assertEquals("Jane", result.getPrenom());
        assertEquals("new_email@example.com", result.getEmail());
        assertEquals("F", result.getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, result.getType());
        
        // Error scenario: null value
        try {
            dao.update(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // exception expected
        }
    }
    
    @Test
    public void testDelete() {
        // Prepare test data
        Identite testIdentite = new Identite();
        testIdentite.setId(3);
        testIdentite.setNom("Mark");
        testIdentite.setPrenom("Smith");
        testIdentite.setEmail("mark.smith@example.com");
        testIdentite.setSex("M");
        testIdentite.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(testIdentite, "Form5", null);
        
        // Typical use case
        dao.delete(testIdentite);
        
        // Verify the deletion
        List<Identite> resultList = dao.find("Mark", "Smith", "M", "mark.smith@example.com", Identite.TypeIdentite.FORMATEUR);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
        
        // Error scenario: null value
        try {
            dao.delete(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // exception expected
        }
    }
    
    @Test
    public void testFind() {
        // Prepare test data
        Identite testIdentite1 = new Identite();
        testIdentite1.setId(4);
        testIdentite1.setNom("John");
        testIdentite1.setPrenom("Doe");
        testIdentite1.setEmail("john.doe@example.com");
        testIdentite1.setSex("M");
        testIdentite1.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(testIdentite1, "", null);
        
        Identite testIdentite2 = new Identite();
        testIdentite2.setId(5);
        testIdentite2.setNom("Jane");
        testIdentite2.setPrenom("Doe");
        testIdentite2.setEmail("jane.doe@example.com");
        testIdentite2.setSex("F");
        testIdentite2.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(testIdentite2, "Form6", null);
        
        // Typical use case
        List<Identite> resultList = dao.find("Doe", "John", "M", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite result = resultList.get(0);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("M", result.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, result.getType());
        
        // Edge case: empty strings
        resultList = dao.find("", "", "", "", null);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        
        // Error scenario: invalid type
        resultList = dao.find("", "", "", "", Identite.TypeIdentite.UNKNOWN);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test
    public void testFindCandidat() {
        // Prepare test data
        Identite testIdentite1 = new Identite();
        testIdentite1.setId(6);
        testIdentite1.setNom("John");
        testIdentite1.setPrenom("Doe");
        testIdentite1.setEmail("john.doe@example.com");
        testIdentite1.setSex("M");
        testIdentite1.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(testIdentite1, "", null);
        
        Identite testIdentite2 = new Identite();
        testIdentite2.setId(7);
        testIdentite2.setNom("Jane");
        testIdentite2.setPrenom("Doe");
        testIdentite2.setEmail("jane.doe@example.com");
        testIdentite2.setSex("F");
        testIdentite2.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(testIdentite2, "Form7", null);
        
        // Typical use case
        Candidat[] result = dao.findCandidat("Doe", "", "M", "");
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(6, result[0].getId());
        assertEquals("John", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
        assertEquals("john.doe@example.com", result[0].getEmail());
        assertEquals("M", result[0].getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, result[0].getType());
        
        // Edge case: empty strings
        result = dao.findCandidat("", "", "", "");
        assertNotNull(result);
        assertEquals(2, result.length);
        
        // Error scenario: null values
        try {
            result = dao.findCandidat(null, null, null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // exception expected
        }
    }
    
    @Test
    public void testFindRespForm() {
        // Prepare test data
        Identite testIdentite1 = new Identite();
        testIdentite1.setId(8);
        testIdentite1.setNom("John");
        testIdentite1.setPrenom("Doe");
        testIdentite1.setEmail("john.doe@example.com");
        testIdentite1.setSex("M");
        testIdentite1.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(testIdentite1, "", null);
        
        Identite testIdentite2 = new Identite();
        testIdentite2.setId(9);
        testIdentite2.setNom("Jane");
        testIdentite2.setPrenom("Doe");
        testIdentite2.setEmail("jane.doe@example.com");
        testIdentite2.setSex("F");
        testIdentite2.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(testIdentite2, "Form8", null);
        
        // Typical use case
        RespFormation[] result = dao.findRespForm("Doe", "", "F", "");
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(9, result[0].getId());
        assertEquals("Jane", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
        assertEquals("jane.doe@example.com", result[0].getEmail());
        assertEquals("F", result[0].getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, result[0].getType());
        
        // Edge case: empty strings
        result = dao.findRespForm("", "", "", "");
        assertNotNull(result);
        assertEquals(2, result.length);
        
        // Error scenario: null values
        try {
            result = dao.findRespForm(null, null, null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // exception expected
        }
    }
    
}