package fr.unice.gfarce.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.Test;

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
    @Test
    public void testInsert() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Identite identite = new Identite();
        String nomForm = "John";
        Calendar dateLimite = Calendar.getInstance();
        
        dao.insert(identite, nomForm, dateLimite);
        // TODO: assert the insertion was successful
    }

    @Test
    public void testUpdate() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Identite identite = new Identite();
        
        dao.update(identite);
        // TODO: assert the update was successful
    }

    @Test
    public void testDelete() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        Identite identite = new Identite();
        
        dao.delete(identite);
        // TODO: assert the deletion was successful
    }

    @Test
    public void testFind() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "john.doe@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        // TODO: write test cases to assert the correct results from the find method
    }

    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "john.doe@example.com";
        
        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
        
        assertNull(result);
    }

    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = new OracleIdentiteDao();
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "john.doe@example.com";
        
        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
        
        assertNull(result);
    }
}