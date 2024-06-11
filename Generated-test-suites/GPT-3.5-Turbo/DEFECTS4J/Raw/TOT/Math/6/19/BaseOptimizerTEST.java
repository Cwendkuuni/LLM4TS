import org.junit.Assert;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerStub();
        Assert.assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerStub();
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerStub();
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    // Edge case: Test when getMaxEvaluations returns a negative value
    @Test
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer optimizer = new BaseOptimizerNegativeStub();
        Assert.assertEquals(-1, optimizer.getMaxEvaluations());
    }

    // Error scenario: Test when getEvaluations is called before calling optimize method
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        BaseOptimizer optimizer = new BaseOptimizerStub();
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    // Typical use case: Test when getConvergenceChecker returns a valid checker
    @Test
    public void testGetConvergenceCheckerValid() {
        BaseOptimizer optimizer = new BaseOptimizerStub();
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(DefaultConvergenceChecker.class, optimizer.getConvergenceChecker().getClass());
    }
}

// Stub class for BaseOptimizer
class BaseOptimizerStub implements BaseOptimizer<Object> {
    @Override
    public int getMaxEvaluations() {
        return 0;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<Object> getConvergenceChecker() {
        return new DefaultConvergenceChecker<>();
    }
}

// Stub class for BaseOptimizer with negative max evaluations
class BaseOptimizerNegativeStub implements BaseOptimizer<Object> {
    @Override
    public int getMaxEvaluations() {
        return -1;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<Object> getConvergenceChecker() {
        return new DefaultConvergenceChecker<>();
    }
}