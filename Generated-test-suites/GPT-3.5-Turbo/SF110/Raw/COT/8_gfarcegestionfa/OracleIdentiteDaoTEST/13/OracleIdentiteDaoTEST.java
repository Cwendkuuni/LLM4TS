
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

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
        identite.setId(1);
        identite.setEmail("test@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(2022, Calendar.JANUARY, 1);

        dao.insert(identite, "Form", dateLimite);

        List<Identite> resultList = dao.findCandidat("John", "Doe", "Male", "test@example.com");
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(identite, resultList.get(0));
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(2);
        identite.setEmail("updated@example.com");
        identite.setNom("Jane");
        identite.setPrenom("Doe");
        identite.setSex("Female");
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        dao.update(identite);

        List<Identite> resultList = dao.findRespForm("Jane", "Doe", "Female", "updated@example.com");
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(identite, resultList.get(0));
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(3);
        identite.setEmail("delete@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "Form", null);
        dao.delete(identite);

        List<Identite> resultList = dao.findCandidat("John", "Doe", "Male", "delete@example.com");
        assertNull(resultList);
    }

    @Test
    public void testFind() {
        List<Identite> resultList = dao.find(null, null, null, null, null);
        assertNotNull(resultList);

        dao.insert(createIdentite(4, "John", "Doe", "Male", Identite.TypeIdentite.CANDIDAT), "Form", null);
        resultList = dao.find("John", "Doe", "Male", null, Identite.TypeIdentite.CANDIDAT);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());

        resultList = dao.find("John", "Doe", "Male", null, Identite.TypeIdentite.FORMATEUR);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());

        resultList = dao.find(null, null, null, null, Identite.TypeIdentite.FORMATEUR);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindCandidat() {
        dao.insert(createIdentite(5, "John", "Doe", "Male", Identite.TypeIdentite.CANDIDAT), "Form", null);

        Candidat[] resultArray = dao.findCandidat("John", "Doe", "Male", null);
        assertNotNull(resultArray);
        assertEquals(1, resultArray.length);
    }

    @Test
    public void testFindRespForm() {
        dao.insert(createIdentite(6, "Jane", "Doe", "Female", Identite.TypeIdentite.FORMATEUR), "Form", null);

        RespFormation[] resultArray = dao.findRespForm("Jane", "Doe", "Female", null);
        assertNotNull(resultArray);
        assertEquals(1, resultArray.length);
    }

    private Identite createIdentite(int id, String nom, String prenom, String sex, Identite.TypeIdentite type) {
        Identite identite = new Identite();
        identite.setId(id);
        identite.setEmail("test@example.com");
        identite.setNom(nom);
        identite.setPrenom(prenom);
        identite.setSex(sex);
        identite.setType(type);
        return identite;
    }
}
