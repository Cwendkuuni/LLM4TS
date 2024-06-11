
package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    @Test
    public void testInsert() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        // set properties of identite object
        String nomForm = "Form";
        Calendar dateLimite = Calendar.getInstance();
        dao.insert(identite, nomForm, dateLimite);
        // validate that identite is inserted into the database
    }
    
    @Test
    public void testUpdate() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        // set properties of identite object
        dao.update(identite);
        // validate that identite is updated in the database
    }
    
    @Test
    public void testDelete() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        Identite identite = new Identite();
        // set properties of identite object
        dao.delete(identite);
        // validate that identite is deleted from the database
    }
    
    @Test
    public void testFind() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        dao.find(nom, prenom, sex, email, type);
        // validate the returned list of identites
    }
    
    @Test
    public void testFindCandidat() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@example.com";
        dao.findCandidat(nom, prenom, sex, email);
        // validate the returned array of Candidat objects
    }
    
    @Test
    public void testFindRespForm() {
        OracleIdentiteDao dao = OracleIdentiteDao.getDAO();
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "johndoe@example.com";
        dao.findRespForm(nom, prenom, sex, email);
        // validate the returned array of RespFormation objects
    }
}

