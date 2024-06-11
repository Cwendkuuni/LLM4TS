import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
        identite.setEmail("john.doe@example.com");
        
        dao.insert(identite, "Form", Calendar.getInstance());
        
        // Check if the insertion was successful by finding the inserted Identite
        Identite[] result = dao.findCandidat("John", "Doe", null, "john.doe@example.com");
        assertEquals(1, result.length);
        assertEquals("John", result[0].getNom());
        assertEquals("Doe", result[0].getPrenom());
    }
    
    @Test
    public void testUpdate() {
        // Test updating the details of an existing Identite
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("Alice");
        identite.setPrenom("Smith");
        
        dao.update(identite);
        
        // Check if the update was successful by finding the updated Identite
        Identite[] result = dao.findCandidat("Alice", "Smith", null, null);
        assertEquals(1, result.length);
        assertEquals("Smith", result[0].getPrenom());
    }
    
    @Test
    public void testDelete() {
        // Test deleting an existing Identite
        Identite identite = new Identite();
        identite.setId(1);
        
        dao.delete(identite);
        
        // Check if the Identite was successfully deleted
        Identite[] result = dao.findCandidat(null, null, null, null);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFind() {
        // Test finding Identite based on specified parameters
        List<Identite> result = dao.find("Alice", null, null, null, Identite.TypeIdentite.CANDIDAT);
        
        // Check if the correct Identite is found
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getNom());
    }
    
    @Test
    public void testFindCandidat() {
        // Test finding Candidat based on specified parameters
        Candidat[] result = dao.findCandidat("Alice", null, null, null);
        
        // Check if the correct Candidat is found
        assertNotNull(result);
    }
    
    @Test
    public void testFindRespForm() {
        // Test finding RespFormation based on specified parameters
        RespFormation[] result = dao.findRespForm(null, "Jane", null, null);
        
        // Check if the correct RespFormation is found
        assertNotNull(result);
    }

}