package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.RealVectorFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import org.junit.Test;

public class MultiDirectionalTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullComparator() {
        new MultiDirectional(null);
    }

    @Test
    public void testConstructorDefault() {
        final MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    @Test
    public void testConstructorCustom() {
        final double khi = 1.5;
        final double gamma = 0.8;
        final MultiDirectional optimizer = new MultiDirectional(khi, gamma);
        assertNotNull(optimizer);
        assertEquals(khi, optimizer.khi, 0.001);
        assertEquals(gamma, optimizer.gamma, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexNullComparator() {
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() {
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.simplex = new RealPointValuePair[0];
        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexMaxEvalExceededException() {
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        final MultiDirectional optimizer = new MultiDirectional();
        final RealVectorFunction function = new DummyFunction();
        optimizer.function = function;
        optimizer.maxIterations = 0;
        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIterateSimplexNumberIsTooSmallException() {
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue()) > 0 ? 1 : -1;
            }
        };
        final MultiDirectional optimizer = new MultiDirectional();
        final RealVectorFunction function = new DummyFunction();
        optimizer.function = function;
        optimizer.maxIterations = 5;
        optimizer.iterateSimplex(comparator);
        optimizer.setMaxIterations(1);
        optimizer.iterateSimplex(comparator);
    }

    @Test
    public void testIterateSimplexSuccess() {
        final Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue()) > 0 ? 1 : -1;
            }
        };
        final MultiDirectional optimizer = new MultiDirectional();
        final RealVectorFunction function = new DummyFunction();
        optimizer.function = function;
        optimizer.maxIterations = 5;
        optimizer.setMinimizationSimpleBounds(new SimpleBounds(1.0, 2.0, new double[0]));
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.iterateSimplex(comparator);
        final PointValuePair solution = optimizer.getSolution();
        assertNotNull(solution);
        assertTrue(solution.getValue() < 2.0);
    }

}

class DummyFunction implements RealVectorFunction {
    @Override
    public double value(double[] x) {
        return 0.0;
    }

    @Override
    public double[] gradient(double[] x) {
        return new double[0];
    }

    @Override
    public double[] getGradient(double[] x, double stepSize, double[] workingSetSize) {
        return new double[0];
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }
}