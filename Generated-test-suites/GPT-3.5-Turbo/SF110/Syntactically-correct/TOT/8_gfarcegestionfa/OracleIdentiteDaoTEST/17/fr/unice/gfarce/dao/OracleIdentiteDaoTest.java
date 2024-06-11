import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OracleIdentiteDaoTest {
    
    @Test
    public void testInsert() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(Calendar.YEAR, 2022);
        dateLimite.set(Calendar.MONTH, 0);
        dateLimite.set(Calendar.DAY_OF_MONTH, 1);
        
        dao.insert(identite, "Form", dateLimite);
        
        // Check if the identite is inserted correctly
        List<Identite> result = dao.find("John", "Doe", "Male", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, result.size());
        Identite insertedIdentite = result.get(0);
        assertEquals("John", insertedIdentite.getNom());
        assertEquals("Doe", insertedIdentite.getPrenom());
        assertEquals("Male", insertedIdentite.getSex());
        assertEquals("john.doe@example.com", insertedIdentite.getEmail());
        assertEquals(Identite.TypeIdentite.CANDIDAT, insertedIdentite.getType());
        assertEquals(dateLimite, insertedIdentite.getDateLimite());
    }
    
    @Test
    public void testUpdate() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.update(identite); // Update identite with new values
        
        // Check if the identite is updated correctly
        List<Identite> result = dao.find("John", "Doe", "Male", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, result.size());
        Identite updatedIdentite = result.get(0);
        assertEquals("John", updatedIdentite.getNom());
        assertEquals("Doe", updatedIdentite.getPrenom());
        assertEquals("Male", updatedIdentite.getSex());
        assertEquals("john.doe@example.com", updatedIdentite.getEmail());
        assertEquals(Identite.TypeIdentite.CANDIDAT, updatedIdentite.getType());
    }
    
    @Test
    public void testDelete() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.delete(identite);
        
        // Check if the identite is deleted correctly
        List<Identite> result = dao.find("John", "Doe", "Male", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testFind() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        
        // Test typical use case
        List<Identite> result1 = dao.find("John", "Doe", "Male", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, result1.size());
        
        // Test edge case with null values
        List<Identite> result2 = dao.find(null, null, null, null, null);
        assertEquals(1, result2.size());
        
        // Test error scenario with non-existing values
        List<Identite> result3 = dao.find("Jane", "Smith", "Female", "jane.smith@example.com", Identite.TypeIdentite.FORMATEUR);
        assertEquals(0, result3.size());
    }
    
    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        
        // Test typical use case
        Candidat[] result1 = dao.findCandidat("John", "Doe", "Male", "john.doe@example.com");
        assertEquals(1, result1.length);
        
        // Test edge case with null values
        Candidat[] result2 = dao.findCandidat(null, null, null, null);
        assertEquals(1, result2.length);
        
        // Test error scenario with non-existing values
        Candidat[] result3 = dao.findCandidat("Jane", "Smith", "Female", "jane.smith@example.com");
        assertNull(result3);
    }
    
    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        
        // Test typical use case
        RespFormation[] result1 = dao.findRespForm("John", "Doe", "Male", "john.doe@example.com");
        assertEquals(1, result1.length);
        
        // Test edge case with null values
        RespFormation[] result2 = dao.findRespForm(null, null, null, null);
        assertEquals(1, result2.length);
        
        // Test error scenario with non-existing values
        RespFormation[] result3 = dao.findRespForm("Jane", "Smith", "Female", "jane.smith@example.com");
        assertNull(result3);
    }
}