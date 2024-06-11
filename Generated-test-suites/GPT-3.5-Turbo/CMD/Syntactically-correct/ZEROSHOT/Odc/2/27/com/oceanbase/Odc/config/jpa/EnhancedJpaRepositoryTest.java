import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Serializable> entityInformation = mock(JpaEntityInformation.class);
        when(entityInformation.getIdType()).thenReturn(Long.class);

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);

        ConnectionProvider connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);

        DataSource expectedDataSource = mock(DataSource.class);
        when(((DatasourceConnectionProviderImpl) connectionProvider).getDataSource()).thenReturn(expectedDataSource);

        DataSource actualDataSource = enhancedJpaRepository.getDataSource(entityManager);

        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager expectedEntityManager = mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository = new EnhancedJpaRepository<>(null, expectedEntityManager);

        EntityManager actualEntityManager = enhancedJpaRepository.getEntityManager();

        assertEquals(expectedEntityManager, actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        JdbcTemplate expectedJdbcTemplate = mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(expectedJdbcTemplate);

        EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository = new EnhancedJpaRepository<>(null, null);
        Mockito.doReturn(namedParameterJdbcTemplate).when(enhancedJpaRepository).getNamedParameterJdbcTemplate();

        JdbcTemplate actualJdbcTemplate = enhancedJpaRepository.getJdbcTemplate();

        assertEquals(expectedJdbcTemplate, actualJdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate expectedNamedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository = new EnhancedJpaRepository<>(null, null);

        NamedParameterJdbcTemplate actualNamedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();

        assertEquals(expectedNamedParameterJdbcTemplate, actualNamedParameterJdbcTemplate);
    }

    private static class TestEntity {
    }

}