package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        EntityManager entityManager = new MockEntityManager();
        JpaEntityInformation<TestEntity, Serializable> entityInformation = new MockJpaEntityInformation<>();
        EnhancedJpaRepository<TestEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = new MockEntityManager();
        JpaEntityInformation<TestEntity, Serializable> entityInformation = new MockJpaEntityInformation<>();
        EnhancedJpaRepository<TestEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = new MockEntityManager();
        JpaEntityInformation<TestEntity, Serializable> entityInformation = new MockJpaEntityInformation<>();
        EnhancedJpaRepository<TestEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = new MockEntityManager();
        JpaEntityInformation<TestEntity, Serializable> entityInformation = new MockJpaEntityInformation<>();
        EnhancedJpaRepository<TestEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    private static class TestEntity {}

    private static class MockEntityManager implements EntityManager {
        @Override
        public void clear() {}
        @Override
        public void close() {}
        @Override
        public boolean contains(Object entity) { return false; }
        // Implement other methods as needed for testing
    }

    private static class MockJpaEntityInformation<T, ID extends Serializable> implements JpaEntityInformation<T, ID> {
        @Override
        public Class<T> getJavaType() { return null; }
        @Override
        public String getEntityName() { return null; }
        @Override
        public Iterable<String> getIdAttributeNames() { return null; }
        @Override
        public Class<ID> getIdType() { return null; }
        // Implement other methods as needed for testing
    }
}