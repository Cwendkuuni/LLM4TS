package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalWithDefaultCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        PointValuePair best = optimizer.optimize(new TestFunction1(), new double[]{-1.0, -1.0}, new RealConvergenceChecker(1e-6, 1000));
        assertEquals(0.0, best.getPoint()[0], 1e-6);
        assertEquals(0.0, best.getPoint()[1], 1e-6);
        assertEquals(0.0, best.getValue(), 1e-6);
    }

    @Test
    public void testMultiDirectionalWithCustomCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        PointValuePair best = optimizer.optimize(new TestFunction1(), new double[]{-1.0, -1.0}, new RealConvergenceChecker(1e-6, 1000));
        assertEquals(0.0, best.getPoint()[0], 1e-6);
        assertEquals(0.0, best.getPoint()[1], 1e-6);
        assertEquals(0.0, best.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNullComparator() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = {-1.0, -1.0};
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(x, 1.0, false);
        simplex = original;
        Comparator<RealPointValuePair> comparator = null; // Replace with a valid comparator
        optimizer.evaluateNewSimplex(original, 1.0, comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testEvaluateNewSimplexThrowsOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = {-1.0, -1.0};
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(x, 1.0, false);
        simplex = original;
        Comparator<RealPointValuePair> comparator = null; // Replace with a valid comparator
        optimizer.evaluateNewSimplex(original, Integer.MAX_VALUE, comparator);
    }

    private static class TestFunction1 implements org.apache.commons.math3.optim.UnivariateFunction {
        public double value(double[] point) {
            return Math.pow(point[0] * point[0] + point[1] * point[1], 2);
        }
    }
}