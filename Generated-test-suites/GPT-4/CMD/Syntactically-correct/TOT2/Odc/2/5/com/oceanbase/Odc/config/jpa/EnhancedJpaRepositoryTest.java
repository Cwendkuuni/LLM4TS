package com.oceanbase.odc.config.jpa;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest<T extends Serializable, ID> {

    private JpaEntityInformation<T, ?> entityInformation;
    private EntityManager entityManager;

    private EnhancedJpaRepository<T, ID> repository;

    @Before
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEntityManager() {
        assertThat(repository.getEntityManager(), is(entityManager));
    }

    @Test
    public void testOwnedEntityManagerNull() {
        repository = new EnhancedJpaRepository<>(entityInformation, null);
        assertNull(repository.getEntityManager());
    }

    @Test
    public void testJdbcTemplate() {
        // JdbcTemplate and NamedParameterJdbcTemplate have a complex relationship,
        // let's assumes that it just works and don't mock them.
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        // Same as above.
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}
