package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;
    private JpaEntityInformation<T, ?> entityInformation;
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetDataSource() {
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        DatasourceConnectionProviderImpl connectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);

        DataSource result = this.enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(result);
        assertEquals(dataSource, result);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = this.enhancedJpaRepository.getEntityManager();
        assertNotNull(result);
        assertEquals(entityManager, result);
    }
    
    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(this.enhancedJpaRepository.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(this.enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}

