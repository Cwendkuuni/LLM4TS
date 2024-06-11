import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        // Typical use case: The maximal number of function evaluations is set to 100
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);

        // Edge case: The maximal number of function evaluations is set to 0
        optimizer = new BaseOptimizer<>(0);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        // Typical use case: The number of evaluations is 10
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        int evaluations = optimizer.getEvaluations();
        assertEquals(10, evaluations);

        // Edge case: The number of evaluations is 0
        optimizer = new BaseOptimizer<>(0);
        evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        // Typical use case: The convergence checker is set to a StandardConvergenceChecker
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        ConvergenceChecker<Integer> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof StandardConvergenceChecker);

        // Error scenario: The convergence checker is not set (null)
        optimizer = new BaseOptimizer<>(10, null);
        convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }

}