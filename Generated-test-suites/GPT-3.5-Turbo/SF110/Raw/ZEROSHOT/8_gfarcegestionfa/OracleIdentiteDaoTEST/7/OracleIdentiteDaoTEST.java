import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao oracleIdentiteDao;
    private EntityManagerFactory emf;
    private EntityManager em;
    
    @Before
    public void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
        emf = Persistence.createEntityManagerFactory("Identite");
        em = emf.createEntityManager();
        oracleIdentiteDao.em = em;
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("Tester");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(2022, Calendar.JANUARY, 1);
        
        oracleIdentiteDao.insert(identite, "Test", dateLimite);
        
        Identite insertedIdentite = em.find(Identite.class, identite.getId());
        
        assertNotNull("Inserted Identite should not be null", insertedIdentite);
        assertEquals("Inserted Identite should have the correct email", identite.getEmail(), insertedIdentite.getEmail());
        assertEquals("Inserted Identite should have the correct nom", identite.getNom(), insertedIdentite.getNom());
        assertEquals("Inserted Identite should have the correct prenom", identite.getPrenom(), insertedIdentite.getPrenom());
        assertEquals("Inserted Identite should have the correct sex", identite.getSex(), insertedIdentite.getSex());
        assertEquals("Inserted Identite should have the correct type", identite.getType(), insertedIdentite.getType());
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("Tester");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        em.getTransaction().begin();
        em.persist(identite);
        em.getTransaction().commit();
        
        identite.setEmail("newemail@test.com");
        identite.setNom("NewTest");
        identite.setPrenom("NewTester");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        
        oracleIdentiteDao.update(identite);
        
        Identite updatedIdentite = em.find(Identite.class, identite.getId());
        
        assertNotNull("Updated Identite should not be null", updatedIdentite);
        assertEquals("Updated Identite should have the correct email", identite.getEmail(), updatedIdentite.getEmail());
        assertEquals("Updated Identite should have the correct nom", identite.getNom(), updatedIdentite.getNom());
        assertEquals("Updated Identite should have the correct prenom", identite.getPrenom(), updatedIdentite.getPrenom());
        assertEquals("Updated Identite should have the correct sex", identite.getSex(), updatedIdentite.getSex());
        assertEquals("Updated Identite should have the correct type", identite.getType(), updatedIdentite.getType());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("Tester");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        em.getTransaction().begin();
        em.persist(identite);
        em.getTransaction().commit();
        
        oracleIdentiteDao.delete(identite);
        
        Identite deletedIdentite = em.find(Identite.class, identite.getId());
        
        assertNull("Deleted Identite should be null", deletedIdentite);
    }
    
    @Test
    public void testFind() {
        String nom = "Test";
        String prenom = "Tester";
        String sex = "M";
        String email = "test@test.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 1", 1, result.size());
        Identite identite = result.get(0);
        assertEquals("Result Identite should have the correct email", email, identite.getEmail());
        assertEquals("Result Identite should have the correct nom", nom, identite.getNom());
        assertEquals("Result Identite should have the correct prenom", prenom, identite.getPrenom());
        assertEquals("Result Identite should have the correct sex", sex, identite.getSex());
        assertEquals("Result Identite should have the correct type", type, identite.getType());
    }
    
    @Test
    public void testFindCandidat() {
        String nom = "Test";
        String prenom = "Tester";
        String sex = "M";
        String email = "test@test.com";
        
        Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Result length should be 1", 1, result.length);
        assertEquals("Result Candidat should have the correct email", email, result[0].getEmail());
        assertEquals("Result Candidat should have the correct nom", nom, result[0].getNom());
        assertEquals("Result Candidat should have the correct prenom", prenom, result[0].getPrenom());
        assertEquals("Result Candidat should have the correct sex", sex, result[0].getSex());
    }
    
    @Test
    public void testFindRespForm() {
        String nom = "Test";
        String prenom = "Tester";
        String sex = "M";
        String email = "test@test.com";
        
        RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Result length should be 1", 1, result.length);
        assertEquals("Result RespFormation should have the correct email", email, result[0].getEmail());
        assertEquals("Result RespFormation should have the correct nom", nom, result[0].getNom());
        assertEquals("Result RespFormation should have the correct prenom", prenom, result[0].getPrenom());
        assertEquals("Result RespFormation should have the correct sex", sex, result[0].getSex());
    }
    
    @After
    public void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}