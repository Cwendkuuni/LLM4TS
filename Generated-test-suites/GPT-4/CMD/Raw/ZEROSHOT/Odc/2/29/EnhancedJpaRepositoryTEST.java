
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javax.persistence.EntityManager;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.Serializable;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    EntityManager entityManager = mock(EntityManager.class);
    JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);

    EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);

    @Test
    public void testEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = mock(DataSource.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(namedParameterJdbcTemplate.getJdbcOperations());
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}
