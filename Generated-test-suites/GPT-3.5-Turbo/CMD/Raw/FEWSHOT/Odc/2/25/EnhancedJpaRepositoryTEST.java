package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        JpaEntityInformation<TestEntity, Long> entityInformation = new TestEntityInformation();
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertTrue(enhancedJpaRepository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertTrue(enhancedJpaRepository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    private class TestEntity {}

    private class TestEntityInformation implements JpaEntityInformation<TestEntity, Long> {
        @Override
        public Long getId(TestEntity entity) {
            return null;
        }

        @Override
        public Class<TestEntity> getJavaType() {
            return TestEntity.class;
        }

        @Override
        public Class<Long> getIdType() {
            return Long.class;
        }

        @Override
        public String getEntityName() {
            return null;
        }

        @Override
        public String getMappedTableName() {
            return null;
        }

        @Override
        public void generateId() {}

        @Override
        public boolean isNew(TestEntity entity) {
            return false;
        }

        @Override
        public boolean isIdGenerated() {
            return false;
        }
    }
}