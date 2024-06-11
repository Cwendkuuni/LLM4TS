import static org.mockito.Mockito.*;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Long> entityInformation = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actualEntityManager = enhancedJpaRepository.getEntityManager();
        assertEquals(entityManager, actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

}