package fr.unice.gfarce.dao;

import javax.persistence.*;
import java.util.*;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {

    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("TestNom");
        identite.setPrenom("TestPrenom");
        identite.setEmail("test@test.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.insert(identite, "TestForm", Calendar.getInstance());

        // Additional assertions could be added here to check if the insert was successful
    }

    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("TestNomUpdated");
        identite.setPrenom("TestPrenomUpdated");
        identite.setEmail("test@test.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.update(identite);

        // Additional assertions could be added here to check if the update was successful
    }

    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setNom("TestNom");
        identite.setPrenom("TestPrenom");
        identite.setEmail("test@test.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.delete(identite);

        // Additional assertions could be added here to check if the delete was successful
    }

    @Test
    public void testFind() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        List<Identite> result = dao.find("TestNom", "TestPrenom", "M", "test@test.com", Identite.TypeIdentite.CANDIDAT);

        // Additional assertions could be added here to check if the find method returned the expected list of Identite objects
    }

    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Candidat[] result = dao.findCandidat("TestNom", "TestPrenom", "M", "test@test.com");

        // Additional assertions could be added here to check if the findCandidat method returned the expected array of Candidat objects
    }

    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        RespFormation[] result = dao.findRespForm("TestNom", "TestPrenom", "M", "test@test.com");

        // Additional assertions could be added here to check if the findRespForm method returned the expected array of RespFormation objects
    }
}