import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EnhancedJpaRepositoryTest {

    EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    EntityManager entityManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<>(new JpaEntityInformation<>(TestEntity.class), entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }

}