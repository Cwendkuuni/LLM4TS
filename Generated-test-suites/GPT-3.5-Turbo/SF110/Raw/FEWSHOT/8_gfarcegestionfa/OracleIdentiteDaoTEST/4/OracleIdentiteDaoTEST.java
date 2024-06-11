package fr.unice.gfarce.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao dao;
    private String persistUnitName = "Identite";
    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setup() {
        dao = new OracleIdentiteDao();
        emf = Persistence.createEntityManagerFactory(persistUnitName);
        em = emf.createEntityManager();
        dao.em = em;
    }

    @After
    public void teardown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testInsert() {
        try {
            Identite identite = new Identite();
            String nomForm = "form";
            Calendar dateLimite = Calendar.getInstance();

            dao.insert(identite, nomForm, dateLimite);
            // Test insertion
            Identite insertedIdentite = em.find(Identite.class, identite.getId());
            assertEquals(identite, insertedIdentite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {
        try {
            Identite identite = new Identite();
            identite.setEmail("email");
            identite.setNom("nom");
            identite.setPrenom("prenom");
            identite.setSex("sex");
            identite.setType(Identite.TypeIdentite.CANDIDAT);

            em.persist(identite);

            Identite updatedIdentite = new Identite();
            updatedIdentite.setId(identite.getId());
            updatedIdentite.setEmail("updatedEmail");
            updatedIdentite.setNom("updatedNom");
            updatedIdentite.setPrenom("updatedPrenom");
            updatedIdentite.setSex("updatedSex");
            updatedIdentite.setType(Identite.TypeIdentite.FORMATEUR);

            dao.update(updatedIdentite);
            // Test update
            Identite foundIdentite = em.find(Identite.class, updatedIdentite.getId());
            assertEquals(updatedIdentite.getEmail(), foundIdentite.getEmail());
            assertEquals(updatedIdentite.getNom(), foundIdentite.getNom());
            assertEquals(updatedIdentite.getPrenom(), foundIdentite.getPrenom());
            assertEquals(updatedIdentite.getSex(), foundIdentite.getSex());
            assertEquals(updatedIdentite.getType(), foundIdentite.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            Identite identite = new Identite();
            em.persist(identite);
            long identiteId = identite.getId();

            dao.delete(identite);
            // Test deletion
            Identite deletedIdentite = em.find(Identite.class, identiteId);
            assertNull(deletedIdentite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFind() {
        try {
            String nom = "nom";
            String prenom = "prenom";
            String sex = "sex";
            String email = "email";
            Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

            // Test case where all parameters are null
            dao.find(null, null, null, null, null);

            // Test case with specified parameters
            dao.find(nom, prenom, sex, email, type);
            // Add asserts for checking the query results if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindCandidat() {
        try {
            String nom = "nom";
            String prenom = "prenom";
            String sex = "sex";
            String email = "email";

            dao.findCandidat(nom, prenom, sex, email);
            // Add asserts for checking the query results if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindRespForm() {
        try {
            String nom = "nom";
            String prenom = "prenom";
            String sex = "sex";
            String email = "email";

            dao.findRespForm(nom, prenom, sex, email);
            // Add asserts for checking the query results if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}