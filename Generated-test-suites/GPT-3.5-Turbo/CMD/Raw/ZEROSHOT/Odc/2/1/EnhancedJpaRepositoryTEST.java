import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private JpaEntityInformation entityInformation;
    private DataSource dataSource;
    private EnhancedJpaRepository<Object, Long> repository;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        dataSource = mock(DataSource.class);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = repository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        repository = spy(repository);
        doReturn(namedParameterJdbcTemplate).when(repository).getNamedParameterJdbcTemplate();

        JdbcTemplate result = repository.getJdbcTemplate();
        assertEquals(jdbcTemplate, result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        DataSource result = repository.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }
}