import org.junit.*;
import static org.junit.Assert.*;
import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;
import fr.unice.gfarce.dao.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;

    @Before
    public void setUp() {
        dao = new OracleIdentiteDao();
    }

    @Test
    public void testInsert() {
        // Test inserting a new Identite
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        
        dao.insert(identite, "Form", Calendar.getInstance());
        
        // Check if the identite was inserted successfully
        assertNotNull(identite.getId());
    }

    @Test
    public void testUpdate() {
        // Test updating an existing Identite
        Identite identite = new Identite();
        identite.setNom("Alice");
        identite.setPrenom("Smith");
        
        dao.insert(identite, "Form", Calendar.getInstance());
        
        // Update the Identite details
        identite.setEmail("alice@example.com");
        
        dao.update(identite);
        
        // Check if the email was updated successfully
        assertEquals("alice@example.com", identite.getEmail());
    }

    @Test
    public void testDelete() {
        // Test deleting an existing Identite
        Identite identite = new Identite();
        identite.setNom("Bob");
        identite.setPrenom("Johnson");
        
        dao.insert(identite, "Form", Calendar.getInstance());
        
        dao.delete(identite);
        
        // Check if the identite was deleted successfully
        assertNull(identite.getId());
    }

    @Test
    public void testFind() {
        // Test finding Identite based on criteria
        List<Identite> resultList = dao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        
        // Check if the result list is not null
        assertNotNull(resultList);
    }

    @Test
    public void testFindCandidat() {
        // Test finding Candidat based on criteria
        Candidat[] result = dao.findCandidat("Emma", "Watson", "Female", "emma@example.com");
        
        // Check if the result array is not null
        assertNotNull(result);
    }

    @Test
    public void testFindRespForm() {
        // Test finding RespFormation based on criteria
        RespFormation[] result = dao.findRespForm("Tom", "Hanks", "Male", "tom@example.com");
        
        // Check if the result array is not null
        assertNotNull(result);
    }

}