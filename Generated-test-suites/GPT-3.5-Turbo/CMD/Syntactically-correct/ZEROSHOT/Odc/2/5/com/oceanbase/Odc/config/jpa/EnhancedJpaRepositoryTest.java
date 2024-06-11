import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private JpaEntityInformation<TestEntity, ?> entityInformationMock;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;

    @Mock
    private JdbcTemplate jdbcTemplateMock;

    @Before
    public void setUp() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformationMock, entityManagerMock);
        Mockito.when(entityManagerMock.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManagerMock.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));
        Mockito.when(((DatasourceConnectionProviderImpl) entityManagerMock.getEntityManagerFactory()
                .unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .getDataSource()).thenReturn(Mockito.mock(DataSource.class));
        Mockito.when(namedParameterJdbcTemplateMock.getJdbcOperations()).thenReturn(jdbcTemplateMock);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(jdbcTemplateMock, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplateMock, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}