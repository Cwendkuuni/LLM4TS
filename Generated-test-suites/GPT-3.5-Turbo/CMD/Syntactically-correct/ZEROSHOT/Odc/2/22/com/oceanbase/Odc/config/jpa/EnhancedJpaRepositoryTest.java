import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
    private EntityManager entityManagerMock;
    private JpaEntityInformation<TestEntity, Long> entityInformationMock;

    @Before
    public void setup() {
        entityManagerMock = mock(EntityManager.class);
        entityInformationMock = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformationMock, entityManagerMock);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManagerMock, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        DataSource dataSourceMock = mock(DataSource.class);
        SessionFactoryImpl sessionFactoryMock = mock(SessionFactoryImpl.class);
        ConnectionProvider connectionProviderMock = mock(ConnectionProvider.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplateMock = mock(NamedParameterJdbcTemplate.class);
        JdbcTemplate jdbcTemplateMock = mock(JdbcTemplate.class);

        when(entityManagerMock.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryMock);
        when(sessionFactoryMock.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProviderMock);
        when(connectionProviderMock.getDataSource()).thenReturn(dataSourceMock);
        when(namedParameterJdbcTemplateMock.getJdbcOperations()).thenReturn(jdbcTemplateMock);

        assertEquals(jdbcTemplateMock, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

}