import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    @Mock
    private JpaEntityInformation entityInformation;
    @Mock
    private EntityManager entityManager;
    private EnhancedJpaRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void constructorTest() throws Exception {
        assertNotNull(repository);
    }

    @Test
    public void getDataSourceTest() throws Exception {
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void getEntityManagerTest() throws Exception {
        EntityManager entityManager = repository.getEntityManager();
        assertEquals(this.entityManager, entityManager);
    }

    @Test
    public void getJdbcTemplateTest() throws Exception {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() throws Exception {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }
}