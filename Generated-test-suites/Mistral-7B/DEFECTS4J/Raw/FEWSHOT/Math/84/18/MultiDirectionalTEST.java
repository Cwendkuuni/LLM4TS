package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealVector;
import org.apache.commons.math3.optim.SimpleValuesFunction;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunctionGrad;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunctionValue;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.UnivariateFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunctionValue;
import org.junit.Test;

import org.apache.commons.cli.Util;

public class MultiDirectionalTest {
    private static final double EPSILON = 1e-8;
    private static final double TOLERANCE = 1e-6;

    private static class TestFunction implements ObjectiveFunction, ObjectiveFunctionGrad {
        @Override
        public double value(RealVector x) throws NumberIsTooLargeException {
            double sum = 0.0;
            for (int i = 0; i < x.getDimension(); ++i) {
                sum += Math.pow(x.getEntry(i), 2.0);
            }
            return sum;
        }

        @Override
        public double[] gradient(RealVector x) throws NumberIsTooLargeException {
            double[] grad = new double[x.getDimension()];
            for (int i = 0; i < x.getDimension(); ++i) {
                grad[i] = 2.0 * x.getEntry(i);
            }
            return grad;
        }
    }

    @Test
    public void testMultiDirectional() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                if (p1.getValue() < p2.getValue()) {
                    return -1;
                } else if (p1.getValue() > p2.getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        MultiDirectional optimizer = new MultiDirectional();

        ObjectiveFunction f = new SimpleValuesFunction("test", 2, new TestFunction());
        RealVector start = new org.apache.commons.math3.optim.point.PointValuePair(new double[]{1.0, 1.0}, 3.0);
        RealVector xBest = new org.apache.commons.math3.optim.point.PointValuePair(new double[]{1.0, -1.0}, 0.0);

        optimizer.setInitialGuess(new InitialGuess() {
            @Override
            public PointValuePair getInitialGuess() {
                return start;
            }
        });

        optimizer.setMaxEvaluations(1000);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair solution = optimizer.optimize(f);
        assertEquals(xBest, solution, EPSILON);

        System.out.println("Solution found: " + solution);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsReached() throws Exception {
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                if (p1.getValue() < p2.getValue()) {
                    return -1;
                } else if (p1.getValue() > p2.getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        MultiDirectional optimizer = new MultiDirectional();

        ObjectiveFunction f = new SimpleValuesFunction("test", 2, new TestFunction());
        RealVector start = new org.apache.commons.math3.optim.point.PointValuePair(new double[]{1.0, 1.0}, 3.0);
        RealVector xBest = new org.apache.commons.math3.optim.point.PointValuePair(new double[]{1.0, -1.0}, 0.0);

        optimizer.setInitialGuess(new InitialGuess() {
            @Override
            public PointValuePair getInitialGuess() {
                return start;
            }
        });

        optimizer.setMaxEvaluations(1);
        optimizer.setGoalType(GoalType.MINIMIZE);

        optimizer.optimize(f);
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo\" bar", Util.stripLeadingAndTrailingQuotes("foo\" bar"));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                if (p1.getValue() < p2.getValue()) {
                    return -1;
                } else if (p1.getValue() > p2.getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        ObjectiveFunction f = new SimpleValuesFunction("test", 2, new TestFunction());
        RealVector start = new org.apache.commons.math3.optim.point.PointValuePair(new double[]{1.0, 1.0}, 3.0);
        RealVector xBest = new org.apache.commons.math3.optim.point.PointValuePair(new double[]{1.0, -1.0}, 0.0);

        optimizer.setInitialGuess(new InitialGuess() {
            @Override
            public PointValuePair getInitialGuess() {
                return start;
            }
        });

        optimizer.setMaxIterations(100);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair solution = optimizer.optimize(f);
        assertEquals(xBest, solution, EPSILON);

        // Perform some basic checks
        assertEquals(100, optimizer.getIterations());
        assertEquals(TOLERANCE, optimizer.getBestValue(), EPSILON);
        assertArrayEquals(xbest.getPointRef(), optimizer.getBestPoint().getPointRef(), EPSILON);

        // Check that the simplex evaluation order is correct
        PointValuePairList simplex = new PointValuePairList();
        optimizer.iterateSimplex(comparator);
        double[] xOriginal = start.getPointRef();
        double[] xReflected = new double[xOriginal.length];
        double[] xExpanded = new double[xOriginal.length];
        simplex.add(new PointValuePair(new org.apache.commons.math3.optim.point.PointValuePair(xOriginal, xBest.getValue()), xBest.getValue()));

        int i = 1;
        for (PointValuePair pointValuePair : optimizer.getSimplex()) {
            simplex.add(pointValuePair);
            if (i == 1) {
                Arrays.systemArrayCopy(xOriginal, 0, xReflected, 0);
                System.arraycopy(xOriginal, 0, xExpanded, 0);
                double coeff = 1.0;
                if (i <= xOriginal.length) {
                    xReflected[i - 1] += coeff * (xOriginal[i - 1] - xReflected[i - 1]);
                    xExpanded[i - 1] += optimizer.khi * (xOriginal[i - 1] - xReflected[i - 1]);
                }
                PointValuePair reflected = new org.apache.commons.math3.optim.point.PointValuePair(xReflected, f.value(new org.apache.commons.math3.optim.point.PointValuePair(xReflected, Double.NaN, false)));
                PointValuePair expanded = new org.apache.commons.math3.optim.point.PointValuePair(xExpanded, Double.NaN);

                assertEquals(comparator.compare(reflected, simplex.get(i)), -1);
                assertEquals(comparator.compare(expanded, simplex.get(i)), -1);
                ++i;
            }
        }

        assertEquals(1 + xOriginal.length, simplex.size());
    }

}