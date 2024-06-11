package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @Mock
    private JpaEntityInformation<T, ?> entityInformation;

    @Mock
    private SessionFactoryImpl sessionFactory;
    
    @Mock
    private EntityManagerFactory entityManagerFactory;

    private EnhancedJpaRepository<T, ID> repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(DatasourceConnectionProviderImpl());
        when(DatasourceConnectionProviderImpl().getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEntityManager(){
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
        assertEquals(this.entityManager, entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }
}
