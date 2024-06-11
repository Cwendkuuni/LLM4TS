import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        // Initialize mocks
        given(testEntityInformation.getJavaType())
                .willReturn(TestEntity.class);
        given(testEntityInformation.getEntityClass())
                .willReturn(TestEntity.class);
        given(testEntityInformation.getEntityName())
                .willReturn("TestEntity");
        given(entityManager.unwrap(SessionFactoryImpl.class))
                .willReturn(new SessionFactoryImpl());
        given(entityManager.unwrap(EntityManager.class))
                .willReturn(entityManager);
        given(entityManager.getEntityManagerFactory())
                .willReturn(mock(org.hibernate.SessionFactory.class));
        given(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .willReturn(mock(SessionFactoryImpl.class));
        given(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry().getService(ConnectionProvider.class))
                .willReturn(new DatasourceConnectionProviderImpl(dataSource));
    }

    @Test
    public void testGetEntityManager() {
        given(entityManager.unwrap(SessionFactoryImpl.class))
                .willReturn(new SessionFactoryImpl());

        enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(testEntityInformation, entityManager);

        EntityManager actualEntityManager = enhancedJpaRepository.getEntityManager();

        verify(entityManager).unwrap(EntityManager.class);
        then(actualEntityManager).isSameAs(entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        given(entityManager.unwrap(SessionFactoryImpl.class))
                .willReturn(new SessionFactoryImpl());

        enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(testEntityInformation, entityManager);

        JdbcTemplate actualJdbcTemplate = enhancedJpaRepository.getJdbcTemplate();

        ArgumentCaptor<DataSource> dataSourceArgumentCaptor = ArgumentCaptor.forClass(DataSource.class);

        verify(namedParameterJdbcTemplate).setDataSource(dataSourceArgumentCaptor.capture());
        DataSource capturedDataSource = dataSourceArgumentCaptor.getValue();
        then(capturedDataSource).isSameAs(dataSource);

        verify(namedParameterJdbcTemplate).getJdbcOperations();
        then(actualJdbcTemplate).isSameAs(namedParameterJdbcTemplate.getJdbcOperations());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        given(entityManager.unwrap(SessionFactoryImpl.class))
                .willReturn(new SessionFactoryImpl());

        enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(testEntityInformation, entityManager);

        NamedParameterJdbcTemplate actualNamedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();

        verify(namedParameterJdbcTemplate).getJdbcOperations();
        then(actualNamedParameterJdbcTemplate).isSameAs(namedParameterJdbcTemplate);
    }
}