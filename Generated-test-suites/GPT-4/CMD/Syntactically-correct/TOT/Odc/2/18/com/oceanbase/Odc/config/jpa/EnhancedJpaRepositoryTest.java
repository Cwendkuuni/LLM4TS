package com.oceanbase.odc.config.jpa;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

    @Test
    public void enhancedJpaRepositoryConstructorTest() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl ds = mock(DatasourceConnectionProviderImpl.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(ds);
        
        EnhancedJpaRepository<Object, Serializable> ejp = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);

        assertNotNull(ejp.getEntityManager());
        assertNotNull(ejp.getJdbcTemplate());
        assertNotNull(ejp.getNamedParameterJdbcTemplate());
    }
}