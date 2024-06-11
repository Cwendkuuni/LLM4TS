package com.oceanbase.odc.config.jpa;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testEntityManager(){
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testJdbcTemplate(){
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
         
        when(entityManager.getEntityManagerFactory()).thenReturn(jdbcTemplate);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testNamedParameterJdbcTemplate(){
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
         
        when(entityManager.getEntityManagerFactory()).thenReturn(namedParameterJdbcTemplate);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        
        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource(){
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);
        
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        
        assertSame(dataSource, repository.getDataSource(entityManager));
    }

}
