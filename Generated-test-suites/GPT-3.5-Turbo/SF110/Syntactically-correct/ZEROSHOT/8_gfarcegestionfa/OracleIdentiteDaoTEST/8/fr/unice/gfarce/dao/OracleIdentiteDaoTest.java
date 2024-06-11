import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import fr.unice.gfarce.dao.*;
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

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        Calendar dateLimite = Calendar.getInstance();
        identite.setEmail("john.doe@example.com");
        dao.insert(identite, "Form", dateLimite);

        // Test if the identite is inserted properly
        List<Identite> resultList = dao.find("John", "Doe", null, "john.doe@example.com", null);
        assertEquals(1, resultList.size());
        assertEquals(identite, resultList.get(0));
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(2);
        identite.setNom("Jane");
        identite.setPrenom("Doe");
        identite.setEmail("jane.doe@example.com");
        dao.insert(identite, "Form", Calendar.getInstance());

        // Update the identite
        identite.setNom("Mary");
        dao.update(identite);

        // Test if the identite is updated properly
        List<Identite> resultList = dao.find("Mary", "Doe", null, "jane.doe@example.com", null);
        assertEquals(1, resultList.size());
        assertEquals(identite, resultList.get(0));
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(3);
        identite.setNom("Tom");
        identite.setPrenom("Jerry");
        identite.setEmail("tom.jerry@example.com");
        dao.insert(identite, "Form", Calendar.getInstance());

        // Delete the identite
        dao.delete(identite);

        // Test if the identite is deleted properly
        List<Identite> resultList = dao.find("Tom", "Jerry", null, "tom.jerry@example.com", null);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(4);
        identite1.setNom("Alice");
        identite1.setPrenom("Smith");
        identite1.setEmail("alice.smith@example.com");
        dao.insert(identite1, "Form", Calendar.getInstance());

        Identite identite2 = new Identite();
        identite2.setId(5);
        identite2.setNom("Bob");
        identite2.setPrenom("Johnson");
        identite2.setEmail("bob.johnson@example.com");
        dao.insert(identite2, "Form", Calendar.getInstance());

        Identite identite3 = new Identite();
        identite3.setId(6);
        identite3.setNom("Alice");
        identite3.setPrenom("Johnson");
        identite3.setEmail("alice.johnson@example.com");
        dao.insert(identite3, "Form", Calendar.getInstance());

        // Test if the candidats are found properly
        Candidat[] candidats = dao.findCandidat("Alice", null, null, null);
        assertEquals(2, candidats.length);
        assertEquals(identite1, candidats[0]);
        assertEquals(identite3, candidats[1]);
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(7);
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("john.doe@example.com");
        dao.insert(identite1, "Form", Calendar.getInstance());

        Identite identite2 = new Identite();
        identite2.setId(8);
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setEmail("jane.doe@example.com");
        dao.insert(identite2, "Form", Calendar.getInstance());

        Identite identite3 = new Identite();
        identite3.setId(9);
        identite3.setNom("John");
        identite3.setPrenom("Smith");
        identite3.setEmail("john.smith@example.com");
        dao.insert(identite3, "Form", Calendar.getInstance());

        // Test if the resp formations are found properly
        RespFormation[] respFormations = dao.findRespForm("John", null, null, null);
        assertEquals(2, respFormations.length);
        assertEquals(identite1, respFormations[0]);
        assertEquals(identite3, respFormations[1]);
    }
}