import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test cases for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    /**
     * Test constructor with relative and absolute threshold.
     */
    @Test
    public void testBrentOptimizerConstructorWithRelativEAndAbsoluteThreshold() {
        final double relativeThreshold = 0.01;
        final double absoluteThreshold = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        Assert.assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    /**
     * Test constructor with only relative threshold.
     */
    @Test
    public void testBrentOptimizerConstructorWithOnlyRelativeThreshold() {
        final double relativeThreshold = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold);
        Assert.assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(Double.NaN, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimizationWithSimpleFunction() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariatePointValuePair result = optimizer.optimize(function, new MaxEval(100));
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getX(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with a discontinuous function.
     */
    @Test
    public void testOptimizationWithDiscontinuousFunction() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sin(Math.PI / x);
                } else {
                    throw new ArithmeticException("Divide by zero");
                }
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariatePointValuePair result = optimizer.optimize(function, new MaxEval(100));
        Assert.assertNotNull(result);
        Assert.assertFalse(Double.isNaN(result.getX()));
        Assert.assertTrue(Math.abs(Math.PI / result.getX() - Math.PI) < 0.01);
    }

    /**
     * Test optimization with a function having multiple local minima.
     */
    @Test
    public void testOptimizationWithFunctionHavingMultipleLocalMinima() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2) * Math.exp(-Math.abs(x - 1));
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariatePointValuePair result = optimizer.optimize(function, new MaxEval(100));
        Assert.assertNotNull(result);
        Assert.assertTrue(Math.abs(function.value(result.getX()) - 0.26885) < 0.01);
    }
}