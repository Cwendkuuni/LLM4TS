package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> testEntityRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setup() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        testEntityRepository = context.getBean(EnhancedJpaRepository.class);
        jdbcTemplate = context.getBean(JdbcTemplate.class);
        namedParameterJdbcTemplate = context.getBean(NamedParameterJdbcTemplate.class);
    }

    @Test
    public void testInitialization() {
        assertNotNull(testEntityRepository);
        assertNotNull(entityManager);
        assertNotNull(jdbcTemplate);
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(testEntityRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(testEntityRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(testEntityRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(testEntityRepository.getDataSource(testEntityRepository.getEntityManager()));
    }
}

class TestEntity {
    private Long id;
    private String name;

    public TestEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}