package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Mock
    private DataSource dataSource;

    @Mock
    private SessionFactory sessionFactory;

    @Before
    public void setup() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(dataSource));
        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(javax.sql.Connection.class));
    }

    @After
    public void tearDown() {
        enhancedJpaRepository = null;
        entityManager = null;
        entityInformation = null;
        dataSource = null;
        sessionFactory = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
        assertNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}