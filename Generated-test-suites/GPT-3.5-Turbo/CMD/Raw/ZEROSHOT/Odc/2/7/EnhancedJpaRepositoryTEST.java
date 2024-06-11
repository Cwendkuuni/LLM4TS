import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<TestEntity, Long> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        assertEquals(entityManager, repo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<TestEntity, Long> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        JdbcTemplate jdbcTemplate = repo.getJdbcTemplate();
        assertEquals(JdbcTemplate.class, jdbcTemplate.getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<TestEntity, Long> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repo.getNamedParameterJdbcTemplate();
        assertEquals(NamedParameterJdbcTemplate.class, namedParameterJdbcTemplate.getClass());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl dataSourceProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        EntityManager entityManager = repo.getEntityManager();
        
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dataSourceProvider);
        when(dataSourceProvider.getDataSource()).thenReturn(dataSource);
        
        EnhancedJpaRepository<TestEntity, Long> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        DataSource result = repo.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }
}