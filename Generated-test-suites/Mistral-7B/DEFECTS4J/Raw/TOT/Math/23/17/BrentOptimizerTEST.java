package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.BrentOptimizerConfig;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) * Math.exp(-Math.abs(x));
        }

        /** {@inheritDoc} */
        @Override
        public double derivate(double x) {
            double sinX = Math.sin(x);
            double expAbsX = Math.exp(-Math.abs(x));
            double result = 2 * Math.pow(sinX, 2) * Math.exp(-Math.abs(x))
                    - Math.pow(Math.cos(x), 2) * Math.exp(-Math.abs(x));
            return result;
        }
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(BrentOptimizerConfig)}
     * constructor.
     * @throws NotStrictlyPositiveException if an exception is thrown.
     */
    @Test
    public void testBrentOptimizerConstructor() throws NotStrictlyPositiveException {
        BrentOptimizerConfig config = new BrentOptimizerConfig(1e-6, 1e-6);
        UnivariateOptimizer optimizer = new BrentOptimizer(config);
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * constructor.
     * @throws NotStrictlyPositiveException if an exception is thrown.
     */
    @Test
    public void testBrentOptimizerConstructorAbsRel() throws NotStrictlyPositiveException {
        BrentOptimizer optimizer
                = new BrentOptimizer(1e-6, 1e-6);
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method with a function that has a global minimum.
     */
    @Test
    public void testOptimizeMinimum() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), 1.0, 10.0);
        PointValuePair result = optimizer.optimize();
        double expected = -3.1415926536;
        Assert.assertEquals(expected, result.getPoint(), 1e-10);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method with a function that has a global maximum.
     */
    @Test
    public void testOptimizeMaximum() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), -1.0, 1.0);
        PointValuePair result = optimizer.optimize();
        double expected = 3.1415926536;
        Assert.assertEquals(expected, result.getPoint(), 1e-10);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method with a function that has no root.
     */
    @Test
    public void testOptimizeNoRoot() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), 0.0, 0.0);
        PointValuePair result = optimizer.optimize();
        Assert.assertNull(result);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method with a function that has multiple roots.
     */
    @Test
    public void testOptimizeMultipleRoots() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), -5.0, 5.0);
        PointValuePair result = optimizer.optimize();
        double[] expected
                = {-3.1415926536, 3.1415926536};
        Assert.assertArrayEquals(expected, new double[]{result.getPoint(),
                optimizer.optimize().getPoint()}, 1e-10);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method when the function has no derivative.
     */
    @Test
    public void testOptimizeNoDerivative() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), 1.0, 10.0);
        PointValuePair result = optimizer.optimize();
        double expected = -3.1415926536;
        Assert.assertEquals(expected, result.getPoint(), 1e-10);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method when the function has a vertical tangent.
     */
    @Test
    public void testOptimizeVerticalTangent() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), 1.0, 2.0);
        PointValuePair result = optimizer.optimize();
        double expected = 1.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-10);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}
     * method when the function has a flat bottom.
     */
    @Test
    public void testOptimizeFlatBottom() {
        TestFunction function = new TestFunction();
        BrentOptimizer optimizer
                = new BrentOptimizer(new TestFunction(), 1.0, 1.1);
        PointValuePair result = optimizer.optimize();
        double expected = 1.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-10);
    }
}