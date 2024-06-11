package fr.unice.gfarce.dao;

import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    private OracleIdentiteDao oracleIdentiteDao;

    @Before
    public void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
    }

    @After
    public void tearDown() {
        oracleIdentiteDao = null;
    }

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        String nomForm = "NomForm";
        Calendar dateLimite = Calendar.getInstance();
        oracleIdentiteDao.insert(identite, nomForm, dateLimite);
        //Add assertions here
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        oracleIdentiteDao.update(identite);
        //Add assertions here
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        oracleIdentiteDao.delete(identite);
        //Add assertions here
    }

    @Test
    public void testFind() {
        String nom = "Test";
        String prenom = "User";
        String sex = "Male";
        String email = "test.user@test.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        List<Identite> result = oracleIdentiteDao.find(nom, prenom, sex, email, type);
        //Add assertions here
    }

    @Test
    public void testFindCandidat() {
        String nom = "Test";
        String prenom = "User";
        String sex = "Male";
        String email = "test.user@test.com";
        Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        //Add assertions here
    }

    @Test
    public void testFindRespForm() {
        String nom = "Test";
        String prenom = "User";
        String sex = "Male";
        String email = "test.user@test.com";
        RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        //Add assertions here
    }
}