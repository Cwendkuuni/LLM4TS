import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
        ConvergenceChecker<Object> checker = optimizer.getConvergenceChecker();
        // Assert that the checker is not null
        assertNotNull(checker);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}