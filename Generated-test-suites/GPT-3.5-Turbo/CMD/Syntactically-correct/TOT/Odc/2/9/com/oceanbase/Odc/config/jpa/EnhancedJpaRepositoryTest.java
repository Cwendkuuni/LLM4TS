import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Long> repository;
    private EntityManager entityManager;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        
        repository = new EnhancedJpaRepository<>(null, entityManager);
        when(repository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl dsProvider = mock(DatasourceConnectionProviderImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dsProvider);
        
        DataSource actualDataSource = repository.getDataSource(entityManager);
        
        assertNotNull(actualDataSource);
        assertEquals(dsProvider.getDataSource(), actualDataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actualEntityManager = repository.getEntityManager();
        
        assertEquals(entityManager, actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        
        JdbcTemplate actualJdbcTemplate = repository.getJdbcTemplate();
        
        assertEquals(jdbcTemplate, actualJdbcTemplate);
    }
}