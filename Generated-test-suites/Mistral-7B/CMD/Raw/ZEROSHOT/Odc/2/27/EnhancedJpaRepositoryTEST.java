import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformationImpl;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityId;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<TestEntity, TestEntityId> testEntityInformation;

    @Mock
    private DataSource dataSource;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, TestEntityId> enhancedJpaRepository;

    @Before
    public void setup() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(mock(ConnectionProvider.class));
        when(sessionFactory.getServiceRegistry().getService(DataSource.class)).thenReturn(dataSource);
        when(testEntityInformation.getEntityClass()).thenReturn(TestEntity.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(testEntityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
        assertEquals(TestEntity.class, enhancedJpaRepository.getDomainClass());
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
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