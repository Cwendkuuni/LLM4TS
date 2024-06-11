import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {
    
    private BaseOptimizer<Object> optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Object>() {
        
            public int getMaxEvaluations() {
                return 0;
            }
            
            public int getEvaluations() {
                return 0;
            }
            
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }
    
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<Object> checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }

    // Edge cases and exception handling scenarios
    
    @Test
    public void testMaxEvaluationsNegative() {
        optimizer.getMaxEvaluations(); // to make sure method is called before this test
        try {
            optimizer.setMaxEvaluations(-1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // exception is expected
        }
    }
    
    @Test
    public void testEvaluationsNegative() {
        optimizer.getEvaluations(); // to make sure method is called before this test
        try {
            optimizer.setEvaluations(-1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // exception is expected
        }
    }
    
    @Test
    public void testConvergenceCheckerNull() {
        optimizer.getConvergenceChecker(); // to make sure method is called before this test
        try {
            optimizer.setConvergenceChecker(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // exception is expected
        }
    }
}