import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        // Create instance of BaseOptimizer
        BaseOptimizer optimizer = new BaseOptimizerImpl();

        // Test the functionality of getMaxEvaluations method
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create instance of BaseOptimizer
        BaseOptimizer optimizer = new BaseOptimizerImpl();

        // Test the functionality of getEvaluations method
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create instance of BaseOptimizer
        BaseOptimizer optimizer = new BaseOptimizerImpl();

        // Test the functionality of getConvergenceChecker method
        assertNull(optimizer.getConvergenceChecker());
    }
}