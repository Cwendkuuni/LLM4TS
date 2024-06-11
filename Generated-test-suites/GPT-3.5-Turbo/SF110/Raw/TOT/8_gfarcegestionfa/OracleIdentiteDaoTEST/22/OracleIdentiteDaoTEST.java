import org.junit.*;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

    @Test
    public void testInsert() {
        // Typical use case
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        dateLimite.set(2022, 0, 1); // January 1, 2022

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.insert(identite, "Form", dateLimite);

        // Check if insert was successful
        Identite insertedIdentite = dao.findCandidat("John", "Doe", "male", "email@example.com")[0];
        assertNotNull(insertedIdentite);
    }

    @Test
    public void testUpdate() {
        // Typical use case
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("new_email@example.com");
        identite.setNom("Jane");
        identite.setPrenom("Smith");
        identite.setSex("female");
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        dao.update(identite);

        // Check if update was successful
        Identite updatedIdentite = dao.findRespForm("Jane", "Smith", "female", "new_email@example.com")[0];
        assertEquals("Jane", updatedIdentite.getNom());
        assertEquals("Smith", updatedIdentite.getPrenom());
        assertEquals("female", updatedIdentite.getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, updatedIdentite.getType());
    }

    @Test
    public void testDelete() {
        // Typical use case
        Identite identite = new Identite();
        identite.setId(1);

        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite toBeDeleted = dao.findCandidat(null, null, null, null)[0];
        dao.delete(toBeDeleted);

        // Check if delete was successful
        Identite deletedIdentite = dao.findCandidat(null, null, null, null)[0];
        assertNull(deletedIdentite);
    }

    @Test
    public void testFind() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite1 = new Identite();
        identite1.setEmail("email1@example.com");
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setSex("male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite();
        identite2.setEmail("email2@example.com");
        identite2.setNom("Jane");
        identite2.setPrenom("Smith");
        identite2.setSex("female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());

        // Typical use case
        List<Identite> resultList1 = dao.find("John", "Doe", "male", "email1@example.com", Identite.TypeIdentite.CANDIDAT);
        assertEquals(1, resultList1.size());
        Identite foundIdentite1 = resultList1.get(0);
        assertEquals("John", foundIdentite1.getNom());
        assertEquals("Doe", foundIdentite1.getPrenom());
        assertEquals("male", foundIdentite1.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite1.getType());

        // Edge case
        List<Identite> resultList2 = dao.find(null, null, null, null, null);
        assertEquals(2, resultList2.size());

        // Error scenario
        List<Identite> resultList3 = dao.find("NonExisting", null, null, null, null);
        assertTrue(resultList3.isEmpty());
    }

    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite1 = new Identite();
        identite1.setEmail("email1@example.com");
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setSex("male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite();
        identite2.setEmail("email2@example.com");
        identite2.setNom("Jane");
        identite2.setPrenom("Smith");
        identite2.setSex("female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());

        // Typical use case
        Candidat[] resultArray1 = dao.findCandidat("John", "Doe", "male", "email1@example.com");
        assertNotNull(resultArray1);
        assertEquals(1, resultArray1.length);
        Candidat foundCandidat1 = resultArray1[0];
        assertEquals("John", foundCandidat1.getNom());
        assertEquals("Doe", foundCandidat1.getPrenom());
        assertEquals("male", foundCandidat1.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, foundCandidat1.getType());

        // Edge case
        Candidat[] resultArray2 = dao.findCandidat(null, null, null, null);
        assertNotNull(resultArray2);
        assertEquals(1, resultArray2.length);

        // Error scenario
        Candidat[] resultArray3 = dao.findCandidat("NonExisting", null, null, null);
        assertNull(resultArray3);
    }

    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite1 = new Identite();
        identite1.setEmail("email1@example.com");
        identite1.setNom("John");
        identite1.setPrenom("Doe");
        identite1.setSex("male");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Identite identite2 = new Identite();
        identite2.setEmail("email2@example.com");
        identite2.setNom("Jane");
        identite2.setPrenom("Smith");
        identite2.setSex("female");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        dao.insert(identite1, "Form", Calendar.getInstance());
        dao.insert(identite2, "Form", Calendar.getInstance());

        // Typical use case
        RespFormation[] resultArray1 = dao.findRespForm("Jane", "Smith", "female", "email2@example.com");
        assertNotNull(resultArray1);
        assertEquals(1, resultArray1.length);
        RespFormation foundRespForm1 = resultArray1[0];
        assertEquals("Jane", foundRespForm1.getNom());
        assertEquals("Smith", foundRespForm1.getPrenom());
        assertEquals("female", foundRespForm1.getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, foundRespForm1.getType());

        // Edge case
        RespFormation[] resultArray2 = dao.findRespForm(null, null, null, null);
        assertNotNull(resultArray2);
        assertEquals(1, resultArray2.length);

        // Error scenario
        RespFormation[] resultArray3 = dao.findRespForm("NonExisting", null, null, null);
        assertNull(resultArray3);
    }
}