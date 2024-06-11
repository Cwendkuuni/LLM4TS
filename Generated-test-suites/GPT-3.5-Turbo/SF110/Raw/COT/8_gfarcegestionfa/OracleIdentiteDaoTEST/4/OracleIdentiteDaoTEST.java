
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    private OracleIdentiteDao oracleIdentiteDao;

    @Before
    public void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1);
        
        oracleIdentiteDao.insert(identite, "Form", calendar);
        
        // Verify that the insert operation was successful
        List<Identite> resultList = oracleIdentiteDao.findCandidat("John", "Doe", "M", "test@test.com");
        assertTrue(resultList.contains(identite));
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.insert(identite, "Form", null);

        // Update the email of the identite
        identite.setEmail("updated@test.com");
        
        oracleIdentiteDao.update(identite);
        
        // Verify that the update operation was successful
        List<Identite> resultList = oracleIdentiteDao.findCandidat("John", "Doe", "M", "updated@test.com");
        assertTrue(resultList.contains(identite));
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setEmail("test@test.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.insert(identite, "Form", null);
        
        oracleIdentiteDao.delete(identite);
        
        // Verify that the delete operation was successful
        List<Identite> resultList = oracleIdentiteDao.findCandidat("John", "Doe", "M", "test@test.com");
        assertFalse(resultList.contains(identite));
    }

    @Test
    public void testFind() {
        List<Identite> resultList = oracleIdentiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);

        // Verify that the find operation returns all candidat identities
        assertNotNull(resultList);
        assertTrue(resultList.size() > 0);
    }

    @Test
    public void testFindCandidat() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "test@test.com";
        
        List<Identite> resultList = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);

        // Verify that the findCandidat operation returns the correct result
        assertNotNull(resultList);
        assertTrue(resultList.size() > 0);
        for (Identite identite : resultList) {
            assertEquals(nom, identite.getNom());
            assertEquals(prenom, identite.getPrenom());
            assertEquals(sex, identite.getSex());
            assertEquals(email, identite.getEmail());
            assertEquals(Identite.TypeIdentite.CANDIDAT, identite.getType());
        }
    }

    @Test
    public void testFindRespForm() {
        String nom = "Jane";
        String prenom = "Doe";
        String sex = "F";
        String email = "test@test.com";
        
        List<Identite> resultList = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);

        // Verify that the findRespForm operation returns the correct result
        assertNotNull(resultList);
        assertTrue(resultList.size() > 0);
        for (Identite identite : resultList) {
            assertEquals(nom, identite.getNom());
            assertEquals(prenom, identite.getPrenom());
            assertEquals(sex, identite.getSex());
            assertEquals(email, identite.getEmail());
            assertEquals(Identite.TypeIdentite.FORMATEUR, identite.getType());
        }
    }
}
