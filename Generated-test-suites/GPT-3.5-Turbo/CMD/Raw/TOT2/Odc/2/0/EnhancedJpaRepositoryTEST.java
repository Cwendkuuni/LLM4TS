import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;
    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setUp() {
        JpaEntityInformation<TestEntity, ?> entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = repository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);

        DataSource dataSource = mock(DataSource.class);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        DataSource result = repository.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }
}