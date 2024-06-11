package com.oceanbase.odc.config.jpa;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class EnhancedJpaRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation entityInformation;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

   @Test
   public void testEntityManagerNotNullAfterConstruction() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(rep.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplateNotNull() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(rep.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplateNotNull() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(rep.getNamedParameterJdbcTemplate());
    }

    // tests for edge cases, null EntityManager during construction
    @Test(expected = IllegalArgumentException.class)
    public void testEntityManagerNullDuringConstruction() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(entityInformation, null);
    }

    // tests for edge cases, null JpaEntityInformation during construction
    @Test(expected = IllegalArgumentException.class)
    public void testEntityInfoNullDuringConstruction() {
        EnhancedJpaRepository rep = new EnhancedJpaRepository(null, entityManager);
    }

}