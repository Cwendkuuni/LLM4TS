import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {
    
    EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setUp() {
        EntityManager entityManager = // create entity manager instance
        repository = new EnhancedJpaRepository<>(new TestEntityInformation(), entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test (expected = NullPointerException.class)
    public void testNullEntityManagerConstructor() {
        new EnhancedJpaRepository<>(new TestEntityInformation(), null);
    }

    @Test
    public void testDataSourceNotNull() {
        assertNotNull(repository.getDataSource(repository.getEntityManager()));
    }

    @Test
    public void testJdbcTemplateNotNull() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplateNotNull() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}