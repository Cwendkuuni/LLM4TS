package com.oceanbase.odc.config.jpa;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<String, Integer> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository<String, Integer> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository<String, Integer> repository = new EnhancedJpaRepository<>(null, null);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    // Edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testEnhancedJpaRepositoryConstructorNullException() {
        new EnhancedJpaRepository<>(null, null);
    }

}