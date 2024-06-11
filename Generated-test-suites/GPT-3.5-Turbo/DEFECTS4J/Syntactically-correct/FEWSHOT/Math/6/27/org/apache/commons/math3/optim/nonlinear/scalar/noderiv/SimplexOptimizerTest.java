package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void optimizationWithoutSimplex() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(1000, function, goalType);
    }

    @Test
    public void optimizationWithSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(2) {

            protected void initialize(PointValuePair[] referenceSimplex) {
                referenceSimplex[0] = new PointValuePair(new double[]{-1, -1}, 2);
                referenceSimplex[1] = new PointValuePair(new double[]{1, -1}, 4);
                referenceSimplex[2] = new PointValuePair(new double[]{0, 2}, 6);
            }
        };
        optimizer.setSimplex(simplex);

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = optimizer.optimize(1000, function, goalType);
        double[] point = result.getPoint();
        assertEquals(0.0, point[0], 1e-5);
        assertEquals(0.0, point[1], 1e-5);
    }

}