import static org.junit.Assert.*;
import org.junit.Test;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;

    @Before
    public void setup() {
        dao = new OracleIdentiteDao();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "TestForm", Calendar.getInstance());

        assertNotNull(identite.getId());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setEmail("updatedemail@example.com");

        dao.update(identite);

        assertEquals("updatedemail@example.com", identite.getEmail());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1L);

        dao.delete(identite);

        assertNull(dao.find(1L));
    }

    @Test
    public void testFindCandidat() {
        Candidat[] candidats = dao.findCandidat("Test", "Candidate", "F", "testcandidate@example.com");

        assertNotNull(candidats);
    }

    @Test
    public void testFindRespForm() {
        RespFormation[] respFormations = dao.findRespForm("Test", "Formateur", "M", "testformateur@example.com");

        assertNotNull(respFormations);
    }
    
    @Test
    public void testFind() {
        List<Identite> identites = dao.find("John", "Doe", "M", "johndoe@example.com", Identite.TypeIdentite.CANDIDAT);

        assertEquals(1, identites.size());
    }
}