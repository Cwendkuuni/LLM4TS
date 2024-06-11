package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.repository.support.PageableExecutionUtils.ExecutionInterceptor;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getJdbcTemplate());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        EntityManagerFactoryInfo entityManagerFactoryInfo = mock(EntityManagerFactoryInfo.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        SessionFactoryImpl sessionFactoryImpl = mock(SessionFactoryImpl.class);
        SessionFactoryImplementor sessionFactoryImplementor = mock(SessionFactoryImplementor.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        Mockito.when(entityManagerFactory).thenReturn(entityManagerFactoryInfo);
        Mockito.when(entityManagerFactoryInfo.unwrap(SessionFactory.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory).thenReturn(sessionFactoryImpl);
        Mockito.when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<?, ?> repository = new EnhancedJpaRepository<>(null, entityManager);

        DataSource result = ReflectionTestUtils.invokeMethod(repository, "getDataSource", entityManager);

        assertEquals(dataSource, result);
    }

}