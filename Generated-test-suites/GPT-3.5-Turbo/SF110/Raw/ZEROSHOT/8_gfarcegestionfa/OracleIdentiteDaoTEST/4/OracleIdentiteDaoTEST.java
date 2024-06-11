package fr.unice.gfarce.dao;

import org.junit.*;
import static org.junit.Assert.*;
import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    OracleIdentiteDao oracleIdentiteDao;

    @Before
    public void setUp() {
        oracleIdentiteDao = OracleIdentiteDao.getDAO();
    }

    @After
    public void tearDown() {
        oracleIdentiteDao = null;
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        String nomForm = "test";
        Calendar dateLimite = Calendar.getInstance();
        oracleIdentiteDao.insert(identite, nomForm, dateLimite);
        // insert test here
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        oracleIdentiteDao.update(identite);
        // update test here
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        oracleIdentiteDao.delete(identite);
        // delete test here
    }

    @Test
    public void testFind() {
        String nom = "testNom";
        String prenom = "testPrenom";
        String sex = "testSex";
        String email = "testEmail";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
        // find test here
    }

    @Test
    public void testFindCandidat() {
        String nom = "testNom";
        String prenom = "testPrenom";
        String sex = "testSex";
        String email = "testEmail";
        
        Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        // findCandidat test here
    }

    @Test
    public void testFindRespForm() {
        String nom = "testNom";
        String prenom = "testPrenom";
        String sex = "testSex";
        String email = "testEmail";
        
        RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        // findRespForm test here
    }
}