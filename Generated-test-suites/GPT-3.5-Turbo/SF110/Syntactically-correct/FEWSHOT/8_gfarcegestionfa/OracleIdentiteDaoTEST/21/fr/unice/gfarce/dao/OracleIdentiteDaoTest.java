package fr.unice.gfarce.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.identity.Identite.TypeIdentite;

public class OracleIdentiteDaoTest {
    
    OracleIdentiteDao dao;
    
    @Before
    public void setUp() {
        dao = new OracleIdentiteDao();
    }
    
    @Test
    public void testInsert() {
        Identite identite = new Identite();
        identite.setEmail("test@gmail.com");
        identite.setNom("Foo");
        identite.setPrenom("Bar");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        dao.insert(identite, "TestForm", null);
        assertNotNull(identite.getId());
    }
    
    @Test
    public void testUpdate() {
        Identite identite = new Identite();
        identite.setId(1);
        identite.setEmail("test@gmail.com");
        identite.setNom("Foo");
        identite.setPrenom("Bar");
        identite.setSex("M");
        identite.setType(TypeIdentite.CANDIDAT);
        dao.update(identite);
        assertEquals("test@gmail.com", identite.getEmail());
        assertEquals("Foo", identite.getNom());
        assertEquals("Bar", identite.getPrenom());
        assertEquals("M", identite.getSex());
        assertEquals(TypeIdentite.CANDIDAT, identite.getType());
    }
    
    @Test
    public void testDelete() {
        Identite identite = new Identite();
        identite.setId(1);
        dao.delete(identite);
        assertNull(identite.getId());
    }
    
    @Test
    public void testFind() {
        String nom = "Foo";
        String prenom = "Bar";
        String sex = "M";
        String email = "test@gmail.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        assertNotNull(dao.find(nom, prenom, sex, email, type));
    }
    
    @Test
    public void testFindCandidat() {
        String nom = "Foo";
        String prenom = "Bar";
        String sex = "M";
        String email = "test@gmail.com";
        assertNotNull(dao.findCandidat(nom, prenom, sex, email));
    }
    
    @Test
    public void testFindRespForm() {
        String nom = "Foo";
        String prenom = "Bar";
        String sex = "M";
        String email = "test@gmail.com";
        assertNotNull(dao.findRespForm(nom, prenom, sex, email));
    }
}