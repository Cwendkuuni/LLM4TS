import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {
    
    @Test(expected = NullPointerException.class)
    public void construct_NullEntityInformation_NullPointerException() {
        new EnhancedJpaRepository<Object, Serializable>(null, mock(EntityManager.class));
    }

    @Test(expected = NullPointerException.class)
    public void construct_NullEntityManager_NullPointerException() {
        new EnhancedJpaRepository<Object, Serializable>(mock(JpaEntityInformation.class), null);
    }

    @Test(expected = NullPointerException.class)
    public void construct_NullEntityManager_NullDataSource_NullPointerException() {
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        EntityManager em = mock(EntityManager.class);
        
        when(em.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(null);
        
        new EnhancedJpaRepository<Object, Serializable>(mock(JpaEntityInformation.class), em);
    }

    @Test
    public void accessors_AccessDataSourceDataSource_GettersReturnConsistentValues() {
        EntityManager em = mock(EntityManager.class);
        JpaEntityInformation ei = mock(JpaEntityInformation.class);
        
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(ei, em);
        
        assertSame("getEntityManager should return consistent value", em, repo.getEntityManager());
        assertNotNull("getJdbcTemplate should not return null", repo.getJdbcTemplate());
        assertNotNull("getNamedParameterJdbcTemplate should not return null", repo.getNamedParameterJdbcTemplate());
    }
}