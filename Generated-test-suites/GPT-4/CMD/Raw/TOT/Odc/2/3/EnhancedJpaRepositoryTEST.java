import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation entityInformation;

    private EnhancedJpaRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertTrue(repository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDataSource_Exception() {
        when(entityManager.getEntityManagerFactory())
                .thenThrow(new RuntimeException("Mock Exception"));

        DataSource dataSource = repository.getDataSource(entityManager);

        assertNull(dataSource);
    }
}