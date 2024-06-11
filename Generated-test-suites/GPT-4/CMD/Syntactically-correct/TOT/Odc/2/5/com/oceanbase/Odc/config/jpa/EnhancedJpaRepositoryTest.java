package com.oceanbase.odc.config.jpa;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {
    
    private EnhancedJpaRepository<Entity, Long> enhancedJpaRepository;
    private EntityManager entityManager;
    private DataSource dataSource;
     
    @Before
    public void setUp() {
        // given
        JpaEntityInformation<Entity, ?> entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
        dataSource = Mockito.mock(DataSource.class);

        // when
        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(Mockito.any())).thenReturn(null); // As we're unable to mock the static `unwrap` method, we return null

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }
     
    @Test
    public void getEntityManagerTest() {
        // then
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }
     
    @Test
    public void getJdbcTemplateTest() {
        // then
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }
     
    @Test
    public void getNamedParameterJdbcTemplateTest() {
        // then
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}