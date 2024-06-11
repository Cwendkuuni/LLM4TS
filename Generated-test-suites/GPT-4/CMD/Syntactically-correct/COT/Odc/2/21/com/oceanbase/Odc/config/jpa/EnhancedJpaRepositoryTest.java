package com.oceanbase.odc.config.jpa;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertNotNull(repository);
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class));
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEntityInformationNull() {
        new EnhancedJpaRepository<>(null, mock(EntityManager.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEntityManagerNull() {
        new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), null);
    }

    @Test(expected = PersistenceException.class)
    public void testConstructorCannotUnwrapSessionFactory() {
        EntityManager em = mock(EntityManager.class);
        when(em.getEntityManagerFactory().unwrap(any())).thenThrow(PersistenceException.class);
        new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), em);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorCannotGetConnectionProvider() {
        EntityManager em = mock(EntityManager.class);
        when(em.getEntityManagerFactory().unwrap(any())).thenReturn(mock(SessionFactoryImpl.class));
        when(sf.getServiceRegistry().getService(any())).thenThrow(IllegalStateException.class);
        new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), em);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorCannotGetDataSource() {
        EntityManager em = mock(EntityManager.class);
        when(em.getEntityManagerFactory().unwrap(any())).thenReturn(mock(SessionFactoryImpl.class));
        when(sf.getServiceRegistry().getService(any())).thenReturn(mock(ConnectionProvider.class));
        when(cp.getDataSource()).thenReturn(null);
        new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), em);
    }
}