package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.statistics.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Powell optimizer instance.
     */
    private PowellOptimizer powellOptimizer;

    /**
     * Sets up the test by creating a Powell optimizer instance.
     */
    @Before
    public void setUp() {
        powellOptimizer = new PowellOptimizer(1e-5, 1e-5);
    }

    /**
     * Basic test for {@code PowellOptimizer#optimize()} method with a simple
     * univariate polynomial function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateFunction fun = new PolynomialFunction(new double[]{-1, 2, 1},
                                                       new double[]{0, 0, 1});
        double[] startPoint = new double[]{3.0};
        double expected = 1.0;
        PointValuePair result = powellOptimizer.optimize(fun, startPoint);
        assertEquals(expected, result.getValue(), 1e-8);
    }

    /**
     * Test for {@code PowellOptimizer#optimize()} method with a multivariate
     * polynomial function using default start point.
     */
    @Test
    public void testOptimizeMultivariateFunctionDefaultStartPoint() {
        MultivariateFunction fun = new MultivariatePolynomialFunction(2,
                                                                    new double[][]{-1, 1, 1, -1, -1, 1},
                                                                    new double[]{0, 0, 0, 0, 0, 0});
        PointValuePair result = powellOptimizer.optimize(fun, null);

        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF = -2.0;
        assertEquals(expectedX[0], result.getPoint().getValue(0), 1e-8);
        assertEquals(expectedX[1], result.getPoint().getValue(1), 1e-8);
        assertEquals(expectedF, result.getValue(), 1e-8);
    }

    /**
     * Test for {@code PowellOptimizer#optimize()} method with a multivariate
     * polynomial function using custom start point.
     */
    @Test
    public void testOptimizeMultivariateFunctionCustomStartPoint() {
        MultivariateFunction fun = new MultivariatePolynomialFunction(2,
                                                                    new double[][]{-1, 1, 1, -1, -1, 1},
                                                                    new double[]{0, 0, 0, 0, 0, 0});
        double[] startPoint = new double[]{2.0, 2.0};
        PointValuePair result = powellOptimizer.optimize(fun, startPoint);

        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF = -2.0;
        assertEquals(expectedX[0], result.getPoint().getValue(0), 1e-8);
        assertEquals(expectedX[1], result.getPoint().getValue(1), 1e-8);
        assertEquals(expectedF, result.getValue(), 1e-8);
    }

    /**
     * Test for {@code PowellOptimizer#optimize()} method with a custom
     * convergence checker.
     */
    @Test
    public void testOptimizeCustomConvergenceChecker() {
        MultivariateFunction fun = new MultivariatePolynomialFunction(2,
                                                                    new double[][]{-1, 1, 1, -1, -1, 1},
                                                                    new double[]{0, 0, 0, 0, 0, 0});
        ConvergenceChecker<PointValuePair> customChecker =
                new CustomConvergenceChecker(1e-6, 1e-6);
        double[] startPoint = new double[]{1.5, 1.5};
        PointValuePair result = powellOptimizer.optimize(fun, startPoint, customChecker);

        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF = -2.0;
        assertEquals(expectedX[0], result.getPoint().getValue(0), 1e-8);
        assertEquals(expectedX[1], result.getPoint().getValue(1), 1e-8);
        assertEquals(expectedF, result.getValue(), 1e-8);
        assertTrue(customChecker.isConverged(1, result, new PointValuePair(startPoint, 0.0)));
    }

    /**
     * Test for {@code PowellOptimizer#optimize()} method with a custom
     * initializer.
     */
    @Test
    public void testOptimizeCustomInitializer() {
        MultivariateFunction fun = new MultivariatePolynomialFunction(2,
                                                                    new double[][]{-1, 1, 1, -1, -1, 1},
                                                                    new double[]{0, 0, 0, 0, 0, 0});
        AverageInitializer initializer = new AverageInitializer(2);
        PointValuePair result = powellOptimizer.optimize(fun, initializer);

        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF = -2.0;
        assertEquals(expectedX[0], result.getPoint().getValue(0), 1e-8);
        assertEquals(expectedX[1], result.getPoint().getValue(1), 1e-8);
        assertEquals(expectedF, result.getValue(), 1e-8);
    }

    /**
     * Test for {@code PowellOptimizer#optimize()} method with a multivariate
     * polynomial function and custom bounds.
     */
    @Test
    public void testOptimizeMultivariateFunctionCustomBounds() {
        MultivariateFunction fun = new MultivariatePolynomialFunction(2,
                                                                    new double[][]{-1, 1, 1, -1, -1, 1},
                                                                    new double[]{0, 0, 0, 0, 0, 0});
        SimpleBounds bounds = new SimpleBounds(new double[]{-2.0, -2.0},
                                               new double[]{2.0, 2.0});
        PointValuePair result = powellOptimizer.optimize(fun, bounds);

        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF = -2.0;
        assertEquals(expectedX[0], result.getPoint().getValue(0), 1e-8);
        assertEquals(expectedX[1], result.getPoint().getValue(1), 1e-8);
        assertEquals(expectedF, result.getValue(), 1e-8);
    }

    /**
     * Test for {@code PowellOptimizer#optimize()} method with a multivariate
     * polynomial function and custom convergence checker and bounds.
     */
    @Test
    public void testOptimizeMultivariateFunctionCustomCheckerAndBounds() {
        MultivariateFunction fun = new MultivariatePolynomialFunction(2,
                                                                    new double[][]{-1, 1, 1, -1, -1, 1},
                                                                    new double[]{0, 0, 0, 0, 0, 0});
        SimpleBounds bounds = new SimpleBounds(new double[]{-2.0, -2.0},
                                               new double[]{2.0, 2.0});
        ConvergenceChecker<PointValuePair> customChecker =
                new CustomConvergenceChecker(1e-6, 1e-6);
        PointValuePair result = powellOptimizer.optimize(fun, bounds, customChecker);

        double expectedX[] = new double[]{1.0, 1.0};
        double expectedF = -2.0;
        assertEquals(expectedX[0], result.getPoint().getValue(0), 1e-8);
        assertEquals(expectedX[1], result.getPoint().getValue(1), 1e-8);
        assertEquals(expectedF, result.getValue(), 1e-8);
        assertTrue(customChecker.isConverged(1, result, new PointValuePair(bounds.getMin(), 0.0)));
    }

    /**
     * Custom convergence checker for {@code PowellOptimizerTest#testOptimizeCustomChecker()} test.
     */
    private static class CustomConvergenceChecker
            extends SimpleUnivariateValueChecker {

        public CustomConvergenceChecker(double relTolerance, double absTolerance) {
            super(relTolerance, absTolerance);
        }

        @Override
        public boolean isConverged(int iteration, PointValuePair current,
                                   PointValuePair previous) {
            if (super.isConverged(iteration, current, previous)) {
                return true;
            }

            DescriptiveStatistics stats = new DescriptiveStatistics(
                    new PointValuePairCollection(new PointValuePair[]{previous, current}));
            double stdDev = stats.getStandardDeviation();
            double mean = stats.getMean();
            return (stdDev < 1e-6);
        }
    }
}