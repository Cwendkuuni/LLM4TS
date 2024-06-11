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
        identite.setNom("John");
        identite.setPrenom("Doe");
        Calendar dateLimite = Calendar.getInstance();
        
        dao.insert(identite, "Form", dateLimite);
        
        // Verify that the identite is inserted in the database
        List<Identite> result = dao.find("John", "Doe", null, null, null);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testUpdate() {
        // Insert an identite into the database
        Identite identite = new Identite();
        identite.setNom("Jane");
        identite.setPrenom("Doe");
        dao.insert(identite, "Form", Calendar.getInstance());
        
        // Update the identite
        identite.setNom("Jane Updated");
        identite.setPrenom("Doe Updated");
        dao.update(identite);
        
        // Verify that the identite is updated in the database
        List<Identite> result = dao.find("Jane Updated", "Doe Updated", null, null, null);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testDelete() {
        // Insert an identite into the database
        Identite identite = new Identite();
        identite.setNom("Mary");
        identite.setPrenom("Doe");
        dao.insert(identite, "Form", Calendar.getInstance());
        
        // Delete the identite from the database
        dao.delete(identite);
        
        // Verify that the identite is deleted from the database
        List<Identite> result = dao.find("Mary", "Doe", null, null, null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testFind() {
        // Insert identites into the database
        Identite identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        dao.insert(identite1, "Form", Calendar.getInstance());
        
        Identite identite2 = new Identite();
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        dao.insert(identite2, "Form", Calendar.getInstance());
        
        // Find identites from the database
        List<Identite> result = dao.find(null, "Doe", null, null, null);
        
        // Verify that the correct number of identites are found
        assertEquals(2, result.size());
    }
    
    @Test
    public void testFindCandidat() {
        // Insert identites into the database
        Identite identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        dao.insert(identite1, "Form", Calendar.getInstance());
        
        Identite identite2 = new Identite();
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        dao.insert(identite2, "Form", Calendar.getInstance());
        
        // Find candidats from the database
        Candidat[] result = dao.findCandidat(null, "Doe", null, null);
        
        // Verify that the correct number of candidats are found
        assertEquals(1, result.length);
    }
    
    @Test
    public void testFindRespForm() {
        // Insert identites into the database
        Identite identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        dao.insert(identite1, "Form", Calendar.getInstance());
        
        Identite identite2 = new Identite();
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        dao.insert(identite2, "Form", Calendar.getInstance());
        
        // Find resp formations from the database
        RespFormation[] result = dao.findRespForm(null, "Doe", null, null);
        
        // Verify that the correct number of resp formations are found
        assertEquals(1, result.length);
    }
}