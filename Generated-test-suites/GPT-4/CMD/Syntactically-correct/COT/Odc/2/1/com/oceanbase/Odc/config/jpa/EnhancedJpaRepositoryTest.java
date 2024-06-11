package com.oceanbase.odc.config.jpa;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructorAndGetterMethods() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        JpaEntityInformation entityInformationMock = mock(JpaEntityInformation.class);
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformationMock, entityManagerMock);
        assertEquals(entityManagerMock, repo.getEntityManager());
    }
  
    @Test
    public void testJdbcTemplate() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        JpaEntityInformation entityInformationMock = mock(JpaEntityInformation.class);
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformationMock, entityManagerMock);
        assertTrue(repo.getJdbcTemplate() instanceof JdbcTemplate);
    }
    
    @Test
    public void testNamedParameterJdbcTemplate() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        JpaEntityInformation entityInformationMock = mock(JpaEntityInformation.class);
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformationMock, entityManagerMock);
        assertTrue(repo.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    //Edge case when EntityManager is null
    @Test(expected=NullPointerException.class)
    public void testIncorrectEntityManager() {
        EntityManager entityManagerMock = null;
        JpaEntityInformation entityInformationMock = mock(JpaEntityInformation.class);
        new EnhancedJpaRepository(entityInformationMock, entityManagerMock);
    }

    //Edge case when JpaEntityInformation is null
    @Test(expected=NullPointerException.class)
    public void testIncorrectEntityInformation() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        JpaEntityInformation entityInformationMock = null;
        new EnhancedJpaRepository(entityInformationMock, entityManagerMock);
    }
}
