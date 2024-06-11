import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.List;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    
    @Before
    public void setup() {
        dao = OracleIdentiteDao.getDAO();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite, "Test Form", dateLimite);
        
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
        assertTrue(result.contains(identite));
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        identite.setNom("Jane");
        dao.update(identite);
        
        Identite updatedIdentite = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
        assertEquals(identite.getNom(), updatedIdentite.getNom());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        dao.delete(identite);
        
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
        assertFalse(result.contains(identite));
    }
    
    @Test
    public void testFind() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
        assertTrue(result.contains(identite));
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        Candidat[] result = dao.findCandidat(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail());
        assertNotNull(result);
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        RespFormation[] result = dao.findRespForm(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail());
        assertNotNull(result);
    }
    
    @Test
    public void testInsertWithException() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = null;
        dao.insert(identite, "Test Form", dateLimite);
        
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
        assertFalse(result.contains(identite));
    }
    
    @Test
    public void testUpdateWithException() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        Identite updatedIdentite = new Identite();
        updatedIdentite.setId(2);
        updatedIdentite.setNom("Jane");
        updatedIdentite.setPrenom("Doe");
        updatedIdentite.setEmail("janedoe@example.com");
        updatedIdentite.setSex("Female");
        updatedIdentite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.update(updatedIdentite);
        
        Identite returnedIdentite = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT).get(0);
        assertNotEquals(updatedIdentite.getNom(), returnedIdentite.getNom());
    }
    
    @Test
    public void testDeleteWithException() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@example.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite, "Test Form", Calendar.getInstance());
        
        Identite deletedIdentite = new Identite();
        deletedIdentite.setId(2);
        deletedIdentite.setNom("Jane");
        deletedIdentite.setPrenom("Doe");
        deletedIdentite.setEmail("janedoe@example.com");
        deletedIdentite.setSex("Female");
        deletedIdentite.setType(Identite.TypeIdentite.CANDIDAT);
        dao.delete(deletedIdentite);
        
        List<Identite> result = dao.find(identite.getNom(), identite.getPrenom(), identite.getSex(), identite.getEmail(), Identite.TypeIdentite.CANDIDAT);
        assertTrue(result.contains(identite));
    }

}