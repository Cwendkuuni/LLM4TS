import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        JpaEntityInformation<Object, Long> entityInformation = JpaEntityInformationSupport.getEntityInformation(Object.class, entityManager);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEntityManagerNotNull() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testJdbcTemplateNotNull() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplateNotNull() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManager);
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }
}