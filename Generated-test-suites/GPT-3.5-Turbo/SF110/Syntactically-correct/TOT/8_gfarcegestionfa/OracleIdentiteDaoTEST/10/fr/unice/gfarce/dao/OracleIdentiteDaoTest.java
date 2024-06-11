
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;
    private Identite identite;
    private String nomForm;
    private Calendar dateLimite;

    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
        identite = new Identite();
        nomForm = "Form Name";
        dateLimite = Calendar.getInstance();
    }

    @Test
    public void testInsert() {
        dao.insert(identite, nomForm, dateLimite);
        // check if the identite is inserted in the database successfully
    }

    @Test
    public void testUpdate() {
        Identite updatedIdentite = new Identite();
        // set the attributes of updatedIdentite
        dao.update(updatedIdentite);
        // check if the identite is updated successfully in the database
    }

    @Test
    public void testDelete() {
        dao.delete(identite);
        // check if the identite is deleted successfully from the database
    }

    @Test
    public void testFind() {
        // prepare some data in the database to test the find method
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        // insert test data into the database

        // test case 1: search by nom and prenom
        List<Identite> result = dao.find(nom, prenom, null, null, type);
        assertEquals(1, result.size());
        assertEquals(nom, result.get(0).getNom());
        assertEquals(prenom, result.get(0).getPrenom());
        
        // test case 2: search by nom, prenom and sex
        result = dao.find(nom, prenom, sex, null, type);
        assertEquals(1, result.size());
        assertEquals(nom, result.get(0).getNom());
        assertEquals(prenom, result.get(0).getPrenom());
        assertEquals(sex, result.get(0).getSex());

        // test case 3: search by nom, prenom, sex, and email
        result = dao.find(nom, prenom, sex, email, type);
        assertEquals(1, result.size());
        assertEquals(nom, result.get(0).getNom());
        assertEquals(prenom, result.get(0).getPrenom());
        assertEquals(sex, result.get(0).getSex());
        assertEquals(email, result.get(0).getEmail());

        // test case 4: search by nom, prenom, sex, email, and type
        result = dao.find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindCandidat() {
        // prepare some data in the database to test the findCandidat method
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@example.com";

        // insert test data into the database

        // test case 1: search by nom and prenom
        Candidat[] result = dao.findCandidat(nom, prenom, null, null);
        assertEquals(1, result.length);
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());

        // test case 2: search by nom, prenom and sex
        result = dao.findCandidat(nom, prenom, sex, null);
        assertEquals(1, result.length);
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        assertEquals(sex, result[0].getSex());

        // test case 3: search by nom, prenom, sex, and email
        result = dao.findCandidat(nom, prenom, sex, email);
        assertEquals(1, result.length);
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        assertEquals(sex, result[0].getSex());
        assertEquals(email, result[0].getEmail());
    }

    @Test
    public void testFindRespForm() {
        // prepare some data in the database to test the findRespForm method
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@example.com";

        // insert test data into the database

        // test case 1: search by nom and prenom
        RespFormation[] result = dao.findRespForm(nom, prenom, null, null);
        assertEquals(1, result.length);
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        
        // test case 2: search by nom, prenom and sex
        result = dao.findRespForm(nom, prenom, sex, null);
        assertEquals(1, result.length);
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        assertEquals(sex, result[0].getSex());

        // test case 3: search by nom, prenom, sex, and email
        result = dao.findRespForm(nom, prenom, sex, email);
        assertEquals(1, result.length);
        assertEquals(nom, result[0].getNom());
        assertEquals(prenom, result[0].getPrenom());
        assertEquals(sex, result[0].getSex());
        assertEquals(email, result[0].getEmail());
    }

}

