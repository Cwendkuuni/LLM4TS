import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        // Initialize the repository with test data
        EntityManager entityManager = EntityManagerMock.createEntityManager();
        JpaEntityInformation<TestEntity, Long> entityInformation = new JpaEntityInformationImpl<>(TestEntity.class, entityManager);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
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
        // Edge Case: Test getting data source
        DataSource dataSource = repository.getDataSource(repository.getEntityManager());
        assertNotNull(dataSource);
        // Exception Handling: Test getting data source with null EntityManager
        dataSource = repository.getDataSource(null);
        assertEquals(null, dataSource);
    }
}