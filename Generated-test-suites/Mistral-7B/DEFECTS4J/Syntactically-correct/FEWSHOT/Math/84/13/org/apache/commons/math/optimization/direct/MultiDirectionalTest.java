package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.7, optimizer.gamma, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeKhi() {
        new MultiDirectional(-1.5, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeGamma() {
        new MultiDirectional(2.0, -0.5);
    }

    @Test
    public void testIterateSimplexDefaultComparator() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        double[] x0 = {1.0, 2.0, 3.0};
        double[] x1 = {-1.0, -2.0, -3.0};
        double f0 = -Math.pow(Math.E, 3.0) * Math.pow(Math.PI, 2.0);
        double f1 = -Math.pow(Math.E, 6.0) * Math.pow(Math.PI, 2.0);
        PointValuePair p0 = new PointValuePair(x0, f0);
        PointValuePair p1 = new PointValuePair(x1, f1);

        optimizer.setInitialPoint(p0);
        optimizer.setMaxIterations(10);
        optimizer.optimize();

        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0, best.getIndex());
        assertArrayEquals(x0, best.getPoint(), 0.001);
        assertEquals(f0, best.getValue(), 0.001);
    }

    @Test
    public void testIterateSimplexCustomComparator() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setConvergenceChecker(new RealConvergenceChecker() {
            @Override
            public boolean checkConvergence(double error) {
                return error < 0.001;
            }
        });

        double[] x0 = {1.0, 2.0, 3.0};
        double[] x1 = {-1.0, -2.0, -3.0};
        double f0 = -Math.pow(Math.E, 3.0) * Math.pow(Math.PI, 2.0);
        double f1 = -Math.pow(Math.E, 6.0) * Math.pow(Math.PI, 2.0);
        PointValuePair p0 = new PointValuePair(x0, f0);
        PointValuePair p1 = new PointValuePair(x1, f1);

        optimizer.setInitialPoint(p0);
        optimizer.setMaxIterations(10);
        optimizer.optimize();

        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0, best.getIndex());
        assertArrayEquals(x0, best.getPoint(), 0.001);
        assertEquals(f0, best.getValue(), 0.001);

        assertTrue(optimizer.isConverged());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        double[] x0 = {1.0, 2.0, 3.0};
        double[] x1 = {-1.0, -2.0, -3.0};
        double f0 = -Math.pow(Math.E, 3.0) * Math.pow(Math.PI, 2.0);
        double f1 = Double.NaN; // function evaluation exception
        PointValuePair p0 = new PointValuePair(x0, f0);
        PointValuePair p1 = new PointValuePair(x1, f1);

        optimizer.setInitialPoint(p0);
        optimizer.setMaxIterations(10);
        optimizer.optimize();
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexMaxIterationsExceeded() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        double[] x0 = {1.0, 2.0, 3.0};
        double[] x1 = {-1.0, -2.0, -3.0};
        double[] x2 = {2.0, 2.0, 2.0}; // out of bounds
        double f0 = -Math.pow(Math.E, 3.0) * Math.pow(Math.PI, 2.0);
        double f1 = -Math.pow(Math.E, 6.0) * Math.pow(Math.PI, 2.0);
        double f2 = Double.POSITIVE_INFINITY;
        PointValuePair p0 = new PointValuePair(x0, f0);
        PointValuePair p1 = new PointValuePair(x1, f1);
        PointValuePair p2 = new PointValuePair(x2, f2);

        optimizer.setInitialPoint(p0);
        optimizer.setMaxIterations(1);
        optimizer.optimize();

        optimizer.addPoint(p1);
        optimizer.addPoint(p2);

        try {
            optimizer.optimize();
            fail("Expected OptimizationException");
        } finally {
            assertNull(optimizer.getBestPoint());
        }
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();

        double[] x0 = {1.0, 2.0, 3.0};
        double[] x1 = {-1.0, -2.0, -3.0};
        double[] x2 = {-0.5, -1.5, -2.5}; // reflection point
        double[] x3 = {-2.0, -3.0, -4.0}; // contraction point
        double[] xReflected = {0.5, 1.5, 2.5}; // reflected point
        double[] xExpanded = {4.0, 5.0, 6.0}; // expanded point
        double f0 = -Math.pow(Math.E, 3.0) * Math.pow(Math.PI, 2.0);
        double f1 = -Math.pow(Math.E, 6.0) * Math.pow(Math.PI, 2.0);
        double fReflected = -Math.pow(Math.E, 4.5) * Math.pow(Math.PI, 2.0);
        double fExpanded = -Math.pow(Math.E, 9.0) * Math.pow(Math.PI, 2.0);
        PointValuePair p0 = new PointValuePair(x0, f0);
        PointValuePair p1 = new PointValuePair(x1, f1);
        PointValuePair p2 = new PointValuePair(x2, Double.NaN);
        PointValuePair p3 = new PointValuePair(x3, Double.NaN);

        optimizer.setInitialPoint(p0);
        optimizer.setMaxIterations(3);

        optimizer.addPoint(p1);
        optimizer.addPoint(p2);
        optimizer.addPoint(p3);

        optimizer.optimize();

        PointValuePair best = optimizer.getBestPoint();
        assertEquals(0, best.getIndex());
        assertArrayEquals(x0, best.getPoint(), 0.001);
        assertEquals(f0, best.getValue(), 0.001);

        // reflection
        PointValuePair reflected = optimizer.evaluateNewSimplex(simplex, 1.0, Comparator.comparingDouble(o -> o.getValue()));
        assertEquals(1, reflected.getIndex());
        assertArrayEquals(xReflected, reflected.getPoint(), 0.001);
        assertEquals(fReflected, reflected.getValue(), 0.001);

        // expansion
        PointValuePair expanded = optimizer.evaluateNewSimplex(simplex, 2.0, Comparator.comparingDouble(o -> o.getValue()));
        assertEquals(n + 1, expanded.getIndex());
        assertArrayEquals(xExpanded, expanded.getPoint(), 0.001);
        assertEquals(fExpanded, expanded.getValue(), 0.001);
    }
}