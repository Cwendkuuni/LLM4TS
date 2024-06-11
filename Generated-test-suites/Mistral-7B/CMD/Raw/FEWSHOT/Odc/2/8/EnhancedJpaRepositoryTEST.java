package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private SessionFactory sessionFactory;
    private AnnotationConfigApplicationContext applicationContext;
    private EnhancedJpaRepository<DummyEntity, Long> repository;

    @Before
    public void setup() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        applicationContext = new AnnotationConfigApplicationContext("com.oceanbase.odc.config.jpa");
        repository = applicationContext.getBean(EnhancedJpaRepository.class);
    }

    @After
    public void tearDown() {
        sessionFactory.close();
        applicationContext.close();
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation entityInformation = new JpaEntityInformationImpl<>(DummyEntity.class, null);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(repository.getEntityManager(), sessionFactory.getCurrentSession());
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

    @Test
    public void testNamedParameterOperation() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Long id = (Long) session.save(new DummyEntity("test"));
        session.getTransaction().commit();

        String sql = "SELECT name FROM DummyEntity WHERE id = :id";
        String result = repository.getNamedParameterJdbcTemplate().queryForObject(sql, new Parameters(new Parameter("id", id))).toString();
        assertEquals("test", result);

        session.beginTransaction();
        repository.getNamedParameterJdbcTemplate().update(sql, new Parameters(new Parameter("id", id)));
        session.getTransaction().commit();

        result = repository.findOne(id).getName();
        assertEquals("test", result);
    }

    static class DummyEntity {
        private String name;

        public DummyEntity(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class Parameters {
        private Map<String, Object> parameters;

        public Parameters(Parameter... parameters) {
            this.parameters = new LinkedHashMap<>();
            for (Parameter p : parameters) {
                this.parameters.put(p.getName(), p.getValue());
            }
        }
    }

    static class Parameter {
        private String name;
        private Object value;

        public Parameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }
}