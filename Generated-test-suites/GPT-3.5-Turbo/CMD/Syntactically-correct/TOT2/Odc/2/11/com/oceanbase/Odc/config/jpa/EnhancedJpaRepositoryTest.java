import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        JpaEntityInformation<TestEntity, Long> entityInformation = new JpaEntityInformation<TestEntity, Long>() {

            @Override
            public Long getId(TestEntity entity) {
                return null;
            }

            @Override
            public Class<Long> getIdType() {
                return Long.class;
            }

            @Override
            public Class<TestEntity> getJavaType() {
                return TestEntity.class;
            }

            @Override
            public boolean isNew(TestEntity entity) {
                return true;
            }
        };

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager actualEntityManager = repository.getEntityManager();
        assertNotNull(actualEntityManager);
        assertEquals(entityManager, actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullDataSource() {
        EntityManager nullEntityManager = null;
        EnhancedJpaRepository<TestEntity, Long> nullDataSourceRepository = new EnhancedJpaRepository<>(null, nullEntityManager);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullEntityInformation() {
        EntityManager nullEntityManager = null;
        JpaEntityInformation<TestEntity, Long> nullEntityInformation = null;
        EnhancedJpaRepository<TestEntity, Long> nullEntityInfoRepository = new EnhancedJpaRepository<>(nullEntityInformation, nullEntityManager);
    }

    public static class TestEntity implements Persistable<Long> {

        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return id == null;
        }
    }
}