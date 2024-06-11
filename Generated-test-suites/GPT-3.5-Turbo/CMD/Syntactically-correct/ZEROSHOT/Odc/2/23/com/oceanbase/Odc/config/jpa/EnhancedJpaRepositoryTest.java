import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;
    private JpaEntityInformation<TestEntity, Long> entityInformation;
    private EntityManager entityManager;
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private class TestEntity {
        // Define TestEntity properties
    }

    @Before
    public void setup() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        dataSource = mock(DataSource.class);
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManager);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(mock(SessionFactoryImpl.class)
                .getServiceRegistry()
                .getService(ConnectionProvider.class)).thenReturn(mock(DatasourceConnectionProviderImpl.class));
        when(mock(DatasourceConnectionProviderImpl.class).getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        repository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

}