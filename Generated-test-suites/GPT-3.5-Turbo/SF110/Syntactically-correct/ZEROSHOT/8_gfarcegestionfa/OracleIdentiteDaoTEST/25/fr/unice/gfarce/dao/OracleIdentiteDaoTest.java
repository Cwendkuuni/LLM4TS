import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {

    OracleIdentiteDao dao;

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
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite, "exampleForm", dateLimite);

        // Verify insertion
        List<Identite> resultList = dao.find("John", "Doe", null, null, null);
        assertTrue(resultList.contains(identite));
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setNom("Jane");
        identite.setPrenom("Doe");
        identite.setEmail("jane@example.com");
        dao.insert(identite, "exampleForm", null);

        // Update identite
        identite.setNom("Janet");
        dao.update(identite);

        // Verify update
        List<Identite> resultList = dao.find("Janet", "Doe", null, "jane@example.com", null);
        assertTrue(resultList.contains(identite));
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setNom("Alice");
        identite.setPrenom("Smith");
        dao.insert(identite, "exampleForm", null);

        // Delete identite
        dao.delete(identite);

        // Verify deletion
        List<Identite> resultList = dao.find("Alice", "Smith", null, null, null);
        assertFalse(resultList.contains(identite));
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite1, "exampleForm", null);

        Identite identite2 = new Identite();
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite2, "exampleForm", null);

        // Find Candidats
        Candidat[] result = dao.findCandidat(null, "Doe", null, null);

        // Verify results
        assertNotNull(result);
        assertEquals(2, result.length);
        assertTrue(result[0].isCandidat());
        assertTrue(result[1].isCandidat());
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setNom("Bob");
        identite1.setPrenom("Smith");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite1, "exampleForm", null);

        Identite identite2 = new Identite();
        identite2.setNom("Alice");
        identite2.setPrenom("Smith");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite2, "exampleForm", null);

        // Find RespForm
        RespFormation[] result = dao.findRespForm(null, "Smith", null, null);

        // Verify results
        assertNotNull(result);
        assertEquals(2, result.length);
        assertTrue(result[0].isRespFormation());
        assertTrue(result[1].isRespFormation());
    }
}