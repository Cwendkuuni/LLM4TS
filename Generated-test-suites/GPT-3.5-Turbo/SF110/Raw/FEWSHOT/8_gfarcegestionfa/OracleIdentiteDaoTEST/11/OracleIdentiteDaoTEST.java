import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.Calendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fr.unice.gfarce.dao.IdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
    private OracleIdentiteDao oracleIdentiteDao;

    @Before
    public void setUp() {
        oracleIdentiteDao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown() {
        oracleIdentiteDao = null;
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.DECEMBER, 31);
        
        oracleIdentiteDao.insert(identite, "Test Form", calendar);
        
        // Verify insertion
        assertEquals("John", identite.getNom());
        assertEquals("Doe", identite.getPrenom());
        assertNotNull(identite.getId());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        
        oracleIdentiteDao.update(identite);
        
        // Verify update
        assertEquals("John", identite.getNom());
        assertEquals("Doe", identite.getPrenom());
        assertNotNull(identite.getId());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        
        oracleIdentiteDao.delete(identite);
        
        // Verify delete
        assertNull(identite.getId());
        assertNull(identite.getNom());
        assertNull(identite.getPrenom());
    }

    @Test
    public void testFind() {
        String nom = "Doe";
        String prenom = "John";
        String sex = "Male";
        String email = "john.doe@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        // Test with specific values
        List<Identite> resultList = oracleIdentiteDao.find(nom, prenom, sex, email, type);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Identite result = resultList.get(0);
        assertNotNull(result);
        assertEquals(nom, result.getNom());
        assertEquals(prenom, result.getPrenom());
        assertEquals(sex, result.getSex());
        assertEquals(email, result.getEmail());
        assertEquals(type, result.getType());
        
        // Test with null values
        resultList = oracleIdentiteDao.find(null, null, null, null, type);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        result = resultList.get(0);
        assertNotNull(result);
        // Verify the other attributes are not null
        assertNotNull(result.getNom());
        assertNotNull(result.getPrenom());
        assertNotNull(result.getSex());
        assertNotNull(result.getEmail());
        assertNotNull(result.getType());
    }

    @Test
    public void testFindCandidat() {
        String nom = "Doe";
        String prenom = "John";
        String sex = "Male";
        String email = "john.doe@example.com";
        
        Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        
        assertNotNull(result);
        // Verify the other attributes are not null
        assertNotNull(result[0].getNom());
        assertNotNull(result[0].getPrenom());
        assertNotNull(result[0].getSex());
        assertNotNull(result[0].getEmail());
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        assertEquals(sex, result[0].getSex());
        assertEquals(email, result[0].getEmail());
    }

    @Test
    public void testFindRespForm() {
        String nom = "Doe";
        String prenom = "John";
        String sex = "Male";
        String email = "john.doe@example.com";
        
        RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        
        assertNotNull(result);
        // Verify the other attributes are not null
        assertNotNull(result[0].getNom());
        assertNotNull(result[0].getPrenom());
        assertNotNull(result[0].getSex());
        assertNotNull(result[0].getEmail());
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        assertEquals(sex, result[0].getSex());
        assertEquals(email, result[0].getEmail());
    }
}