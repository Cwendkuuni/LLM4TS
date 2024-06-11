import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    // Test method for getMaxEvaluations
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    // Test method for getEvaluations
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    // Test method for getConvergenceChecker
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    // Test class implementing BaseOptimizer
    private class BaseOptimizerImpl implements BaseOptimizer<PAIR> {

        @Override
        public int getMaxEvaluations() {
            return 0;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return null;
        }
    }
}