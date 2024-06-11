package com.oceanbase.odc.config.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    @Test
    public void enhancedJpaRepositoryConstructorAndMethodsTest() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        SessionFactoryImpl sessionFactoryImpl = mock(SessionFactoryImpl.class);
        ConnectionProvider connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryImpl);
        when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(((DatasourceConnectionProviderImpl)connectionProvider).getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);

        assertSame(entityManager, repository.getEntityManager());
        assertNotNull(repository.getJdbcTemplate());
        assertNotNull(repository.getNamedParameterJdbcTemplate());

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        JdbcTemplate jdbcTemplateExpected = (JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations();
        JdbcTemplate jdbcTemplateActual = (JdbcTemplate) repository.getNamedParameterJdbcTemplate().getJdbcOperations();

        assertSame(jdbcTemplateExpected, jdbcTemplateActual);
    }
}