import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;
    private EntityManager entityManager;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

}