
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl cp = mock(DatasourceConnectionProviderImpl.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
        
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void getDataSourceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DataSource result = Whitebox.invokeMethod(repository, "getDataSource", entityManager);
        assertNotNull(result);
    }

    @Test
    public void getEntityManagerTest() {
        EntityManager result = repository.getEntityManager();
        assertNotEquals(null, result);
    }

    @Test
    public void getJdbcTemplateTest() {
        JdbcTemplate result = repository.getJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }
}
