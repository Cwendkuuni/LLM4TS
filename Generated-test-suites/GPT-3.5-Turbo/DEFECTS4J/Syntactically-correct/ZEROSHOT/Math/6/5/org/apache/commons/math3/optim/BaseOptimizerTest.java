import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
    
    private class BaseOptimizerImpl implements BaseOptimizer {
        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return 0;
        }

        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return new ConvergenceChecker<PAIR>() {
                public boolean converged(int iteration, PAIR previous, PAIR current) {
                    return false;
                }
            };
        }
    }
}