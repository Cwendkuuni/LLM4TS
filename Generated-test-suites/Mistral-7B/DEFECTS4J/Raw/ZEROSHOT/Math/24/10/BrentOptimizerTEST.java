import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.IntervalRealValuedVectorOptimizerInitializer;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test file for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function for Brent optimizer.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 10 * x + 9;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 10;
        }
    }

    /**
     * Test for {@link BrentOptimizer} with default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        RealPointValuePair result = optimizer.optimize(new TestFunction(), new IntervalRealValuedVectorOptimizerInitializer(0, 20));
        Assert.assertEquals(2.0, result.getPoint(), 0.001);
        Assert.assertEquals(-3.13155648, result.getValue(), 0.001);
    }

    /**
     * Test for {@link BrentOptimizer} with custom threshold values.
     */
    @Test
    public void testCustomThresholdValues() {
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-8;
        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        RealPointValuePair result = optimizer.optimize(new TestFunction(), new IntervalRealValuedVectorOptimizerInitializer(0, 20));
        Assert.assertEquals(2.0, result.getPoint(), 0.001);
        Assert.assertEquals(-3.13155648, result.getValue(), 0.001);
    }

    /**
     * Test for {@link BrentOptimizer} with custom threshold values and
     * custom convergence checker.
     */
    @Test
    public void testCustomThresholdValuesAndChecker() {
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-8;
        int maxIterations = 50;
        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        optimizer.setMaxIterations(maxIterations);
        optimizer.setConvergenceChecker((iter, previous, current) -> maxIterations <= iter);
        RealPointValuePair result = optimizer.optimize(new TestFunction(), new IntervalRealValuedVectorOptimizerInitializer(0, 20));
        Assert.assertEquals(2.0, result.getPoint(), 0.001);
        Assert.assertEquals(-3.13155648, result.getValue(), 0.001);
        Assert.assertEquals(maxIterations, optimizer.getIterations());
    }
}