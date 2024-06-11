package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.01);
        assertEquals(0.5, optimizer.gamma, 0.01);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, optimizer.khi, 0.01);
        assertEquals(0.7, optimizer.gamma, 0.01);
    }

    @Test
    public void testIterateSimplexWithFunctionEvaluationException() {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[4];
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new TestFunction(), bounds);
        try {
            optimizer.iterateSimplex(Comparator.comparingDouble(p -> p.getValue()));
            fail();
        } catch (FunctionEvaluationException ex) {
            // expected
        }
    }

    @Test
    public void testIterateSimplexWithOptimizationException() {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[4];
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new TestFunction(), bounds);
        try {
            optimizer.iterateSimplex(Comparator.comparingDouble(p -> p.getValue()));
            fail();
        } catch (OptimizationException ex) {
            // expected
        }
    }

    @Test
    public void testIterateSimplexWithIllegalArgumentException() {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair[] original = new RealPointValuePair[4];
        SimpleBounds bounds = new SimpleBounds(null, new double[]{1.0, 1.0});
        try {
            optimizer.iterateSimplex(Comparator.comparingDouble(p -> p.getValue()));
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testReflectionStep() {
        final MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[5];
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new LinearFunction(new double[]{-1.0, -1.0}, new double[]{3.0}), bounds);
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(p -> p.getValue()));
        assertArrayEquals(new double[]{0.0, 0.0}, reflected.getPointRef(), 0.01);
    }

    @Test
    public void testReflectionAndExpansionStep() {
        final MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[5];
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new LinearFunction(new double[]{-1.0, -1.0}, new double[]{3.0}), bounds);
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(p -> p.getValue()));
        RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, 2.0, Comparator.comparingDouble(p -> p.getValue()));
        assertArrayEquals(new double[]{0.6, 0.6}, expanded.getPointRef(), 0.01);
    }

    @Test
    public void testReflectionAndContractStep() {
        final MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[5];
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new LinearFunction(new double[]{-1.0, -1.0}, new double[]{3.0}), bounds);
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, Comparator.comparingDouble(p -> p.getValue()));
        RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, Comparator.comparingDouble(p -> p.getValue()));
        assertArrayEquals(new double[]{0.3, 0.3}, contracted.getPointRef(), 0.01);
    }

    @Test
    public void testConvergence() {
        final MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[5];
        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        optimizer.optimize(new PolynomialFunction(new double[]{-1.0, -1.0}, new double[]{4.0}, 3), bounds);
        PointValuePair best = optimizer.getBestSolution();
        assertEquals(0.0, best.getPoint()[0], 0.01);
        assertEquals(0.0, best.getPoint()[1], 0.01);
    }

    @Test
    public void testGetKhi() {
        final MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), 0.01);
    }

    @Test
    public void testSetKhi() {
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.setKhi(3.0);
        assertEquals(3.0, optimizer.getKhi(), 0.01);
    }

    @Test
    public void testGetGamma() {
        final MultiDirectional optimizer = new MultiDirectional();
        assertEquals(0.5, optimizer.getGamma(), 0.01);
    }

    @Test
    public void testSetGamma() {
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.setGamma(0.7);
        assertEquals(0.7, optimizer.getGamma(), 0.01);
    }

    private static class TestFunction implements LinearFunction {

        @Override
        public double value(double[] x) {
            return x[0] * x[0] + x[1] * x[1];
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2.0 * x[0], 2.0 * x[1]};
        }

        @Override
        public RealMatrix jacobian(RealVector x) {
            return new Array2DRowRealMatrix(new double[][]{
                {2.0, 0.0},
                {0.0, 2.0}
            });
        }

    }

}