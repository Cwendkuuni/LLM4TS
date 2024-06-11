import org.junit.*;
import static org.junit.Assert.*;
import fr.unice.gfarce.identity.*;
import java.util.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;
    
    @Before
    public void setUp() {
        dao = new OracleIdentiteDao();
    }
    
    @After
    public void tearDown() {
        dao = null;
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite, "Form", dateLimite);
        
        // Check if identite is inserted correctly
        List<Identite> resultList = dao.find("John", "Doe", null, null, null);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite retrievedIdentite = resultList.get(0);
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(123);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");
        
        dao.update(identite);
        
        // Check if identite is updated correctly
        List<Identite> resultList = dao.find("John", "Doe", null, null, null);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite retrievedIdentite = resultList.get(0);
        assertEquals(identite.getNom(), retrievedIdentite.getNom());
        assertEquals(identite.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite.getEmail(), retrievedIdentite.getEmail());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(123);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");
        
        dao.delete(identite);
        
        // Check if identite is deleted correctly
        List<Identite> resultList = dao.find("John", "Doe", null, null, null);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test
    public void testFind() {
        // Insert dummy data
        Identite identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("john.doe@example.com");
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite1, "Form", dateLimite);
        
        Identite identite2 = new Identite();
        identite2.setNom("Alice");
        identite2.setPrenom("Smith");
        identite2.setEmail("alice.smith@example.com");
        dao.insert(identite2, "Form", dateLimite);
        
        // Check if find method returns correct result
        List<Identite> resultList = dao.find("John", "Doe", null, null, null);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite retrievedIdentite = resultList.get(0);
        assertEquals(identite1.getNom(), retrievedIdentite.getNom());
        assertEquals(identite1.getPrenom(), retrievedIdentite.getPrenom());
        assertEquals(identite1.getEmail(), retrievedIdentite.getEmail());
        
        resultList = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        
        resultList = dao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        
        resultList = dao.find(null, null, "Male", null, null);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
    }
    
    @Test
    public void testFindCandidat() {
        // Insert dummy data
        Identite identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("john.doe@example.com");
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite1, "Form", dateLimite);
        
        // Check if findCandidat method returns correct result
        Candidat[] resultList = dao.findCandidat("John", "Doe", null, null);
        assertNotNull(resultList);
        assertEquals(1, resultList.length);
        Candidat retrievedCandidat = resultList[0];
        assertEquals(identite1.getNom(), retrievedCandidat.getNom());
        assertEquals(identite1.getPrenom(), retrievedCandidat.getPrenom());
        assertEquals(identite1.getEmail(), retrievedCandidat.getEmail());
    }
    
    @Test
    public void testFindRespForm() {
        // Insert dummy data
        Identite identite1 = new Identite();
        identite1.setNom("Alice");
        identite1.setPrenom("Smith");
        identite1.setEmail("alice.smith@example.com");
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite1, "Form", dateLimite);
        
        // Check if findRespForm method returns correct result
        RespFormation[] resultList = dao.findRespForm("Alice", "Smith", null, null);
        assertNotNull(resultList);
        assertEquals(1, resultList.length);
        RespFormation retrievedRespForm = resultList[0];
        assertEquals(identite1.getNom(), retrievedRespForm.getNom());
        assertEquals(identite1.getPrenom(), retrievedRespForm.getPrenom());
        assertEquals(identite1.getEmail(), retrievedRespForm.getEmail());
    }
}