import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<>();
        int expectedMaxEvaluations = 1000;
        int actualMaxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(expectedMaxEvaluations, actualMaxEvaluations);
    }

    @Test
    public void testGetEvaluations_WhenNotCalledYet() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<>();
        int expectedEvaluations = 0;
        int actualEvaluations = optimizer.getEvaluations();
        Assert.assertEquals(expectedEvaluations, actualEvaluations);
    }

    @Test
    public void testGetEvaluations_AfterOptimize() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<>();
        // Call the optimize method here
        int expectedEvaluations = 1;
        int actualEvaluations = optimizer.getEvaluations();
        Assert.assertEquals(expectedEvaluations, actualEvaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<>();
        ConvergenceChecker<PAIR> expectedChecker = new ConvergenceChecker<>();
        ConvergenceChecker<PAIR> actualChecker = optimizer.getConvergenceChecker();
        Assert.assertEquals(expectedChecker, actualChecker);
    }
}