import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setUp() {
        repository = new EnhancedJpaRepository<>(new JpaEntityInformation<TestEntity, Long>() {
            @Override
            public Class<TestEntity> getJavaType() {
                return TestEntity.class;
            }

            @Override
            public Long getId(TestEntity entity) {
                return entity.getId();
            }

            @Override
            public Class<Long> getIdType() {
                return Long.class;
            }
        }, entityManagerMock);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        // Assuming entityManagerMock is an EntityManager mock
        assertNotNull(repository.getDataSource(entityManagerMock));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEntityInformation() {
        new EnhancedJpaRepository<>(null, entityManagerMock);
    }

    @Test
    public void testConstructorWithNullEntityManager() {
        EnhancedJpaRepository<TestEntity, Long> repo = new EnhancedJpaRepository<>(entityInfoMock, null);
        assertNotNull(repo.getEntityManager());
    }

    @Test
    public void testNoDataSourceOnGetDataSource() {
        // Assuming entityManagerMock is an EntityManager mock without a data source
        assertNull(repository.getDataSource(entityManagerMock));
    }
}