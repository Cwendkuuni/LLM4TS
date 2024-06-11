import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetDataSource() {
        EntityManager mockEntityManager = new MockEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(new JpaEntityInformation<>(Object.class), mockEntityManager);

        DataSource dataSource = repository.getDataSource(mockEntityManager);

        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager mockEntityManager = new MockEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(new JpaEntityInformation<>(Object.class), mockEntityManager);

        EntityManager entityManager = repository.getEntityManager();

        assertNotNull(entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager mockEntityManager = new MockEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(new JpaEntityInformation<>(Object.class), mockEntityManager);

        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();

        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager mockEntityManager = new MockEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(new JpaEntityInformation<>(Object.class), mockEntityManager);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        assertNotNull(namedParameterJdbcTemplate);
    }
}

class MockEntityManager implements EntityManager {
    // Mock methods for EntityManager
}