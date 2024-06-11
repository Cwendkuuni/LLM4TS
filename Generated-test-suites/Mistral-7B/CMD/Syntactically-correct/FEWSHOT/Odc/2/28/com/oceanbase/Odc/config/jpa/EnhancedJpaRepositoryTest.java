package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupitermock.JUnitJupiterMockitoSupport;

@ContextConfiguration
public class EnhancedJpaRepositoryTest extends JUnitJupiterMockitoSupport {

    private LocalContainerEntityManagerFactoryBean emf;
    private SessionFactory sessionFactory;
    private JpaEntityInformation<Object, Long> entityInformation;
    private EntityManager entityManager;
    private EnhancedJpaRepository<Object, Long> jpaRepository;

    @Before
    public void setUp() throws Exception {
        emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPersistenceUnitName("testPersistenceUnit");
        emf.afterPropertiesSet();

        sessionFactory = emf.getObject();
        entityInformation = new JpaEntityInformation<>(emf.getBeanFactory().getBean(TestEntity.class), sessionFactory);
        entityManager = sessionFactory.createEntityManager();
        jpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testEnhancedJpaRepositoryConstruction() {
        assertNotNull(jpaRepository);
        assertNotNull(jpaRepository.getNamedParameterJdbcTemplate());
        assertNotNull(jpaRepository.getEntityManager());
        assertNotNull(jpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = jpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);
        assertSame(DataUtils.getDataSource(entityManager), dataSource);
    }

    @Test(expected = HibernateException.class)
    public void testGetDataSourceWithNullEntityManager() {
        EnhancedJpaRepository<Object, Long> jpaRepository = new EnhancedJpaRepository<>(entityInformation, null);
        jpaRepository.getDataSource(entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, jpaRepository.getEntityManager());
        Mockito.doThrow(EmptyResultDataAccessException.class).when(entityManager).find(Mockito.any(Class.class), Mockito.anyLong());
        assertNull(jpaRepository.findOne(1L));
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = jpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
        assertEquals(NamedParameterJdbcTemplate.class, jdbcTemplate.getClass().getSuperclass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = jpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
        assertEquals(NamedParameterJdbcTemplate.class, namedParameterJdbcTemplate.getClass());
    }

}