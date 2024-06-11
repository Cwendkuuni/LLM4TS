import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(JdbcTemplate.class, enhancedJpaRepository.getJdbcTemplate().getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(NamedParameterJdbcTemplate.class, enhancedJpaRepository.getNamedParameterJdbcTemplate().getClass());
    }
}