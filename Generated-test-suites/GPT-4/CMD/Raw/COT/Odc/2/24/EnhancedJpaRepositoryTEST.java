
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    @Test
    public void testConstructor() {
        EntityManager entityManager = mock(EntityManager.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        SessionFactoryImpl sessionFactoryImpl = mock(SessionFactoryImpl.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        DatasourceConnectionProviderImpl datasourceConnectionProviderImpl = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryImpl);
        when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProviderImpl);
        when(datasourceConnectionProviderImpl.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(enhancedJpaRepository);
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        enhancedJpaRepository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        
        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}
