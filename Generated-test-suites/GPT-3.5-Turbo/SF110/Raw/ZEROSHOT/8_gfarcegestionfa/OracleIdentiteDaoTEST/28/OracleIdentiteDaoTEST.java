package fr.unice.gfarce.dao;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Calendar;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao oracleIdentiteDao;
    
    @Before
    public void setUp() {
        oracleIdentiteDao = new OracleIdentiteDao();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite("John", "Doe");
        Calendar dateLimite = Calendar.getInstance();
        oracleIdentiteDao.insert(identite, "testForm", dateLimite);
        
        // Assert code here
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite("John", "Doe");
        identite.setEmail("john.doe@gmail.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.update(identite);
        
        // Assert code here
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite("John", "Doe");
        identite.setEmail("john.doe@gmail.com");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        oracleIdentiteDao.delete(identite);
        
        // Assert code here
    }
    
    @Test
    public void testFind() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "john.doe@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        List<Identite> resultList = oracleIdentiteDao.find(nom, prenom, sex, email, type);
        
        // Assert code here
    }
    
    @Test
    public void testFindCandidat() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "john.doe@gmail.com";
        
        Candidat[] result = oracleIdentiteDao.findCandidat(nom, prenom, sex, email);
        
        // Assert code here
    }
    
    @Test
    public void testFindRespForm() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "M";
        String email = "john.doe@gmail.com";
        
        RespFormation[] result = oracleIdentiteDao.findRespForm(nom, prenom, sex, email);
        
        // Assert code here
    }
}