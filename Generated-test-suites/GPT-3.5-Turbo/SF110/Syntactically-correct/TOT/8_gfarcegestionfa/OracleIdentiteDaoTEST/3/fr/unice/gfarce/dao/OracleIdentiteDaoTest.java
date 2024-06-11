import fr.unice.gfarce.dao.OracleIdentiteDao;
import fr.unice.gfarce.identity.Identite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    private EntityManagerFactory emf;

    @Before
    public void setUp() {
        dao = OracleIdentiteDao.getDAO();
        emf = Persistence.createEntityManagerFactory("Identite");
    }

    @After
    public void tearDown() {
        emf.close();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setEmail("john.doe@example.com");
        Calendar dateLimite = Calendar.getInstance();

        dao.insert(identite, "Form", dateLimite);

        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT i FROM Identite i WHERE i.id = :id");
        query.setParameter("id", 1);
        Identite insertedIdentite = (Identite) query.getSingleResult();

        assertNotNull(insertedIdentite);
        assertEquals(identite.getId(), insertedIdentite.getId());
        assertEquals(identite.getNom(), insertedIdentite.getNom());
        assertEquals(identite.getPrenom(), insertedIdentite.getPrenom());
        assertEquals(identite.getSex(), insertedIdentite.getSex());
        assertEquals(identite.getEmail(), insertedIdentite.getEmail());
        
        em.close();
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(2);
        identite.setNom("Jane");
        identite.setPrenom("Doe");
        identite.setSex("Female");
        identite.setEmail("jane.doe@example.com");

        dao.insert(identite, "Form", Calendar.getInstance());

        identite.setEmail("jane.doe@example.org");
        identite.setSex("Other");

        dao.update(identite);

        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT i FROM Identite i WHERE i.id = :id");
        query.setParameter("id", 2);
        Identite updatedIdentite = (Identite) query.getSingleResult();

        assertNotNull(updatedIdentite);
        assertEquals(identite.getId(), updatedIdentite.getId());
        assertEquals(identite.getNom(), updatedIdentite.getNom());
        assertEquals(identite.getPrenom(), updatedIdentite.getPrenom());
        assertEquals(identite.getSex(), updatedIdentite.getSex());
        assertEquals(identite.getEmail(), updatedIdentite.getEmail());
        
        em.close();
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(3);
        identite.setNom("Michael");
        identite.setPrenom("Smith");
        identite.setSex("Male");
        identite.setEmail("michael.smith@example.com");

        dao.insert(identite, "Form", Calendar.getInstance());

        dao.delete(identite);

        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT i FROM Identite i WHERE i.id = :id");
        query.setParameter("id", 3);
        Identite deletedIdentite = (Identite) query.getSingleResult();

        assertNull(deletedIdentite);
        
        em.close();
    }

    @Test
    public void testFind() {
        String nom = "Doe";
        String prenom = "John";
        String sex = "Male";
        String email = "john.doe@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        List<Identite> resultList = dao.find(nom, prenom, sex, email, type);

        assertNotNull(resultList);
        for (Identite identite : resultList) {
            assertEquals(nom, identite.getNom());
            assertEquals(prenom, identite.getPrenom());
            assertEquals(sex, identite.getSex());
            assertEquals(email, identite.getEmail());
            assertEquals(type, identite.getType());
        }
    }

    @Test
    public void testFindCandidat() {
        String nom = "Doe";
        String prenom = "John";
        String sex = "Male";
        String email = "john.doe@example.com";

        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);

        assertNull(result);
    }

    @Test
    public void testFindRespForm() {
        String nom = "Doe";
        String prenom = "John";
        String sex = "Male";
        String email = "john.doe@example.com";

        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);

        assertNull(result);
    }
}