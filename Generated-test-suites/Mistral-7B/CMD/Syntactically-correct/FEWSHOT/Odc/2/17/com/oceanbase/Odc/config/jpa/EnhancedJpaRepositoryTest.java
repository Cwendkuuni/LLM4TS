package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = EnhancedJpaRepository.class)
@ContextConfiguration(classes = { JpaRepositoryConfiguration.class })
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;
    private EnhancedJpaRepository<Person, Long> repository;

    @Before
    public void setup() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.oceanbase.odc.entity");
        em.afterPropertiesSet();

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        DatabasePopulatorUtils.execute(new ResourceDatabasePopulator(), dataSource);
        this.repository = new EnhancedJpaRepository<Person, Long>(new JpaEntityInformation<Person, Long>(Person.class, getClass().getClassLoader()), em.getObject()) {
            @Override
            public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
                return (NamedParameterJdbcTemplate) super.getJdbcTemplate();
            }
        };
    }

    @After
    public void teardown() {
        DatabasePopulatorUtils.execute(new ResourceDatabasePopulator(), dataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = repository.getDataSource(repository.getEntityManager());
        assertNotNull(ds);
        ConnectionProvider connectionProvider = ds.getConnectionProvider();
        assertNotNull(connectionProvider);
        ConnectionProvider hibernateConnectionProvider = ((DatasourceConnectionProviderImpl) connectionProvider);
        assertEquals(ds, hibernateConnectionProvider.getDataSource());
    }
}

class Person {
    private Long id;
    private String name;

    public Person() {
    }

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}