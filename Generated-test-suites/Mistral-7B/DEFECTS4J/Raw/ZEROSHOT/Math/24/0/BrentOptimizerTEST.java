package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        try {
            new BrentOptimizer(0.0, 0.1);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        new BrentOptimizer(0.001, 1.0);
        new BrentOptimizer(0.01, 0.0);
        new BrentOptimizer(1.0, 0.0);
        new BrentOptimizer(1.0, 1.0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * constructor.
     */
    @Test
    public void testConstructorWithChecker() {
        new BrentOptimizer(0.0, 0.1, null);
        Assert.assertNotNull(new BrentOptimizer(0.001, 1.0, new StubConvergenceChecker()));
        Assert.assertNotNull(new BrentOptimizer(1.0, 0.0, new StubConvergenceChecker()));
        Assert.assertNotNull(new BrentOptimizer(1.0, 1.0, new StubConvergenceChecker()));
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()} method.
     * Tests without user-defined convergence checking.
     */
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        // Test function: f(x) = x^3
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 3);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function);
        Assert.assertEquals(-1.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()} method.
     * Tests with user-defined convergence checking.
     */
    @Test
    public void testDoOptimizeWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, new StubConvergenceChecker());

        // Test function: f(x) = x^3
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 3);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function);
        Assert.assertEquals(-1.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);

        // Test function: f(x) = sin(x)
        UnivariateFunction function2 = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x);
            }
        };

        optimizer.setMinimization(true);
        UnivariatePointValuePair result2 = optimizer.optimize(function2);
        Assert.assertEquals(0.0, result2.getPoint(), 0.001);
        Assert.assertEquals(0.0, result2.getValue(), 0.001);

        optimizer.setMinimization(false);
        UnivariatePointValuePair result3 = optimizer.optimize(function2);
        Assert.assertEquals(FastMath.PI, result3.getPoint(), 0.001);
        Assert.assertEquals(-1.0, result3.getValue(), 0.001);
    }

    /**
     * Stub class for {@link ConvergenceChecker}.
     */
    public static class StubConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return true;
        }
    }
}