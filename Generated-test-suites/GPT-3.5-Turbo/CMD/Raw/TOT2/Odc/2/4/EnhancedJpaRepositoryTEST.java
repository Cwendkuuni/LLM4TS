import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Long> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Long> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        DataSource dataSource = mock(DataSource.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        assertEquals(JdbcTemplate.class, repository.getJdbcTemplate().getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Long> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertEquals(NamedParameterJdbcTemplate.class, repository.getNamedParameterJdbcTemplate().getClass());
    }
}