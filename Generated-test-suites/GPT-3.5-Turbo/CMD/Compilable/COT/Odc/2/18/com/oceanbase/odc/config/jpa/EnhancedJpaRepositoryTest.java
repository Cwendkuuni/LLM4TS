package com.oceanbase.odc.config.jpa;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}
