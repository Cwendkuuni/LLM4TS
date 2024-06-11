import org.junit.Test;
import org.junit.Assert;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<>();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }
}