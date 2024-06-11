
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        enhancedJpaRepository = new EnhancedJpaRepository<>(null, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertNotNull(result);
        Mockito.verify(entityManager).getEntityManagerFactory();
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }
}
