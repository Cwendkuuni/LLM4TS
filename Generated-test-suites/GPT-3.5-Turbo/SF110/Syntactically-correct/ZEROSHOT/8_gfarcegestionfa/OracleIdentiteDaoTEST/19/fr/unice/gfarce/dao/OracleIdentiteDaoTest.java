package fr.unice.gfarce.dao;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao dao;
    
    @Before
    public void setUp() {
        dao = new OracleIdentiteDao();
    }
    
    @After
    public void tearDown() {
        dao = null;
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Calendar dateLimite = Calendar.getInstance();
        
        dao.insert(identite, "nomForm", dateLimite);
        
        // verify the insertion
        
        // ...
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.update(identite);
        
        // verify the update
        
        // ...
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@test.com");
        identite.setNom("Test");
        identite.setPrenom("User");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        dao.delete(identite);
        
        // verify the deletion
        
        // ...
    }
    
    @Test
    public void testFind() {
        String nom = "Test";
        String prenom = "User";
        String sex = "M";
        String email = "test@test.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
        
        // verify the result
        
        // ...
    }
    
    @Test
    public void testFindCandidat() {
        String nom = "Test";
        String prenom = "User";
        String sex = "M";
        String email = "test@test.com";
        
        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
        
        // verify the result
        
        // ...
    }
    
    @Test
    public void testFindRespForm() {
        String nom = "Test";
        String prenom = "User";
        String sex = "M";
        String email = "test@test.com";
        
        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
        
        // verify the result
        
        // ...
    }
}