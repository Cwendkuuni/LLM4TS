package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.ConnectionHolder;
import org.hibernate.jdbc.WorkExecutor;
import org.hibernate.jdbc.WorkManager;
import org.hibernate.jdbc.WorkStatement;
import org.hibernate.jdbc.WorkStatementImpl;
import org.hibernate.jpa.AbstractEntityManagerFactoryImpl;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.test.AbstractEntityManagerForJUnit4GivenRawJdbcAccessTest;
import org.hibernate.jpa.test.SimpleJpaTestHelper;
import org.hibernate.tool.hqlnames.SimpleHqlNameTranslator;
import org.hibernate.tool.hqlnames.StandardHqlNameTranslator;
import org.hibernate.tool.hqlnames.internal.StandardHqlNameTranslatorImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository.GetDataSourceMethod;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest extends AbstractEntityManagerForJUnit4GivenRawJdbcAccessTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManagerFactoryBuilderImpl factory;

    @Mock
    private AbstractEntityManagerFactoryImpl emf;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;

    @Mock
    private ConnectionHolder connectionHolder;

    @Mock
    private WorkExecutor workExecutor;

    @Mock
    private WorkManager workManager;

    @Mock
    private WorkStatement workStatement;

    @Mock
    private WorkStatementImpl workStatementImpl;

    @Mock
    private SimpleHqlNameTranslator nameTranslator;

    @Mock
    private StandardHqlNameTranslatorImpl standardNameTranslator;

    private EnhancedJpaRepository<Object, Serializable> repository;

    @BeforeClass
    public static void setupBeforeClass() {
        StandardHqlNameTranslatorImpl standardNameTranslator = new StandardHqlNameTranslatorImpl();
        SimpleHqlNameTranslator nameTranslator = new SimpleHqlNameTranslator(standardNameTranslator);
        SimpleJpaTestHelper.configure(nameTranslator);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(factory.getDataSource()).thenReturn(mock(DataSource.class));
        when(emf.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(mock(DataSource.class));
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Before
    public void before() {
        super.before();
        repository.afterPropertiesSet();
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        ArgumentCaptor<EntityManager> entityManagerArgumentCaptor = ArgumentCaptor.forClass(EntityManager.class);
        GetDataSourceMethod getDataSourceMethod = new GetDataSourceMethod() {
            @Override
            public DataSource getDataSource(EntityManager entityManager) {
                DataSource dataSource = mock(DataSource.class);
                verify(entityManager).unwrap(DataSource.class);
                verify(entityManager).unwrap(SessionFactoryImpl.class);
                verify(sessionFactory).getServiceRegistry().getService(ConnectionProvider.class);
                verify(connectionProvider).getDataSource();
                return dataSource;
            }
        };
        DataSource expectedDataSource = mock(DataSource.class);
        when(entityManager.unwrap(DataSource.class)).thenReturn(expectedDataSource);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        DataSource actualDataSource = repository.getDataSource(entityManager);
        assertEquals(expectedDataSource, actualDataSource);
        verify(entityManager).unwrap(DataSource.class);
        verify(entityManager).unwrap(SessionFactoryImpl.class);
        verify(sessionFactory).getServiceRegistry();
        verify(connectionProvider).getDataSource();
    }

    @After
    public void tearDown() {
        super.after();
    }
}