package fr.unice.gfarce.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import fr.unice.gfarce.identity.*;

public class OracleIdentiteDaoTest {
    private OracleIdentiteDao dao;
    
    @Mock
    private EntityManager em;
    
    @Mock
    private EntityManagerFactory emf;
    
    @Mock
    private EntityTransaction tx;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dao = new OracleIdentiteDao();
    }

    @Test
    public void testInsert() {
        Identite identite = mock(Identite.class);
        String nomForm = "test";
        Calendar dateLimite = Calendar.getInstance();
        
        when(emf.createEntityManager()).thenReturn(em);
        when(Persistence.createEntityManagerFactory(dao.persistUnitName)).thenReturn(emf);
        when(em.getTransaction()).thenReturn(tx);
        
        dao.insert(identite, nomForm, dateLimite);
        
        verify(emf).createEntityManager();
        verify(Persistence).createEntityManagerFactory(dao.persistUnitName);
        verify(em).getTransaction();
        verify(em).persist(identite);
        verify(tx).begin();
        verify(tx).commit();
        verify(em).close();
        verify(emf).close();
    }

    @Test
    public void testUpdate() {
        Identite identite = mock(Identite.class);
        
        when(emf.createEntityManager()).thenReturn(em);
        when(Persistence.createEntityManagerFactory(dao.persistUnitName)).thenReturn(emf);
        when(em.getTransaction()).thenReturn(tx);
        when(em.find(Identite.class, identite.getId())).thenReturn(identite);
        
        dao.update(identite);
        
        verify(emf).createEntityManager();
        verify(Persistence).createEntityManagerFactory(dao.persistUnitName);
        verify(em).getTransaction();
        verify(em).find(Identite.class, identite.getId());
        verify(identite).getEmail();
        verify(identite).getNom();
        verify(identite).getPrenom();
        verify(identite).getSex();
        verify(identite).getType();
        verify(em).merge(identite);
        verify(tx).begin();
        verify(tx).commit();
        verify(em).close();
        verify(emf).close();
    }

    @Test
    public void testDelete() {
        Identite identite = mock(Identite.class);
        
        when(emf.createEntityManager()).thenReturn(em);
        when(Persistence.createEntityManagerFactory(dao.persistUnitName)).thenReturn(emf);
        when(em.getTransaction()).thenReturn(tx);
        when(em.find(Identite.class, identite.getId())).thenReturn(identite);
        
        dao.delete(identite);
        
        verify(emf).createEntityManager();
        verify(Persistence).createEntityManagerFactory(dao.persistUnitName);
        verify(em).getTransaction();
        verify(em).find(Identite.class, identite.getId());
        verify(em).remove(identite);
        verify(tx).begin();
        verify(tx).commit();
        verify(em).close();
        verify(emf).close();
    }
    
    @Test
    public void testFind() {
        String nom = "test";
        String prenom = "test";
        String sex = "test";
        String email = "test";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;
        List<Identite> identites = new ArrayList<>();
        
        when(emf.createEntityManager()).thenReturn(em);
        when(Persistence.createEntityManagerFactory(dao.persistUnitName)).thenReturn(emf);
        when(em.getTransaction()).thenReturn(tx);
        when(identite.getNom()).thenReturn(nom);
        when(identite.getPrenom()).thenReturn(prenom);
        when(identite.getSex()).thenReturn(sex);
        when(identite.getType()).thenReturn(type);
        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(identites);
        
        List<Identite> result = dao.find(nom, prenom, sex, email, type);

        assertEquals(identites, result);
        verify(emf).createEntityManager();
        verify(Persistence).createEntityManagerFactory(dao.persistUnitName);
        verify(em).getTransaction();
        verify(em).createQuery(anyString());
        verify(query).setParameter(anyString(), any());
        verify(query).getResultList();
        verify(tx).begin();
        verify(tx).commit();
        verify(em).close();
        verify(emf).close();
    }

    @Test
    public void testFindCandidat() {
        String nom = "test";
        String prenom = "test";
        String sex = "test";
        String email = "test";
        List<Identite> identites = new ArrayList<>();
        Candidat[] result = new Candidat[identites.size()];
        
        when(emf.createEntityManager()).thenReturn(em);
        when(Persistence.createEntityManagerFactory(dao.persistUnitName)).thenReturn(emf);
        when(em.getTransaction()).thenReturn(tx);
        when(dao.find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT)).thenReturn(identites);
        when(identite.getNom()).thenReturn(nom);
        when(identite.getPrenom()).thenReturn(prenom);
        when(identite.getSex()).thenReturn(sex);
        when(identite.getEmail()).thenReturn(email);
        
        Candidat[] res = dao.findCandidat(nom, prenom, sex, email);

        assertEquals(result, res);
        verify(emf).createEntityManager();
        verify(Persistence).createEntityManagerFactory(dao.persistUnitName);
        verify(em).getTransaction();
        verify(dao).find(nom, prenom, sex, email, Identite.TypeIdentite.CANDIDAT);
        verify(em).close();
        verify(emf).close();
    }

    @Test
    public void testFindRespForm() {
        String nom = "test";
        String prenom = "test";
        String sex = "test";
        String email = "test";
        List<Identite> identites = new ArrayList<>();
        RespFormation[] result = new RespFormation[identites.size()];
        
        when(emf.createEntityManager()).thenReturn(em);
        when(Persistence.createEntityManagerFactory(dao.persistUnitName)).thenReturn(emf);
        when(em.getTransaction()).thenReturn(tx);
        when(dao.find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR)).thenReturn(identites);
        when(identite.getNom()).thenReturn(nom);
        when(identite.getPrenom()).thenReturn(prenom);
        when(identite.getSex()).thenReturn(sex);
        when(identite.getEmail()).thenReturn(email);
        
        RespFormation[] res = dao.findRespForm(nom, prenom, sex, email);

        assertEquals(result, res);
        verify(emf).createEntityManager();
        verify(Persistence).createEntityManagerFactory(dao.persistUnitName);
        verify(em).getTransaction();
        verify(dao).find(nom, prenom, sex, email, Identite.TypeIdentite.FORMATEUR);
        verify(em).close();
        verify(emf).close();
    }
}