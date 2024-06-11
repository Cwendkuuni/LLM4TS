package com.oceanbase.odc.config.jpa;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Long> repository;

    @Before
    public void setUp() {
        EnhancedJpaRepository<Object, Long> mockedRepository = mock(EnhancedJpaRepository.class);
        this.repository = mockedRepository;
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

}