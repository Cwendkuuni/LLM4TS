package com.oceanbase.odc.config.jpa;

import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
   
   @Test
   public void testGetEntityManager() {
       EntityManager entityManagerMock = Mockito.mock(EntityManager.class);
       JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
       EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManagerMock);

       EntityManager result = repo.getEntityManager();

       assertEquals(entityManagerMock, result);
   }

   @Test
   public void testNamedParameterJdbcTemplate() {
       EntityManager entityManagerMock = Mockito.mock(EntityManager.class);
       JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
       EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManagerMock);

       NamedParameterJdbcTemplate result = repo.getNamedParameterJdbcTemplate();

       assertNotNull(result);
   }

   @Test
   public void testGetJdbcTemplate() {
       EntityManager entityManagerMock = Mockito.mock(EntityManager.class);
       JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
       EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManagerMock);

       JdbcTemplate result = repo.getJdbcTemplate();

       assertNotNull(result);
   }

   @Test
   public void testGetDataSource() {
       EntityManager entityManagerMock = Mockito.mock(EntityManager.class);
       EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
       SessionFactoryImpl sfMock = Mockito.mock(SessionFactoryImpl.class);
       DatasourceConnectionProviderImpl dcpiMock = Mockito.mock(DatasourceConnectionProviderImpl.class);
       
       Mockito.when(entityManagerMock.getEntityManagerFactory()).thenReturn(emfMock);
       Mockito.when(emfMock.unwrap(SessionFactoryImpl.class)).thenReturn(sfMock);
       Mockito.when(sfMock.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dcpiMock);
       Mockito.when(dcpiMock.getDataSource()).thenReturn(Mockito.mock(DataSource.class));
       
       JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
       EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManagerMock);

       DataSource result = repo.testGetDataSource(entityManagerMock);

       assertNotNull(result);
   }
}
