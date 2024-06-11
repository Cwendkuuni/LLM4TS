import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import javax.persistence.PersistenceException;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.dao.OracleIdentiteDao;

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
        Identite identite = new Identite("John Doe", "john.doe@example.com");
        dao.insert(identite, "Form", Calendar.getInstance());
        Identite insertedIdentite = dao.find("John Doe", null, null, "john.doe@example.com", Identite.TypeIdentite.CANDIDAT).get(0);
        assertNotNull("Insertion failed", insertedIdentite);
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite("Jane Smith", "jane.smith@example.com");
        dao.insert(identite, "Form", Calendar.getInstance());
        Identite toUpdate = dao.find("Jane Smith", null, null, "jane.smith@example.com", Identite.TypeIdentite.CANDIDAT).get(0);
        toUpdate.setEmail("jane.updated@example.com");
        dao.update(toUpdate);
        Identite updatedIdentite = dao.find("Jane Smith", null, null, "jane.updated@example.com", Identite.TypeIdentite.CANDIDAT).get(0);
        assertEquals("Update failed", "jane.updated@example.com", updatedIdentite.getEmail());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite("Jake Johnson", "jake.johnson@example.com");
        dao.insert(identite, "Form", Calendar.getInstance());
        Identite toDelete = dao.find("Jake Johnson", null, null, "jake.johnson@example.com", Identite.TypeIdentite.CANDIDAT).get(0);
        dao.delete(toDelete);
        assertTrue("Delete failed", dao.find("Jake Johnson", null, null, "jake.johnson@example.com", Identite.TypeIdentite.CANDIDAT).isEmpty());
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite("Alice", "alice@example.com");
        Identite identite2 = new Identite("Bob", "bob@example.com");
        Identite identite3 = new Identite("Charlie", "charlie@example.com");
        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());
        dao.insert(identite3, "Form", Calendar.getInstance());
        assertEquals("Find by name failed", 2, dao.find("Bob", null, null, null, Identite.TypeIdentite.CANDIDAT).size());
    }

}