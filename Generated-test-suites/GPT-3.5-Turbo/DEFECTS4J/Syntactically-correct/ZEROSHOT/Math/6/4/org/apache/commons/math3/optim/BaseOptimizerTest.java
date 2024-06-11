import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        Assert.assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        Assert.assertNotNull(optimizer.getConvergenceChecker());
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