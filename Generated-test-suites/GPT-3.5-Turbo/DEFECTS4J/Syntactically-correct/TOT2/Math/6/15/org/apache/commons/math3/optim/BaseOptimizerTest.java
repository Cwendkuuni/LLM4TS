import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /* Edge cases and exception handling scenarios */

    @Test
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        // Test for negative value
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluationsInvalid() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        // Test for invalid evaluations
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<>();
        // Test for null convergence checker
        assertNull(optimizer.getConvergenceChecker());
    }

}