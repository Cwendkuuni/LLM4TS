import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setUp() {
        EntityManager entityManager = mock(EntityManager.class);
        repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(repository.getEntityManager(), repository.entityManager);
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