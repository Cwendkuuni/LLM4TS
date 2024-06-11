import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;

    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown() {
        dao = null;
    }

    // Test insert method
    @Test
    public void testInsert() {
        // Creating test data
        Identite identite = new Identite();
        identite.setNom("TestNom");
        identite.setEmail("test@gmail.com");
        Calendar dateLimite = Calendar.getInstance();

        // Perform the insert method
        dao.insert(identite, "TestForm", dateLimite);

        // Assert the result
        List<Identite> result = dao.find("TestNom", null, null, "test@gmail.com", null);
        assertTrue(result.contains(identite));
    }

    // Test update method
    @Test
    public void testUpdate() {
        // Creating test data
        Identite identite = new Identite();
        identite.setNom("TestNom");
        identite.setEmail("test@gmail.com");

        // Insert the test data
        dao.insert(identite, "TestForm", null);

        // Update the test data
        identite.setNom("NewTestNom");
        dao.update(identite);

        // Assert the result
        List<Identite> result = dao.find("NewTestNom", null, null, "test@gmail.com", null);
        assertTrue(result.contains(identite));
    }

    // Test delete method
    @Test
    public void testDelete() {
        // Creating test data
        Identite identite = new Identite();
        identite.setNom("TestNom");
        identite.setEmail("test@gmail.com");

        // Insert the test data
        dao.insert(identite, "TestForm", null);

        // Delete the test data
        dao.delete(identite);

        // Assert the result
        List<Identite> result = dao.find("TestNom", null, null, "test@gmail.com", null);
        assertFalse(result.contains(identite));
    }

    // Test find method
    @Test
    public void testFind() {
        // Creating test data
        Identite identite1 = new Identite();
        identite1.setNom("TestNom1");
        identite1.setEmail("test1@gmail.com");
        Identite identite2 = new Identite();
        identite2.setNom("TestNom2");
        identite2.setEmail("test2@gmail.com");

        // Insert the test data
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite1, "TestForm", dateLimite);
        dao.insert(identite2, "TestForm", dateLimite);

        // Perform the find method
        List<Identite> result = dao.find("TestNom", null, null, "test@gmail.com", null);

        // Assert the result
        assertTrue(result.contains(identite1));
        assertTrue(result.contains(identite2));
    }

    // Test findCandidat method
    @Test
    public void testFindCandidat() {
        // Creating test data
        Identite identite1 = new Identite();
        identite1.setNom("TestNom1");
        identite1.setEmail("test1@gmail.com");
        Identite identite2 = new Identite();
        identite2.setNom("TestNom2");
        identite2.setEmail("test2@gmail.com");

        // Insert the test data
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite1, "TestForm", dateLimite);
        dao.insert(identite2, "TestForm", dateLimite);

        // Perform the findCandidat method
        Candidat[] result = dao.findCandidat("TestNom", null, null, "test@gmail.com");

        // Assert the result
        assertTrue(result.length == 2);
    }

    // Test findRespForm method
    @Test
    public void testFindRespForm() {
        // Creating test data
        Identite identite1 = new Identite();
        identite1.setNom("TestNom1");
        identite1.setEmail("test1@gmail.com");
        Identite identite2 = new Identite();
        identite2.setNom("TestNom2");
        identite2.setEmail("test2@gmail.com");

        // Insert the test data
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite1, "TestForm", dateLimite);
        dao.insert(identite2, "TestForm", dateLimite);

        // Perform the findRespForm method
        RespFormation[] result = dao.findRespForm("TestNom", null, null, "test@gmail.com");

        // Assert the result
        assertTrue(result.length == 2);
    }
}