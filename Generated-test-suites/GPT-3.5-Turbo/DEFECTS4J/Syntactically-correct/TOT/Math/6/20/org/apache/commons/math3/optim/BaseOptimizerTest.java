import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        // Typical use case: Check if the maximal number of function evaluations is returned correctly
        BaseOptimizer<Integer> optimizer = new BaseOptimizerImpl<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(1000, maxEvaluations);

        // Edge case: Check if the maximal number of function evaluations is 0
        optimizer = new BaseOptimizerImpl<>(0);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);

        // Error scenario: Check if a negative maximal number of function evaluations is handled correctly
        optimizer = new BaseOptimizerImpl<>(-100);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        // Typical use case: Check if the number of evaluations is returned correctly after optimization
        BaseOptimizer<Integer> optimizer = new BaseOptimizerImpl<>();
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize();
        assertEquals(10, optimizer.getEvaluations());

        // Edge case: Check if the number of evaluations is 0 when optimize is not called
        optimizer = new BaseOptimizerImpl<>();
        assertEquals(0, optimizer.getEvaluations());

        // Error scenario: Check if the number of evaluations remains 0 after negative number of iterations
        optimizer = new BaseOptimizerImpl<>();
        optimizer.setIterations(-10);
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Typical use case: Check if the convergence checker is returned correctly
        BaseOptimizer<Integer> optimizer = new BaseOptimizerImpl<>();
        ConvergenceChecker<Integer> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);

        // Edge case: Check if the convergence checker is null when set to null
        optimizer = new BaseOptimizerImpl<>();
        optimizer.setConvergenceChecker(null);
        convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }
}