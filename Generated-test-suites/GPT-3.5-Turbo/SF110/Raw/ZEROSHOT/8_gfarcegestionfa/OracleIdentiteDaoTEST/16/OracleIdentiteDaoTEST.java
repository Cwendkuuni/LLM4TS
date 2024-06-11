package fr.unice.gfarce.dao;
import org.junit.jupiter.api.*;
import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;
class OracleIdentiteDaoTest {
    OracleIdentiteDao oracleIdentiteDao;

    @BeforeEach
    void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
    }

    @AfterEach
    void tearDown() {
        oracleIdentiteDao = null;
    }

    @Test
    void testInsert() {
        Identite identite = new Identite();
        String nomForm = "TestNomForm";
        Calendar dateLimite = Calendar.getInstance();
        oracleIdentiteDao.insert(identite, nomForm, dateLimite);
        // add assertion here
    }

    @Test
    void testUpdate() {
        Identite identite = new Identite();
        oracleIdentiteDao.update(identite);
        // add assertion here
    }

    @Test
    void testDelete() {
        Identite identite = new Identite();
        oracleIdentiteDao.delete(identite);
        // add assertion here
    }

    @Test
    void testFind() {
        String nom = "TestNom";
        String prenom = "TestPrenom";
        String sex = "TestSex";
        String email = "TestEmail";
        Identite.TypeIdentite type = null;
        oracleIdentiteDao.find(nom, prenom, sex, email, type);
        // add assertion here
    }

    @Test
    void testFindCandidat() {
        String nom = "TestNom";
        String prenom = "TestPrenom";
        String sex = "TestSex";
        String email = "TestEmail";
        oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        // add assertion here
    }

    @Test
    void testFindRespForm() {
        String nom = "TestNom";
        String prenom = "TestPrenom";
        String sex = "TestSex";
        String email = "TestEmail";
        oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        // add assertion here
    }
}