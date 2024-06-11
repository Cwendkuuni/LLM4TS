import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager em = mock(EntityManager.class);
        JpaEntityInformation entityInfo = mock(JpaEntityInformation.class);
        EnhancedJpaRepository ejrp = new EnhancedJpaRepository(entityInfo, em);

        assertEquals(em, ejrp.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager em = mock(EntityManager.class);
        JpaEntityInformation entityInfo = mock(JpaEntityInformation.class);
        EnhancedJpaRepository ejrp = new EnhancedJpaRepository(entityInfo, em);

        assertTrue(ejrp.getJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager em = mock(EntityManager.class);
        JpaEntityInformation entityInfo = mock(JpaEntityInformation.class);
        EnhancedJpaRepository ejrp = new EnhancedJpaRepository(entityInfo, em);

        assertTrue(ejrp.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    //Edge cases and exceptions should be added once the actual functionality of these methods is known.
    //For instance, the init of EnhancedJpaRepository object might fail if any of the mocks fail
    @Test(expected = RuntimeException.class)
    public void testGetDataSource_Exception() {
        EntityManager em = mock(EntityManager.class);
        when(em.getEntityManagerFactory()).thenThrow(new RuntimeException());
        JpaEntityInformation entityInfo = mock(JpaEntityInformation.class);
        EnhancedJpaRepository ejrp = new EnhancedJpaRepository(entityInfo, em);
        ejrp.getJdbcTemplate();
    }

}