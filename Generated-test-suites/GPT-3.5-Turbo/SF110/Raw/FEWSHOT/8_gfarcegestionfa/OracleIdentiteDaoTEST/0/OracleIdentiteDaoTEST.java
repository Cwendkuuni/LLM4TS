package fr.unice.gfarce.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.Identite.TypeIdentite;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao oracleIdentiteDao;
    
    @Before
    public void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("test@gmail.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.insert(identite, "Form1", Calendar.getInstance());
        // assert statements
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setEmail("test@gmail.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.update(identite);
        // assert statements
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setEmail("test@gmail.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setType(TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.delete(identite);
        // assert statements
    }
    
    @Test
    public void testFind() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "test@gmail.com";
        TypeIdentite type = TypeIdentite.CANDIDAT;
        
        oracleIdentiteDao.find(nom, prenom, sex, email, type);
        // assert statements
    }
    
    @Test
    public void testFindCandidat() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "test@gmail.com";
        
        oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        // assert statements
    }
    
    @Test
    public void testFindRespForm() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "test@gmail.com";
        
        oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        // assert statements
    }
}