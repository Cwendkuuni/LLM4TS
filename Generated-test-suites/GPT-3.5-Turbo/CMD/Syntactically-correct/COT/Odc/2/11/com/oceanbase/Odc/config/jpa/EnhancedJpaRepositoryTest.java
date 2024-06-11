import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        assertEquals(entityManager, repository.getEntityManager());
    }
    
    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        assertNotNull(repository.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    // Test for getDataSource() method's edge case where EntityManager is null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullEntityManager() {
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, null);
        
        repository.getDataSource(null);
    }

    // Test for getDataSource() method's edge case where EntityManager does not have SessionFactoryImpl
    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithNonSessionFactoryImplEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        repository.getDataSource(entityManager);
    }

    // Test for getDataSource() method's edge case where ConnectionProvider is null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullConnectionProvider() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        repository.getDataSource(entityManager);
    }

    // Test for getDataSource() method's edge case where DataSource is null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        repository.getDataSource(entityManager);
    }
}