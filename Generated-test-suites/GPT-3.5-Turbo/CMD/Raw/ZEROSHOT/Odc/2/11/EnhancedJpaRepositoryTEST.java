import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManager;

    @Before
    public void setup() {
        JpaEntityInformation<TestEntity, ?> entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertNotNull(result);
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);

        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        ConnectionProvider connectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);

        DataSource expectedDataSource = ((DatasourceConnectionProviderImpl) connectionProvider).getDataSource();

        assertEquals(expectedDataSource, dataSource);
    }

    private static class TestEntity {

    }

}