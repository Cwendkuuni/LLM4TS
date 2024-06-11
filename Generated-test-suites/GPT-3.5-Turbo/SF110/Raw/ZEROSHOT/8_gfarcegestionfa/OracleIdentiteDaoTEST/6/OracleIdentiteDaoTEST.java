package fr.unice.gfarce.dao;

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
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
        oracleIdentiteDao = OracleIdentiteDao.getDAO();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setNom("John");
        identite.setPrenom("Doe");
        Calendar dateLimite = Calendar.getInstance();

        oracleIdentiteDao.insert(identite, "test", dateLimite);

        Identite insertedIdentite = getLatestInsertedIdentite();
        assertNotNull(insertedIdentite);
        assertEquals("John", insertedIdentite.getNom());
        assertEquals("Doe", insertedIdentite.getPrenom());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("johndoe@gmail.com");
        identite.setSex("Male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        oracleIdentiteDao.update(identite);

        Identite updatedIdentite = getIdentiteById(1L);
        assertNotNull(updatedIdentite);
        assertEquals("johndoe@gmail.com", updatedIdentite.getEmail());
        assertEquals("Male", updatedIdentite.getSex());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1L);
        identite.setNom("John");
        identite.setPrenom("Doe");

        oracleIdentiteDao.delete(identite);

        Identite deletedIdentite = getIdentiteById(1L);
        assertNull(deletedIdentite);
    }

    @Test
    public void testFind() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        List<Identite> identiteList = oracleIdentiteDao.find(nom, prenom, sex, email, type);

        assertNotNull(identiteList);
        assertFalse(identiteList.isEmpty());
    }

    @Test
    public void testFindCandidat() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@gmail.com";

        Candidat[] candidats = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);

        assertNotNull(candidats);
        assertTrue(candidats.length > 0);
    }

    @Test
    public void testFindRespForm() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@gmail.com";

        RespFormation[] respFormations = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);

        assertNotNull(respFormations);
        assertTrue(respFormations.length > 0);
    }

    private Identite getLatestInsertedIdentite() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        Identite identite = (Identite) em.createQuery("SELECT i FROM Identite i ORDER BY i.id DESC")
                .setMaxResults(1)
                .getSingleResult();
        em.close();
        emf.close();
        return identite;
    }

    private Identite getIdentiteById(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        Identite identite = em.find(Identite.class, id);
        em.close();
        emf.close();
        return identite;
    }
}