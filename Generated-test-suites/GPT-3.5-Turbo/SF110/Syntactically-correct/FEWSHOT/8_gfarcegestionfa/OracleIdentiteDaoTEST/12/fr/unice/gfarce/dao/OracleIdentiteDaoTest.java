import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import fr.unice.gfarce.dao.*;
import fr.unice.gfarce.identity.*;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;

public class OracleIdentiteDaoTest {
    @Test
    public void testInsert() {
        Identite identite = mock(Identite.class);
        Calendar dateLimite = Calendar.getInstance();

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.insert(identite, "nomForm", dateLimite);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
        EntityManager em = emf.createEntityManager();

        verify(em, times(1)).persist(identite);
        verify(em, times(1)).getTransaction();
        verify(em.getTransaction(), times(1)).begin();
        verify(em.getTransaction(), times(1)).commit();
        verify(em, times(1)).close();
        verify(emf, times(1)).close();
    }

    @Test
    public void testUpdate() {
        Identite identite = mock(Identite.class);

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.update(identite);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
        EntityManager em = emf.createEntityManager();

        verify(em, times(1)).merge(identite);
        verify(em, times(1)).getTransaction();
        verify(em.getTransaction(), times(1)).begin();
        verify(em.getTransaction(), times(1)).commit();
        verify(em, times(1)).close();
        verify(emf, times(1)).close();
    }

    @Test
    public void testDelete() {
        Identite identite = mock(Identite.class);

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.delete(identite);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
        EntityManager em = emf.createEntityManager();

        verify(em, times(1)).remove(identite);
        verify(em, times(1)).getTransaction();
        verify(em.getTransaction(), times(1)).begin();
        verify(em.getTransaction(), times(1)).commit();
        verify(em, times(1)).close();
        verify(emf, times(1)).close();
    }

    @Test
    public void testFind() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@example.com";
        Identite.TypeIdentite type = Identite.TypeIdentite.CANDIDAT;

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.find(nom, prenom, sex, email, type);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
        EntityManager em = emf.createEntityManager();

        verify(em, times(1)).createQuery(anyString());
        verify(em, times(1)).setParameter(anyString(), any());
        verify(em, times(1)).getResultList();
        verify(em.getTransaction(), times(1)).begin();
        verify(em.getTransaction(), times(1)).commit();
        verify(em, times(1)).close();
        verify(emf, times(1)).close();
    }

    @Test
    public void testFindCandidat() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@example.com";

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.findCandidat(nom, prenom, sex, email);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
        EntityManager em = emf.createEntityManager();

        verify(em, times(1)).createQuery(anyString());
        verify(em, times(1)).setParameter(anyString(), any());
        verify(em, times(1)).getResultList();
        verify(em.getTransaction(), times(1)).begin();
        verify(em.getTransaction(), times(1)).commit();
        verify(em, times(1)).close();
        verify(emf, times(1)).close();
    }

    @Test
    public void testFindRespForm() {
        String nom = "John";
        String prenom = "Doe";
        String sex = "Male";
        String email = "john.doe@example.com";

        OracleIdentiteDao dao = new OracleIdentiteDao();
        dao.findRespForm(nom, prenom, sex, email);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(dao.persistUnitName);
        EntityManager em = emf.createEntityManager();

        verify(em, times(1)).createQuery(anyString());
        verify(em, times(1)).setParameter(anyString(), any());
        verify(em, times(1)).getResultList();
        verify(em.getTransaction(), times(1)).begin();
        verify(em.getTransaction(), times(1)).commit();
        verify(em, times(1)).close();
        verify(emf, times(1)).close();
    }
}