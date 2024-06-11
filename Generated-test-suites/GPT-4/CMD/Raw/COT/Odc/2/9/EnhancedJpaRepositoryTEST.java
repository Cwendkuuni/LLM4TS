package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock private JpaEntityInformation<T, ?> entityInformation;
    @Mock private EntityManager entityManager;
    private EnhancedJpaRepository<T, ID> repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void constructor_and_getEntityManager_test() {
        verify(entityInformation).getJavaType();
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplate_test() {
        // Get the jdbcTemplate and assert it's not null
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void getNamedParameterJdbcTemplate_test() {
        // Get the namedParameterJdbcTemplate and assert it's not null
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}