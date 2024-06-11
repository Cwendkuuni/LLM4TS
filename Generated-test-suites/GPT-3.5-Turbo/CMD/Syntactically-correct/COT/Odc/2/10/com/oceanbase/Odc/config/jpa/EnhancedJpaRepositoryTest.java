
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        repository = new EnhancedJpaRepository<>(new TestEntity(), Mockito.mock(EntityManager.class));
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    // Edge case: entityManager is null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullEntityManager() {
        repository.getDataSource(null);
    }
}
