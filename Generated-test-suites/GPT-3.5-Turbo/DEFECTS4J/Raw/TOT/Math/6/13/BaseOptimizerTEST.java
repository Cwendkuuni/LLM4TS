import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {
    
    // Test for getMaxEvaluations method
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertNotNull(maxEvaluations);
    }
    
    // Test for getEvaluations method
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    // Test for getConvergenceChecker method
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<>();
        ConvergenceChecker<Integer> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
    
}