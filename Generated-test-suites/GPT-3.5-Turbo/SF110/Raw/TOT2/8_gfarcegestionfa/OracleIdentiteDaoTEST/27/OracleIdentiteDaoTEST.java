package fr.unice.gfarce.dao;

import fr.unice.gfarce.identity.*;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao identiteDao;

    @Before
    public void setUp() {
        identiteDao = new OracleIdentiteDao();
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setEmail("john.doe@example.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.insert(identite, "Form", Calendar.getInstance());

        assertNotNull(identite.getId());
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(2);
        identite.setNom("Jane");
        identite.setPrenom("Doe");
        identite.setEmail("jane.doe@example.com");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.FORMATEUR);

        identiteDao.insert(identite, "Form", Calendar.getInstance());
        identite.setNom("Janet");

        identiteDao.update(identite);

        assertEquals("Janet", identite.getNom());
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(3);
        identite.setNom("Alice");
        identite.setPrenom("Smith");
        identite.setEmail("alice.smith@example.com");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        identiteDao.insert(identite, "Form", Calendar.getInstance());

        identiteDao.delete(identite);

        assertNull(identiteDao.find("Alice", "Smith", "F", "alice.smith@example.com", Identite.TypeIdentite.CANDIDAT));
    }

    // Additional test cases for find method can be added here

}