package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.junit.Test;

public class MultiDirectionalTest {

    private static final double EPSILON = 1e-5;
    private static final double MAX_EVAL = 100;

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.getKhi(), EPSILON);
        assertEquals(0.5, optimizer.getGamma(), EPSILON);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        assertEquals(3.0, optimizer.getKhi(), EPSILON);
        assertEquals(0.8, optimizer.getGamma(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeKhi() {
        new MultiDirectional(-1.0, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalNegativeGamma() {
        new MultiDirectional(2.0, -0.5);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        RealFunction function = new PointValuePairFunction(new SimpleFunction());
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0, new double[]{-1.0, 1.0});
        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        PointValuePair initialPoint = new PointValuePair(new double[]{-0.5, 0.5}, 1.0);
        RealPointValuePair[] simplex = {initialPoint};

        optimizer.optimize(function, bounds, GoalType.MINIMIZE, comparator);

        // Reflection test
        PointValuePair reflectedPoint = new PointValuePair(new double[]{0.0, 1.0}, 1.5);
        RealPointValuePair[] reflectedSimplex = new RealPointValuePair[2];
        reflectedSimplex[0] = simplex[0];
        reflectedSimplex[1] = reflectedPoint;
        RealPointValuePair reflected = optimizer.iterateSimplex(reflectedSimplex, comparator);
        assertEquals(reflectedPoint.getPoint(), reflected.getPointRef(), EPSILON);

        // Expansion test
        RealPointValuePair[] expandedSimplex = new RealPointValuePair[3];
        expandedSimplex[0] = simplex[0];
        expandedSimplex[1] = reflectedPoint;
        expandedSimplex[2] = new RealPointValuePair(new double[]{1.5, 0.5}, Double.NaN, false);
        RealPointValuePair expanded = optimizer.iterateSimplex(expandedSimplex, comparator);
        assertTrue(expanded.getValue() < reflected.getValue(), "Expanded point should have a better value than reflected point.");

        // Contraction test
        RealPointValuePair contractedPoint = new PointValuePair(new double[]{-0.25, 0.75}, 0.75);
        RealPointValuePair[] contractedSimplex = new RealPointValuePair[2];
        contractedSimplex[0] = simplex[0];
        contractedSimplex[1] = contractedPoint;
        RealPointValuePair contracted = optimizer.iterateSimplex(contractedSimplex, comparator);
        assertEquals(contractedPoint.getPoint(), contracted.getPointRef(), EPSILON);

        // Convergence test
        RealPointValuePair convergencePoint = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        RealPointValuePair[] convergenceSimplex = new RealPointValuePair[2];
        convergenceSimplex[0] = simplex[0];
        convergenceSimplex[1] = convergencePoint;
        RealPointValuePair convergence = optimizer.iterateSimplex(convergenceSimplex, comparator);
        assertEquals(convergencePoint.getPoint(), convergence.getPointRef(), EPSILON);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testIterateSimplexMaxEval() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        RealFunction function = new PointValuePairFunction(new SimpleFunction());
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0, new double[]{-1.0, 1.0});
        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        PointValuePair initialPoint = new PointValuePair(new double[]{-0.5, 0.5}, 1.0);
        RealPointValuePair[] simplex = {initialPoint};

        optimizer.optimize(function, bounds, GoalType.MINIMIZE, comparator);

        optimizer.setMaxIterations(1);
        optimizer.iterateSimplex(simplex, comparator);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIterateSimplexNumberTooSmall() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        RealFunction function = new PointValuePairFunction(new SimpleFunction());
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0, new double[]{-1.0, 1.0});
        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        PointValuePair initialPoint = new PointValuePair(new double[]{-0.5, 0.5}, 1.0);
        RealPointValuePair[] simplex = {initialPoint};

        optimizer.optimize(function, bounds, GoalType.MINIMIZE, comparator);

        optimizer.setMinStepSize(0.0);
        optimizer.iterateSimplex(simplex, comparator);
    }

    private static class SimpleFunction implements RealFunction {

        @Override
        public double value(double[] x) {
            return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.sin(x[1]), 2);
        }

        @Override
        public PointValuePair gradient(double[] x) {
            double[] gradient = new double[x.length];
            gradient[0] = -2 * Math.cos(x[0]) * Math.sin(x[0]);
            gradient[1] = -2 * Math.cos(x[1]) * Math.sin(x[1]);
            return new PointValuePair(gradient, value(x));
        }

    }

}