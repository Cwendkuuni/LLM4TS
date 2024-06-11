import static org.junit.Assert.*;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<String, Long> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository<String, Long> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository<String, Long> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    // Edge case: Ensure getDataSource() returns a valid DataSource
    @Test
    public void testGetDataSource() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<String, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getDataSource(entityManager));
    }

    // Error scenario: Passing null EntityManager to constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEntityManager() {
        new EnhancedJpaRepository<>(null, null);
    }
}