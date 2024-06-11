import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    // Edge case: negative number of evaluations
    @Test
    public void testGetEvaluationsNegative() {
        BaseOptimizer optimizer = new BaseOptimizer();
        // Setting negative evaluations
        // This is an edge case that should not be allowed, so the behavior is undefined
        // Expecting an IllegalArgumentException 
        try {
            optimizer.setEvaluations(-5);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    // Error scenario: null convergence checker
    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer optimizer = new BaseOptimizer();
        // Setting null convergence checker
        // This is an error scenario that should not be allowed
        // Expecting a NullPointerException
        try {
            optimizer.setConvergenceChecker(null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Success
        }
    }
}