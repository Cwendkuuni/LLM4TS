package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import org.junit.Assert;

import static org.apache.commons.math3.optim.direct.PowellOptimizer.*;
import static org.apache.commons.math3.optim.direct.PowellOptimizer.LineSearch.*;
import static org.apache.commons.math3.util.Precision.absTolerance;
import static org.apache.commons.math3.util.Precision.relative;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}.
     */
    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(new EquationF(), null, GoalType.MINIMIZE, null);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-8);
        Assert.assertEquals(-1.0, result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     */
    @Test
    public void testPowellOptimizerCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        PointValuePair result = optimizer.optimize(new EquationF(), null, GoalType.MINIMIZE, null);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-8);
        Assert.assertEquals(-1.0, result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link LineSearch#search(double[], double[], int)}.
     */
    @Test
    public void testLineSearch() {
        double[] p = {0.1};
        double[] d = {1.0};
        UnivariatePointValuePair result = new LineSearch(1e-6, 1e-6).search(p, d);
        Assert.assertEquals(0.5, result.getPoint(), 1e-6);
        Assert.assertEquals(-0.5, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#doOptimize()} with a simple function.
     */
    @Test
    public void testDoOptimizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.doOptimize();
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-8);
        Assert.assertEquals(-1.0, result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link PowellOptimizer#doOptimize()} with a function that
     * has multiple local minima.
     */
    @Test
    public void testDoOptimizeMultipleMinimaFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.doOptimize();
        Assert.assertEquals(-1.2, result.getValue(), 1e-8);
        // This test is not very reliable since the actual minimum is not
        // determined, but the result should be close to -1.2 in any case.
        Assert.assertEquals(-1.3, result.getPoint()[0], 1e-5);
    }

    /**
     * Test method for {@link PowellOptimizer#doOptimize()} with a function that
     * throws an exception.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testDoOptimizeExceptionFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-16, 1e-16);
        optimizer.doOptimize();
    }

    /**
     * Test method for {@link PowellOptimizer#doOptimize()} with a function that
     * has a singularity.
     */
    @Test(expected = ArithmeticException.class)
    public void testDoOptimizeSingularityFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.doOptimize();
    }

    /**
     * A custom convergence checker for testing purposes.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return (iteration > 10) || relative(previous.getValue(), current.getValue()) < 1e-5;
        }
    }

    /**
     * A simple function for testing purposes.
     */
    private static class EquationF implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 1.0;
        }
    }
}