
package fr.unice.gfarce.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;

public class OracleIdentiteDaoTest {
    
    private OracleIdentiteDao identiteDao;
    
    @Before
    public void setUp() {
        identiteDao = OracleIdentiteDao.getDAO();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", Calendar.getInstance());
        
        // Verify insertion
        Candidat[] result = identiteDao.findCandidat(null, null, null, null);
        boolean found = false;
        for(Candidat candidat : result) {
            if(candidat.getId() == identite.getId()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", Calendar.getInstance());
        
        // Update the identite
        identite.setEmail("newemail@example.com");
        identite.setNom("New John");
        identite.setPrenom("New Doe");
        identite.setSex("F");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.update(identite);
        
        // Verify update
        Identite updatedIdentite = identiteDao.findCandidat(null, null, null, null)[0];
        assertEquals("newemail@example.com", updatedIdentite.getEmail());
        assertEquals("New John", updatedIdentite.getNom());
        assertEquals("New Doe", updatedIdentite.getPrenom());
        assertEquals("F", updatedIdentite.getSex());
        assertEquals(Identite.TypeIdentite.FORMATEUR, updatedIdentite.getType());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", Calendar.getInstance());
        
        // Delete the identite
        identiteDao.delete(identite);
        
        // Verify deletion
        Identite[] result = identiteDao.findCandidat(null, null, null, null);
        boolean found = false;
        for(Identite id : result) {
            if(id.getId() == identite.getId()) {
                found = true;
                break;
            }
        }
        assertEquals(false, found);
    }
    
    @Test
    public void testFind() {
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", Calendar.getInstance());
        
        // Find the inserted identite
        Identite[] result = identiteDao.find(null, null, null, null, Identite.TypeIdentite.CANDIDAT);
        boolean found = false;
        for(Identite id : result) {
            if(id.getId() == identite.getId()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void testFindCandidat() {
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        identiteDao.insert(identite, "nomForm", Calendar.getInstance());
        
        // Find the inserted candidat
        Candidat[] result = identiteDao.findCandidat(null, null, null, null);
        boolean found = false;
        for(Candidat candidat : result) {
            if(candidat.getId() == identite.getId()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void testFindRespForm() {
        Identite identite = new Identite();
        identite.setEmail("email@example.com");
        identite.setNom("John");
        identite.setPrenom("Doe");
        identite.setSex("M");
        identite.setType(Identite.TypeIdentite.FORMATEUR);
        identiteDao.insert(identite, "nomForm", Calendar.getInstance());
        
        // Find the inserted respForm
        RespFormation[] result = identiteDao.findRespForm(null, null, null, null);
        boolean found = false;
        for(RespFormation respForm : result) {
            if(respForm.getId() == identite.getId()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
