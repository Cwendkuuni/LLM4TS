package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.config.JpaConfig;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.JpaRepositoryTestBase;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

public class EnhancedJpaRepositoryTest extends JpaRepositoryTestBase<TestEntity, Long> {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        JpaConfig config = new JpaConfig();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(config.class);
        context.refresh();
        this.entityManagerFactory = context.getBean(org.springframework.data.jpa.LocalContainerEntityManagerFactoryBean.class).getObject();
        this.entityManager = this.entityManagerFactory.createEntityManager();
        this.repository = new EnhancedJpaRepository<>(new JpaEntityInformation<TestEntity, Long>(TestEntity.class, this.entityManagerFactory), this.entityManager);
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = this.repository.getDataSource(this.entityManager);
        assertNotNull(ds);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(this.entityManager, this.repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = this.repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = this.repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSourceFromEntityManager() {
        DataSource ds = this.repository.getDataSource(this.entityManager);
        DataSource ds2 = Mockito.spy(this.entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry()
                .getService(ConnectionProvider.class)
                .getDataSource());
        Mockito.when(ds2.getConnection()).thenReturn(Mockito.mock(javax.sql.ConnectionPoolDataSource.class).getConnection());
        assertEquals(ds, this.repository.getDataSource(this.entityManager));
        assertEquals(ds2, this.repository.getDataSource(this.entityManager));
    }

}