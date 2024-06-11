import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManagerMock;
    @Mock
    private JpaEntityInformation entityInformationMock;
    @Mock
    private SessionFactoryImpl sessionFactoryMock;
    @Mock
    private DatasourceConnectionProviderImpl connectionProviderMock;
    @Mock
    private ConnectionProvider connectionProvider;
    @Mock
    private DataSource dataSourceMock;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;
    @Mock
    private JdbcTemplate jdbcTemplateMock;

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        when(entityManagerMock.getEntityManagerFactory()).thenReturn(sessionFactoryMock);
        when(sessionFactoryMock.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryMock);
        when(sessionFactoryMock.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProviderMock);
        when(connectionProviderMock.getDataSource()).thenReturn(dataSourceMock);

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformationMock, entityManagerMock);
        enhancedJpaRepository.namedParameterJdbcTemplate = namedParameterJdbcTemplateMock;
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManagerMock, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        when(namedParameterJdbcTemplateMock.getJdbcOperations()).thenReturn(jdbcTemplateMock);
        assertEquals(jdbcTemplateMock, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplateMock, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceErrorHandling() {
        when(sessionFactoryMock.getServiceRegistry().getService(ConnectionProvider.class))
                .thenThrow(new SQLException("Error retrieving data source"));
        enhancedJpaRepository.getDataSource(entityManagerMock);
    }

    // Additional test cases can be added here for edge cases and error scenarios

}