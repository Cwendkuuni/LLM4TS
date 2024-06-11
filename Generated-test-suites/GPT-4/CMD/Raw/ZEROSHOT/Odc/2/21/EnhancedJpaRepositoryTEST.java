
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    private EntityManager entityManager;
    private JpaEntityInformation<T, ID> entityInformation;
    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;
    private DataSource dataSource;


    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);

        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        dataSource = mock(DataSource.class);
        builder.bind("java:comp/env/jdbc/datasource", dataSource);
        builder.activate();

        enhancedJpaRepository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);
    }

    @Test
    public void getEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void namedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void getJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }
}
