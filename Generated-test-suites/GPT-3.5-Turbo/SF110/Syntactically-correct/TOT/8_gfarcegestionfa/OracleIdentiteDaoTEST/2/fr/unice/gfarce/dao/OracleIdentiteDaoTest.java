import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao oracleIdentiteDao;

    @Before
    public void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@gmail.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        Calendar dateLimite = Calendar.getInstance();

        oracleIdentiteDao.insert(identite, "Test Form", dateLimite);

        List<Identite> resultList = oracleIdentiteDao.find("John", "Doe", null, "test@gmail.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, resultList.size());
        assertEquals(identite, resultList.get(0));

    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@gmail.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        oracleIdentiteDao.insert(identite, "Test Form", Calendar.getInstance());

        identite.setNom("Updated John");
        identite.setPrenom("Updated Doe");
        identite.setEmail("updated@gmail.com");

        oracleIdentiteDao.update(identite);

        List<Identite> resultList = oracleIdentiteDao.find("Updated John", "Updated Doe", null, "updated@gmail.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, resultList.size());
        assertEquals(identite, resultList.get(0));
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@gmail.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        oracleIdentiteDao.insert(identite, "Test Form", Calendar.getInstance());

        oracleIdentiteDao.delete(identite);

        List<Identite> resultList = oracleIdentiteDao.find("John", "Doe", null, "test@gmail.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test@gmail.com");
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@gmail.com");
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        oracleIdentiteDao.insert(identite1, "Test Form", Calendar.getInstance());
        oracleIdentiteDao.insert(identite2, "Test Form", Calendar.getInstance());

        List<Identite> resultList = oracleIdentiteDao.find("John", null, null, null, null);
        assertEquals(1, resultList.size());
        assertEquals(identite1, resultList.get(0));

        resultList = oracleIdentiteDao.find(null, "Doe", "Female", null, Identite.TypeIdentite.FORMATEUR);
        assertEquals(1, resultList.size());
        assertEquals(identite2, resultList.get(0));
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test@gmail.com");
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@gmail.com");
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);

        oracleIdentiteDao.insert(identite1, "Test Form", Calendar.getInstance());
        oracleIdentiteDao.insert(identite2, "Test Form", Calendar.getInstance());

        Candidat[] resultList = oracleIdentiteDao.findCandidat("John", null, null, null);
        assertEquals(1, resultList.length);
        assertEquals(identite1, resultList[0]);
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(1);
        identite1.setEmail("test@gmail.com");
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setSex("Male");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);

        Identite identite2 = new Identite();
        identite2.setId(2);
        identite2.setEmail("test2@gmail.com");
        identite2.setNom("Jane");
        identite2.setPrenom("Doe");
        identite2.setSex("Female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        oracleIdentiteDao.insert(identite1, "Test Form", Calendar.getInstance());
        oracleIdentiteDao.insert(identite2, "Test Form", Calendar.getInstance());

        RespFormation[] resultList = oracleIdentiteDao.findRespForm(null, null, null, "test2@gmail.com");
        assertEquals(1, resultList.length);
        assertEquals(identite2, resultList[0]);
    }
}