import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<User, Long> repository;

    @Before
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        JpaEntityInformation<User, Long> entityInformation = new JpaEntityInformation<>();
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testDataSourceEdgeCase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test(expected = NullPointerException.class)
    public void testDataSourceNullException() {
        repository.getDataSource(null);
    }

}