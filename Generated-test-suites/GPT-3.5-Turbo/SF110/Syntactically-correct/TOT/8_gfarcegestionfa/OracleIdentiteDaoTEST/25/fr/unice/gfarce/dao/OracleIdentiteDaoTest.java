
package fr.unice.gfarce.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

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
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("Male");
        identite.setEmail("john.doe@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite = Calendar.getInstance();
        
        // Test for successful insert
        dao.insert(identite, "Form", dateLimite);
        
        // Verify that the entity has been inserted
        List<Identite> result = dao.find("John", "Doe", "Male", "john.doe@example.com", Identite.TypeIdentite.CANDIDAT);
        assertTrue(result.contains(identite));
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(2);
        identite.setNom("Jane");
        identite.setPrenom("Smith");
        identite.setSex("Female");
        identite.setEmail("jane.smith@example.com");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        
        // Insert the entity to be updated
        dao.insert(identite, "Form", null);
        
        // Update the entity
        identite.setEmail("jane.newemail@example.com");
        identite.setSex("Male");
        dao.update(identite);
        
        // Verify that the entity has been updated
        List<Identite> result = dao.find("Jane", "Smith", "Male", "jane.newemail@example.com", Identite.TypeIdentite.FORMATEUR);
        assertTrue(result.contains(identite));
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(3);
        identite.setNom("Sarah");
        identite.setPrenom("Johnson");
        identite.setSex("Female");
        identite.setEmail("sarah.johnson@example.com");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        // Insert the entity to be deleted
        dao.insert(identite, "Form", null);
        
        // Delete the entity
        dao.delete(identite);
        
        // Verify that the entity has been deleted
        List<Identite> result = dao.find("Sarah", "Johnson", "Female", "sarah.johnson@example.com", Identite.TypeIdentite.CANDIDAT);
        assertFalse(result.contains(identite));
    }
    
    @Test
    public void testFind() {
        Identite identite1 = new Identite();
        identite1.setId(4);
        identite1.setNom("Michael");
        identite1.setPrenom("Brown");
        identite1.setSex("Male");
        identite1.setEmail("michael.brown@example.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite1 = Calendar.getInstance();
        
        Identite identite2 = new Identite();
        identite2.setId(5);
        identite2.setNom("Emily");
        identite2.setPrenom("Davis");
        identite2.setSex("Female");
        identite2.setEmail("emily.davis@example.com");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        // Insert the entities
        dao.insert(identite1, "Form1", dateLimite1);
        dao.insert(identite2, "Form2", null);
        
        // Test finding by name and gender
        List<Identite> result1 = dao.find("Michael", null, "Male", null, null);
        assertTrue(result1.contains(identite1));
        
        // Test finding by name, gender, and type
        List<Identite> result2 = dao.find("Emily", null, "Female", null, Identite.TypeIdentite.FORMATEUR);
        assertTrue(result2.contains(identite2));
        
        // Test finding by name, gender, and email (should not match any)
        List<Identite> result3 = dao.find("Michael", null, "Female", "emily.davis@example.com", null);
        assertFalse(result3.contains(identite1));
        assertFalse(result3.contains(identite2));
        
        // Test finding by name (should return all entities)
        List<Identite> result4 = dao.find("emily", null, null, null, null);
        assertTrue(result4.containsAll(Arrays.asList(identite1, identite2)));
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite1 = new Identite();
        identite1.setId(6);
        identite1.setNom("Michael");
        identite1.setPrenom("Brown");
        identite1.setSex("Male");
        identite1.setEmail("michael.brown@example.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite1 = Calendar.getInstance();
        
        Identite identite2 = new Identite();
        identite2.setId(7);
        identite2.setNom("Emily");
        identite2.setPrenom("Davis");
        identite2.setSex("Female");
        identite2.setEmail("emily.davis@example.com");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        // Insert the entities
        dao.insert(identite1, "Form1", dateLimite1);
        dao.insert(identite2, "Form2", null);
        
        // Test finding Candidat by name and sex (should match only identite1)
        Candidat[] result1 = dao.findCandidat("Michael", null, "Male", null);
        assertEquals(1, result1.length);
        assertEquals(identite1, result1[0]);
        
        // Test finding Candidat by name and email (should not match any)
        Candidat[] result2 = dao.findCandidat("Michael", null, null, "emily.davis@example.com");
        assertEquals(0, result2.length);
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite1 = new Identite();
        identite1.setId(8);
        identite1.setNom("Michael");
        identite1.setPrenom("Brown");
        identite1.setSex("Male");
        identite1.setEmail("michael.brown@example.com");
        identite1.setType(Identite.TypeIdentite.CANDIDAT);
        Calendar dateLimite1 = Calendar.getInstance();
        
        Identite identite2 = new Identite();
        identite2.setId(9);
        identite2.setNom("Emily");
        identite2.setPrenom("Davis");
        identite2.setSex("Female");
        identite2.setEmail("emily.davis@example.com");
        identite2.setType(Identite.TypeIdentite.FORMATEUR);
        
        // Insert the entities
        dao.insert(identite1, "Form1", dateLimite1);
        dao.insert(identite2, "Form2", null);
        
        // Test finding RespFormation by name and sex (should match only identite2)
        RespFormation[] result1 = dao.findRespForm("Emily", null, "Female", null);
        assertEquals(1, result1.length);
        assertEquals(identite2, result1[0]);
        
        // Test finding RespFormation by name and email (should not match any)
        RespFormation[] result2 = dao.findRespForm("Emily", null, null, "michael.brown@example.com");
        assertEquals(0, result2.length);
    }
    
}

