
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

}

