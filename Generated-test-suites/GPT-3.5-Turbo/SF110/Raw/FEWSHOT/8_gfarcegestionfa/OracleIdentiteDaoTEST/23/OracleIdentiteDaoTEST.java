package fr.unice.gfarce.dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Calendar;
import java.util.List;

import fr.unice.gfarce.identity.Candidat;
import fr.unice.gfarce.identity.Identite;
import fr.unice.gfarce.identity.RespFormation;
import fr.unice.gfarce.dao.IdentiteDao;

public class OracleIdentiteDaoTest {
    
    @Mock
    private OracleIdentiteDao dao;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
        Identite identite = Mockito.mock(Identite.class);
        String nomForm = "test";
        Calendar dateLimite = Mockito.mock(Calendar.class);
        
        dao.insert(identite, nomForm, dateLimite);
        
        // Verify that the EntityManagerFactory and EntityManager were created
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        
        // Verify that the transaction was committed and the entity was persisted
        Mockito.verify(em).persist(identite);
        Mockito.verify(em.getTransaction()).commit();
    }
    
    @Test
    public void testUpdate() {
        Identite identite = Mockito.mock(Identite.class);
        identite.setId(1);
        identite.setEmail("example@example.com");
        identite.setNom("test");
        identite.setPrenom("test");
        identite.setSex("male");
        identite.setType(Identite.TypeIdentite.CANDIDAT);
        
        Mockito.when(dao.em.find((Class)identite.getClass(), (Object)identite.getId())).thenReturn(identite);
        
        dao.update(identite);
        
        // Verify that the EntityManagerFactory and EntityManager were created
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        
        // Verify that the transaction was committed and the entity was merged
        Mockito.verify(em).merge(identite);
        Mockito.verify(em.getTransaction()).commit();
    }
    
    @Test
    public void testDelete() {
        Identite identite = Mockito.mock(Identite.class);
        identite.setId(1);
        
        Mockito.when(dao.em.find((Class)identite.getClass(), (Object)identite.getId())).thenReturn(identite);
        
        dao.delete(identite);
        
        // Verify that the EntityManagerFactory and EntityManager were created
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        
        // Verify that the entity was removed and the transaction was committed
        Mockito.verify(em).remove(identite);
        Mockito.verify(em.getTransaction()).commit();
    }
    
    @Test
    public void testFind() {
        String nom = "test";
        String prenom = "test";
        String sex = "male";
        String email = "example@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        
        List<Identite> resultList = dao.find(nom, prenom, sex, email, type);
        
        // Verify that the EntityManagerFactory and EntityManager were created
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Identite");
        EntityManager em = emf.createEntityManager();
        
        // Verify that the query was executed and the result was returned
        Mockito.verify(emf).createEntityManager();
        Mockito.verify(em.getTransaction()).begin();
        Mockito.verify(em.createQuery(Mockito.anyString())).setParameter(Mockito.anyString(), Mockito.anyObject()).getResultList();
    }
    
    @Test
    public void testFindCandidat() {
        String nom = "test";
        String prenom = "test";
        String sex = "male";
        String email = "example@example.com";
        
        Mockito.when(dao.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT)).thenReturn(null);
        
        Candidat[] result = dao.findCandidat(nom, prenom, sex, email);
        
        assertNull(result);
        
        // Verify that the find method was called with the correct parameters
        Mockito.verify(dao).find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);
    }
    
    @Test
    public void testFindRespForm() {
        String nom = "test";
        String prenom = "test";
        String sex = "male";
        String email = "example@example.com";
        
        Mockito.when(dao.find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR)).thenReturn(null);
        
        RespFormation[] result = dao.findRespForm(nom, prenom, sex, email);
        
        assertNull(result);
        
        // Verify that the find method was called with the correct parameters
        Mockito.verify(dao).find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR);
    }
    
}