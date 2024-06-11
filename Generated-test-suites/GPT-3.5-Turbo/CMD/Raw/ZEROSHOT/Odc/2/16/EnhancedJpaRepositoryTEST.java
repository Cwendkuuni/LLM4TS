import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private JpaEntityInformation<TestEntity, Long> entityInformation;
    private EntityManager entityManager;

    @Before
    public void setup() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        DataSource dataSource = mock(DataSource.class);
        entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry()
                .getService(ConnectionProvider.class)).thenReturn(mock(DatasourceConnectionProviderImpl.class));
        when(((DatasourceConnectionProviderImpl) entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry().getService(ConnectionProvider.class)).getDataSource()).thenReturn(dataSource);

        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertEquals(dataSource, jdbcTemplate.getDataSource());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(enhancedJpaRepository.getNamedParameterJdbcTemplate(), enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    private static class TestEntity {}

}