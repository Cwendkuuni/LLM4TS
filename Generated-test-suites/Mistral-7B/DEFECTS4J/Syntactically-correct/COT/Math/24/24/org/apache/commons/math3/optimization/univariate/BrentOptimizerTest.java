package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.Minimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    private UnivariateFunction testFunction;
    private Minimizer minimizer;

    @Before
    public void setup() {
        testFunction = new TestFunction1();
        minimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    /**
     * Basic test for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)} constructor.
     */
    @Test
    public void testBrentOptimizerConstructor() {
        assertNotNull(minimizer);
    }

    /**
     * Basic test for {@link BrentOptimizer#BrentOptimizer(double, double)} constructor.
     */
    @Test
    public void testBrentOptimizerConstructor2() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a simple function.
     */
    @Test
    public void testSimpleMinimize() {
        UnivariatePointValuePair result = minimizer.optimize(testFunction, GoalType.MINIMIZE);
        assertEquals(-2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a function that has a minimum at the edge of the interval.
     */
    @Test
    public void testMinimumAtEdge() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        UnivariatePointValuePair result = minimizer.optimize(new TestFunction2(), GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a function that has a minimum at the start value.
     */
    @Test
    public void testMinimumAtStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        UnivariatePointValuePair result = minimizer.optimize(new TestFunction3(), GoalType.MINIMIZE);
        assertEquals(-2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a function that has a minimum at the end value.
     */
    @Test
    public void testMinimumAtEndValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        UnivariatePointValuePair result = minimizer.optimize(new TestFunction4(), GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a function that has no minimum.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNoMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        minimizer.optimize(new TestFunction5(), GoalType.MINIMIZE);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a function that has multiple minima.
     */
    @Test
    public void testMultipleMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        UnivariatePointValuePair result = minimizer.optimize(new TestFunction6(), GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with a function that has inflection points.
     */
    @Test
    public void testInflectionPoints() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        UnivariatePointValuePair result = minimizer.optimize(new TestFunction7(), GoalType.MINIMIZE);
        assertEquals(-1.5, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with negative arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeArguments() {
        new BrentOptimizer(-1e-6, -1e-6, null);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with non-positive absolute threshold.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonPositiveAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -1e-6, null);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with non-positive relative threshold.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonPositiveRelativeThreshold() {
        new BrentOptimizer(-1e-6, 1e-6, null);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        new BrentOptimizer(1e-6, 1e-6, null);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with null function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        minimizer.optimize(null, GoalType.MINIMIZE);
    }

    /**
     * Test for {@link BrentOptimizer#doOptimize()} method with goal type other than MINIMIZE.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGoalTypeOtherThanMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new TestConvergenceChecker());
        minimizer = optimizer;
        minimizer.optimize(testFunction, GoalType.MAXIMIZE);
    }

    /**
     * Test convergence checker.
     */
    private static class TestConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        private int iterations;

        @Override
        public boolean converged(int iterations, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            this.iterations = iterations;
            return iterations > 10;
        }

        public int getIterations() {
            return iterations;
        }
    }

    /**
     * Test function for basic testing.
     */
    private static class TestFunction1 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }
    }

    /**
     * Test function for minimum at edge of interval.
     */
    private static class TestFunction2 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 1.0, 2);
        }
    }

    /**
     * Test function for minimum at start value.
     */
    private static class TestFunction3 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x + 1.0, 2) - 4.0;
        }
    }

    /**
     * Test function for minimum at end value.
     */
    private static class TestFunction4 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 2.0, 2) + 4.0;
        }
    }

    /**
     * Test function for no minimum.
     */
    private static class TestFunction5 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }

    /**
     * Test function for multiple minima.
     */
    private static class TestFunction6 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 2.0, 2) * Math.sin(Math.PI * x);
        }
    }

    /**
     * Test function for inflection points.
     */
    private static class TestFunction7 implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 1.5, 3) - 3 * Math.pow(x - 1.5, 2);
        }
    }
}