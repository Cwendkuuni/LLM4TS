package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = createTestEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = createTestEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        EntityManager retrievedEntityManager = repository.getEntityManager();
        assertEquals(entityManager, retrievedEntityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = createTestEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = createTestEntityManager();
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    private EntityManager createTestEntityManager() {
        EntityManagerFactoryBuilderImpl builder = new EntityManagerFactoryBuilderImpl(null, null);
        return builder.build().createEntityManager();
    }
}