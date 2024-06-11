
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.internal.SessionFactoryImpl;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {
    private JpaEntityInformation entityInformation;
    private EntityManager entityManager;
    private DataSource dataSource;
    private EntityManagerFactory emFactory;
    private SessionFactoryImpl sessionFactory;

    @Before
    public void setup() {
         entityManager = Mockito.mock(EntityManager.class);
         entityInformation = Mockito.mock(JpaEntityInformation.class);
         dataSource = Mockito.mock(DataSource.class);
         emFactory = Mockito.mock(EntityManagerFactory.class);
         sessionFactory = Mockito.mock(SessionFactoryImpl.class);
         Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(emFactory);
         Mockito.when(emFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        EntityManager result = repository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        JdbcTemplate result = repository.getJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }
}
