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
        ConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }

    private class BaseOptimizerImpl implements BaseOptimizer {

        @Override
        public int getMaxEvaluations() {
            return 0;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker getConvergenceChecker() {
            return null;
        }
    }
}