import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Object> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<Object, Object> enhancedJpaRepository;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(entityManager.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(jdbcTemplate.getDataSource().getConnection());
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
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

    // Add edge cases and exception handling tests for each method below

    // getDataSource(EntityManager)
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullEntityManager() {
        EnhancedJpaRepository<Object, Object> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.getDataSource(entityManager);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }
}