import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> jpaRepository = new EnhancedJpaRepository<>(null, entityManager);
        assertEquals(entityManager, jpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> jpaRepository = new EnhancedJpaRepository<>(null, entityManager);
        assertEquals(JdbcTemplate.class, jpaRepository.getJdbcTemplate().getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> jpaRepository = new EnhancedJpaRepository<>(null, entityManager);
        assertEquals(NamedParameterJdbcTemplate.class, jpaRepository.getNamedParameterJdbcTemplate().getClass());
    }

    // Edge Cases and Exception Handling scenarios:
    // - Test when the EntityManager is null

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEntityManager() {
        new EnhancedJpaRepository<>(null, null);
    }

    // - Test when the EntityManager's EntityManagerFactory does not unwrap to SessionFactoryImpl

    @Test(expected = ClassCastException.class)
    public void testGetDataSourceWithInvalidEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Long> jpaRepository = new EnhancedJpaRepository<>(null, entityManager);
        jpaRepository.getDataSource(entityManager);
    }

    // - Test when the ConnectionProvider in SessionFactoryImpl is not DatasourceConnectionProviderImpl

    @Test(expected = ClassCastException.class)
    public void testGetDataSourceWithInvalidConnectionProvider() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);

        EnhancedJpaRepository<Object, Long> jpaRepository = new EnhancedJpaRepository<>(null, entityManager);
        jpaRepository.getDataSource(entityManager);
    }

}