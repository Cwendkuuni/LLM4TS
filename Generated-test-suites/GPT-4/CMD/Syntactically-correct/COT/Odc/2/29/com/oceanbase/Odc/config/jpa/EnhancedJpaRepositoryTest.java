package com.oceanbase.odc.config.jpa;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    /** Mock objects */
    @Mock
    private JpaEntityInformation mockEntityInformation;
    @Mock
    private EntityManager mockEntityManager;
    @Mock
    private DataSource mockDataSource;
    @Mock
    private JdbcTemplate mockJdbcTemplate;
    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;

    /** Instantiate the class under test with the mockeds */
    private EnhancedJpaRepository classUnderTest;


    @Before
    public final void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_enhancedJpaRepository_constructor() {
        classUnderTest = new EnhancedJpaRepository(mockEntityInformation, mockEntityManager);
        assertNotNull(classUnderTest);
    }

    @Test
    public void test_getEntityManager() {
        classUnderTest = new EnhancedJpaRepository(mockEntityInformation, mockEntityManager);
        assertSame(mockEntityManager, classUnderTest.getEntityManager());
    }

    @Test
    public void test_getJdbcTemplate() {
        classUnderTest = new EnhancedJpaRepository(mockEntityInformation, mockEntityManager);
        assertSame(mockJdbcTemplate, classUnderTest.getJdbcTemplate());
    }

    @Test
    public void test_getNamedParameterJdbcTemplate() {
        classUnderTest = new EnhancedJpaRepository(mockEntityInformation, mockEntityManager);
        assertSame(mockNamedParameterJdbcTemplate, classUnderTest.getNamedParameterJdbcTemplate());
    }
}