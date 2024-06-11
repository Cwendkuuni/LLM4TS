import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import javax.persistence.*;
import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    OracleIdentiteDao dao;

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
        identite.setEmail("test@gmail.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        String nomForm = "Form1";
        Calendar dateLimite = Calendar.getInstance();

        dao.insert(identite, nomForm, dateLimite);

        // Check if the identite is inserted successfully

    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@gmail.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.update(identite);

        // Check if the identite is updated successfully

    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@gmail.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.delete(identite);

        // Check if the identite is deleted successfully

    }

    @Test
    public void testFind() {
        String nom = "Test";
        String prenom = "User";
        String sex = "M";
        String email = "test@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        List<Identite> result = dao.find(nom, prenom, sex, email, type);

        // Check if the find method returns the correct list

    }

    @Test
    public void testFindCandidat() {
        String nom = "Test";
        String prenom = "User";
        String sex = "M";
        String email = "test@gmail.com";

        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);

        // Check if the findCandidat method returns the correct array

    }

    @Test
    public void testFindRespForm() {
        String nom = "Test";
        String prenom = "User";
        String sex = "M";
        String email = "test@gmail.com";

        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);

        // Check if the findRespForm method returns the correct array

    }

}