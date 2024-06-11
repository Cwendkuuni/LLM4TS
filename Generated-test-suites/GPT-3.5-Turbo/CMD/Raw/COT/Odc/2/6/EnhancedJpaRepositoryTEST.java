import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
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
        assertNotNull(enhancedJpaRepository.getDataSource(enhancedJpaRepository.getEntityManager()));
    }

}