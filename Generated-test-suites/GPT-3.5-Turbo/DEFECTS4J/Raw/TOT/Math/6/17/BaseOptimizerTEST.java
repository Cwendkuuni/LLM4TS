import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizerImpl<>();

        // Typical use case
        Assert.assertEquals(100, optimizer.getMaxEvaluations());

        // Edge case
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());

        // Error scenario
        Assert.assertNotEquals(-1, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizerImpl<>();

        // Typical use case when optimize method has not been called yet
        Assert.assertEquals(0, optimizer.getEvaluations());

        // Error scenario
        Assert.assertNotEquals(10, optimizer.getEvaluations());

        // Typical use case after calling optimize method
        optimizer.optimize();
        Assert.assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizerImpl<>();

        // Typical use case
        Assert.assertNotNull(optimizer.getConvergenceChecker());

        // Error scenario
        Assert.assertNull(optimizer.getConvergenceChecker());
    }
}