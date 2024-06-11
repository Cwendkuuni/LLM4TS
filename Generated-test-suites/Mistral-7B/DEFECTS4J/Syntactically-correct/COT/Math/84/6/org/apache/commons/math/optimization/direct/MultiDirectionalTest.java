package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.FunctionEvaluationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeFunction;
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
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.8, optimizer.gamma, 0.001);
    }

    @Test
    public void testIterateSimplexBasicFunctionality() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = new double[] { 1.0, 2.0 };
        PointValuePair best = new PointValuePair(x, 4.0);
        RealPointValuePair[] simplex = new RealPointValuePair[] { new RealPointValuePair(x, 5.0), best, new RealPointValuePair(x, 6.0) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        optimizer.iterateSimplex(comparator);
        RealPointValuePair actual = optimizer.simplex[0];
        RealPointValuePair expected = best;
        assertEquals(expected.getPointRef(), actual.getPointRef());
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = new double[] { 1.0, 2.0 };
        RealPointValuePair[] simplex = new RealPointValuePair[] { new RealPointValuePair(x, 5.0), new RealPointValuePair(x, 6.0) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        optimizer.setMaxEvaluations(1);
        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexThrowsOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = new double[] { 1.0, 2.0 };
        RealPointValuePair[] simplex = new RealPointValuePair[] { new RealPointValuePair(x, 5.0), new RealPointValuePair(x, 6.0) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        optimizer.setMaxIterations(1);
        optimizer.iterateSimplex(comparator);
    }

    @Test
    public void testEvaluateNewSimplexBasicFunctionality() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[] { 1.0, 2.0 };
        double coeff = 1.5;
        RealPointValuePair[] original = new RealPointValuePair[] { new RealPointValuePair(xSmallest, 5.0), new RealPointValuePair(xSmallest, 6.0) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        RealPointValuePair actual = optimizer.evaluateNewSimplex(original, coeff, comparator);
        RealPointValuePair expected = new RealPointValuePair(new double[] { 1.5 * (xSmallest[0] - 1.0) + 1.0, 1.5 * (xSmallest[1] - 1.0) + 2.0 }, Double.NaN);
        assertArrayEquals(expected.getPointRef(), actual.getPointRef());
    }

    @Test(expected = NullArgumentException.class)
    public void testEvaluateNewSimplexNullComparator() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[] { 1.0, 2.0 };
        double coeff = 1.5;
        RealPointValuePair[] original = new RealPointValuePair[] { new RealPointValuePair(xSmallest, 5.0), new RealPointValuePair(xSmallest, 6.0) };
        comparator = null;
        optimizer.evaluateNewSimplex(original, coeff, comparator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNewSimplexNegativeCoefficient() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[] { 1.0, 2.0 };
        double coeff = -1.5;
        RealPointValuePair[] original = new RealPointValuePair[] { new RealPointValuePair(xSmallest, 5.0), new RealPointValuePair(xSmallest, 6.0) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        optimizer.evaluateNewSimplex(original, coeff, comparator);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testEvaluateNewSimplexMaxEvaluationsExceeded() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[] { 1.0, 2.0 };
        double coeff = 1.5;
        RealPointValuePair[] original = new RealPointValuePair[] { new RealPointValuePair(xSmallest, 5.0), new RealPointValuePair(xSmallest, 6.0) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        optimizer.setMaxEvaluations(1);
        optimizer.evaluateNewSimplex(original, coeff, comparator);
    }

    @Test(expected = NoBracketingException.class)
    public void testEvaluateNewSimplexNoBracketing() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] xSmallest = new double[] { 1.0, 2.0 };
        double coeff = 1.5;
        RealPointValuePair[] original = new RealPointValuePair[] { new RealPointValuePair(xSmallest, 5.0), new RealPointValuePair(xSmallest, Double.POSITIVE_INFINITY) };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        optimizer.evaluateNewSimplex(original, coeff, comparator);
    }

}