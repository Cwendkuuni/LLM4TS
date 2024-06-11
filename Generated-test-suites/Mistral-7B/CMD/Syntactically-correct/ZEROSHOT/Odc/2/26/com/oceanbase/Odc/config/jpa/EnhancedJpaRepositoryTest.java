import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepositoryImpl;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> testRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(entityManagerFactory.getObject()).thenReturn(entityManager);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(DatasourceConnectionProviderImpl.class)).thenReturn(new DatasourceConnectionProviderImpl(){
            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        });
        testRepository = new EnhancedJpaRepositoryImpl<>(testEntityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(testRepository);
        assertNotNull(testRepository.getEntityManager());
        assertNotNull(testRepository.getNamedParameterJdbcTemplate());
        assertNotNull(testRepository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(testRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(testRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(testRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(testRepository.getNamedParameterJdbcTemplate());
    }
}