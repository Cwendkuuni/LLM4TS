
package com.oceanbase.odc.config.jpa;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock 
    private DataSource dataSource;
    
    @Mock
    private EntityManagerFactory entityManagerFactory;
   
    private EnhancedJpaRepository enhancedJpaRepository;

    @Before
    public void setUp() {
       when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
       enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = enhancedJpaRepository.getEntityManager();
        assertThat(entityManager, CoreMatchers.is(this.entityManager));
    }

    @Test 
    public void testGetJdbcTemplate() {
        Object jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertThat(jdbcTemplate, CoreMatchers.notNullValue());
    }

    @Test 
    public void testGetNamedParameterJdbcTemplate() {
       Object namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
       assertThat(namedParameterJdbcTemplate, CoreMatchers.notNullValue());
    }

}
