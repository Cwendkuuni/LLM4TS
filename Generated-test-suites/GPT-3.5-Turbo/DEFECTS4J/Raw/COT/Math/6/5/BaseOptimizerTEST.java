
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        int maxEvaluations = optimizer.getMaxEvaluations();
        // Verify that the returned value is non-negative
        assertTrue(maxEvaluations >= 0);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        int evaluations = optimizer.getEvaluations();
        // Verify that the returned value is non-negative
        assertTrue(evaluations >= 0);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        // Verify that the returned value is not null
        assertNotNull(convergenceChecker);
    }
}
