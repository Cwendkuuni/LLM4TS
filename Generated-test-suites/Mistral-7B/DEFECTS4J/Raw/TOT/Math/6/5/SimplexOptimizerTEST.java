package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleXOptimizer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testMinimization() {
        MultivariateFunction f = new PolynomialFunction(2, 2, -1, 1, 4);
        AbstractSimplex simplex = new NelderMeadSimplex(new PointValuePairCollection(), new SimpleValueChecker(1e-5, 1e-5));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(100, f, goalType, null);
        double expected = 0.0;
        assertEquals(expected, result.getValue(), 1e-5);
        assertArrayEquals(new double[] {-1.0, 1.0}, result.getPoint(), 1e-5);
    }

    @Test
    public void testMaximization() {
        MultivariateFunction f = new PolynomialFunction(2, 2, 1, -1, 4);
        AbstractSimplex simplex = new NelderMeadSimplex(new PointValuePairCollection(), new SimpleValueChecker(1e-5, 1e-5));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair result = optimizer.optimize(100, f, goalType, null);
        double expected = 3.0;
        assertEquals(expected, result.getValue(), 1e-5);
        assertArrayEquals(new double[] {1.0, -1.0}, result.getPoint(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        AbstractSimplex simplex = new NelderMeadSimplex(new PointValuePairCollection(), new SimpleValueChecker(1e-5, 1e-5));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        optimizer.optimize(100, null, GoalType.MINIMIZE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonConvergentFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                throw new ArithmeticException();
            }
        };
        AbstractSimplex simplex = new NelderMeadSimplex(new PointValuePairCollection(), new SimpleValueChecker(1e-5, 1e-5));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        optimizer.optimize(100, f, GoalType.MINIMIZE, null);
    }
}