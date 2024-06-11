import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    private BaseOptimizer<PointValuePair> optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new DummyBaseOptimizer();
        function = new DummyUnivariateFunction();
    }

    /**
     * Test method for getMaxEvaluations.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
        Assert.assertEquals(MaxEval.UNLIMITED, maxEvaluations);
    }

    /**
     * Test method for getEvaluations.
     */
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);
    }

    /**
     * Test method for getConvergenceChecker.
     */
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Test method for optimize.
     */
    @Test
    public void testOptimize() {
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(0.0, 1.0),
                new InitialGuess(0.5), null);
        Assert.assertNotNull(solution);
        Assert.assertNotNull(solution.getValue());
        Assert.assertEquals(0.0, solution.getValue(), 0.01);
    }
}

/**
 * Dummy implementation of BaseOptimizer interface.
 */
class DummyBaseOptimizer implements BaseOptimizer<PointValuePair> {

    @Override
    public int getMaxEvaluations() {
        return MaxEval.UNLIMITED;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return new DummyConvergenceChecker();
    }
}

/**
 * Dummy implementation of ConvergenceChecker interface.
 */
class DummyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

}

/**
 * Dummy implementation of UnivariateFunction interface.
 */
class DummyUnivariateFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2);
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }
}