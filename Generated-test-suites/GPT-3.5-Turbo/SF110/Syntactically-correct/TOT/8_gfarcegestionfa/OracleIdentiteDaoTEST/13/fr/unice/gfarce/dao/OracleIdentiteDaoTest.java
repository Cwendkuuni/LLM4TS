import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class OracleIdentiteDaoTest {
    
    private static OracleIdentiteDao dao;
    private static Identite identite1, identite2, identite3;
    
    @BeforeClass
    public static void setUp() {
        dao = OracleIdentiteDao.getDAO();
        identite1 = new Identite();
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setEmail("john.doe@example.com");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        identite2 = new Identite();
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setEmail("jane.doe@example.com");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);
        identite3 = new Identite();
        identite3.setNom("David");
        identite3.setPrenom("Smith");
        identite3.setEmail("david.smith@example.com");
        identite3.setSex("Male");
        identite3.setType(Identite.TypeIdentite.FORMATEUR);
    }
    
    @Test
    public void testInsert() {
        dao.insert(identite1, "FormA", Calendar.getInstance());
        dao.insert(identite2, "FormB", Calendar.getInstance());
        dao.insert(identite3, "FormC", Calendar.getInstance());
        List<Identite> resultList = dao.find(null, null, null, null, null);
        assertEquals(3, resultList.size());
        assertTrue(resultList.contains(identite1));
        assertTrue(resultList.contains(identite2));
        assertTrue(resultList.contains(identite3));
    }
    
    @Test
    public void testUpdate() {
        dao.insert(identite1, "FormA", Calendar.getInstance());
        identite1.setEmail("newemail@example.com");
        identite1.setNom("John Updated");
        identite1.setPrenom("Doe Updated");
        identite1.setSex("Other");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);
        dao.update(identite1);
        List<Identite> resultList = dao.find("John Updated", "Doe Updated", "Other", "newemail@example.com", Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, resultList.size());
        assertEquals(identite1, resultList.get(0));
    }
    
    @Test
    public void testDelete() {
        dao.insert(identite1, "FormA", Calendar.getInstance());
        dao.delete(identite1);
        List<Identite> resultList = dao.find(null, null, null, null, null);
        assertEquals(0, resultList.size());
        assertFalse(resultList.contains(identite1));
    }
    
    @Test
    public void testFind() {
        dao.insert(identite1, "FormA", Calendar.getInstance());
        dao.insert(identite2, "FormB", Calendar.getInstance());
        dao.insert(identite3, "FormC", Calendar.getInstance());
        List<Identite> resultList = dao.find("Jane", "Doe", "Female", null, Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, resultList.size());
        assertEquals(identite2, resultList.get(0));
    }
    
    @Test
    public void testFindCandidat() {
        dao.insert(identite1, "FormA", Calendar.getInstance());
        dao.insert(identite2, "FormB", Calendar.getInstance());
        dao.insert(identite3, "FormC", Calendar.getInstance());
        Candidat[] resultArray = dao.findCandidat("John", "Doe", null, null);
        assertEquals(1, resultArray.length);
        assertEquals(identite1, resultArray[0]);
    }
    
    @Test
    public void testFindRespForm() {
        dao.insert(identite1, "FormA", Calendar.getInstance());
        dao.insert(identite2, "FormB", Calendar.getInstance());
        dao.insert(identite3, "FormC", Calendar.getInstance());
        RespFormation[] resultArray = dao.findRespForm(null, "Doe", "Male", null);
        assertEquals(1, resultArray.length);
        assertEquals(identite1, resultArray[0]);
    }

}