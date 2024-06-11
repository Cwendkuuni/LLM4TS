import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;
    private EntityManagerFactory emf;

    @Before
    public void setUp() throws Exception {
        dao = new OracleIdentiteDao();
        emf = Persistence.createEntityManagerFactory("Identite");
    }

    @After
    public void tearDown() throws Exception {
        if(emf != null) {
            emf.close();
        }
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "Form", null);

        // Verify that the identity was inserted
        assertNotNull(identite.getId());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "Form", null);

        identite.setEmail("updated@example.com");
        identite.setNom("Updated");
        identite.setPrenom("User");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        dao.update(identite);

        // Retrieve the updated identity and verify its values
        Identite updatedIdentite = emf.createEntityManager().find(Identite.class, identite.getId());

        assertEquals("updated@example.com", updatedIdentite.getEmail());
        assertEquals("Updated", updatedIdentite.getNom());
        assertEquals("User", updatedIdentite.getPrenom());
        assertEquals("F", updatedIdentite.getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, updatedIdentite.getType());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setEmail("test@example.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite, "Form", null);

        dao.delete(identite);

        // Verify that the identity was deleted by trying to retrieve it
        assertNull(emf.createEntityManager().find(Identite.class, identite.getId()));
    }

    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test");
        identite1.setPrenom("User");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test");
        identite2.setPrenom("User");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite1, "Form", null);
        dao.insert(identite2, "Form", null);

        // Perform the find query with null values to retrieve all identities
        List<Identite> resultList = dao.find(null, null, null, null, null);

        // Verify that both identities are found
        assertEquals(2, resultList.size());

        // Retrieve the found identities and verify their values
        Identite foundIdentite1 = resultList.get(0);
        Identite foundIdentite2 = resultList.get(1);

        assertEquals("test1@example.com", foundIdentite1.getEmail());
        assertEquals("Test", foundIdentite1.getNom());
        assertEquals("User", foundIdentite1.getPrenom());
        assertEquals("M", foundIdentite1.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite1.getType());

        assertEquals("test2@example.com", foundIdentite2.getEmail());
        assertEquals("Test", foundIdentite2.getNom());
        assertEquals("User", foundIdentite2.getPrenom());
        assertEquals("M", foundIdentite2.getSex());
        assertEquals(Identite.TypeIdentite.CANDIDAT, foundIdentite2.getType());
    }

    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test");
        identite1.setPrenom("User");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);

        Identite identite2 = new Identite();
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test");
        identite2.setPrenom("User");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.CANDIDAT);

        dao.insert(identite1, "Form", null);
        dao.insert(identite2, "Form", null);

        // Perform the findCandidat query with null values to retrieve all candidats
        Candidat[] candidats = dao.findCandidat(null, null, null, null);

        // Verify that both candidats are found
        assertEquals(2, candidats.length);

        // Verify the values of the found candidats
        assertEquals("test1@example.com", candidats[0].getEmail());
        assertEquals("Test", candidats[0].getNom());
        assertEquals("User", candidats[0].getPrenom());
        assertEquals("M", candidats[0].getSex());

        assertEquals("test2@example.com", candidats[1].getEmail());
        assertEquals("Test", candidats[1].getNom());
        assertEquals("User", candidats[1].getPrenom());
        assertEquals("M", candidats[1].getSex());
    }

    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setEmail("test1@example.com");
        identite1.setNom("Test");
        identite1.setPrenom("User");
        identite1.setSex("M");
        identite1.setType(Identite.TypeIdentite.FORMATEUR);

        Identite identite2 = new Identite();
        identite2.setEmail("test2@example.com");
        identite2.setNom("Test");
        identite2.setPrenom("User");
        identite2.setSex("M");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);

        dao.insert(identite1, "Form", null);
        dao.insert(identite2, "Form", null);

        // Perform the findRespForm query with null values to retrieve all responsables de formation
        RespFormation[] respFormations = dao.findRespForm(null, null, null, null);

        // Verify that both responsables de formation are found
        assertEquals(2, respFormations.length);

        // Verify the values of the found responsables de formation
        assertEquals("test1@example.com", respFormations[0].getEmail());
        assertEquals("Test", respFormations[0].getNom());
        assertEquals("User", respFormations[0].getPrenom());
        assertEquals("M", respFormations[0].getSex());

        assertEquals("test2@example.com", respFormations[1].getEmail());
        assertEquals("Test", respFormations[1].getNom());
        assertEquals("User", respFormations[1].getPrenom());
        assertEquals("M", respFormations[1].getSex());
    }

    @Test(expected = PersistenceException.class)
    public void testInsertNullIdentite() {
        dao.insert(null, "Form", null);
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateNullIdentite() {
        dao.update(null);
    }

    @Test(expected = PersistenceException.class)
    public void testDeleteNullIdentite() {
        dao.delete(null);
    }
}

