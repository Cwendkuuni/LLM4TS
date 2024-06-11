import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations); // Assuming the default value is 0
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations); // Assuming the default value is 0
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        ConvergenceChecker convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker); // Assuming the default value is null
    }
}