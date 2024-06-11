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

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
    private static final String PERSISTENCE_UNIT_NAME = "Identite";
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;
    private OracleIdentiteDao oracleIdentiteDao;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
        tx = em.getTransaction();
        oracleIdentiteDao = new OracleIdentiteDao();
        oracleIdentiteDao.em = em;
        oracleIdentiteDao.persistUnitName = PERSISTENCE_UNIT_NAME;
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");

        oracleIdentiteDao.insert(identite, "Form", null);

        Identite result = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), result.getNom());
        assertEquals(identite.getPrenom(), result.getPrenom());
        assertEquals(identite.getEmail(), result.getEmail());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");

        em.persist(identite);
        em.flush();
        em.clear();

        identite.setNom("Jane");
        identite.setPrenom("Dane");
        identite.setEmail("jane.dane@example.com");

        oracleIdentiteDao.update(identite);

        Identite result = em.find(Identite.class, identite.getId());
        assertEquals(identite.getNom(), result.getNom());
        assertEquals(identite.getPrenom(), result.getPrenom());
        assertEquals(identite.getEmail(), result.getEmail());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");

        em.persist(identite);
        em.flush();
        em.clear();

        oracleIdentiteDao.delete(identite);

        Identite result = em.find(Identite.class, identite.getId());
        assertNull(result);
    }

    @Test
    public void testFind() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        oracleIdentiteDao.insert(identite, "Form", null);

        RespFormation[] result = oracleIdentiteDao.findRespForm("John", "Doe", null, "john.doe@example.com");
        assertEquals(0, result.length);

        Candidat[] candidatResult = oracleIdentiteDao.findCandidat("John", "Doe", null, "john.doe@example.com");
        assertEquals(1, candidatResult.length);
        assertEquals(identite.getNom(), candidatResult[0].getNom());
        assertEquals(identite.getPrenom(), candidatResult[0].getPrenom());
        assertEquals(identite.getEmail(), candidatResult[0].getEmail());

        result = oracleIdentiteDao.findRespForm(null, null, null, null);
        assertEquals(0, result.length);
    }

    @After
    public void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}