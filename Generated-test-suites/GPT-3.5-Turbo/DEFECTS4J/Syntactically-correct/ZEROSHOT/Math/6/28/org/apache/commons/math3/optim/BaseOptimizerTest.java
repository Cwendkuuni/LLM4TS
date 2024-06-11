import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        int maxEvaluations = optimizer.getMaxEvaluations();

        Assert.assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        int evaluations = optimizer.getEvaluations();

        Assert.assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();

        Assert.assertNotNull(convergenceChecker);
    }

    // Helper implementation of BaseOptimizer
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