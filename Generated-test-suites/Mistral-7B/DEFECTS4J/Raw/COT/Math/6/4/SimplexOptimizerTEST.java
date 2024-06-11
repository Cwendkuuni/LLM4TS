package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {

    // Test method for constructor SimplexOptimizer()
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    // Test method for constructor SimplexOptimizer(ConvergenceChecker<PointValuePair>)
    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    // Test method for constructor SimplexOptimizer(double, double)
    @Test
    public void testConstructorWithRelAndAbs() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.0);
        assertNotNull(optimizer);
    }

    // Test method for method setSimplex(AbstractSimplex)
    @Test(expected = DeprecatedException.class)
    public void testDeprecatedSetSimplexMethod() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new Simplex());
    }

    // Test method for method optimize(int,MultivariateFunction,GoalType,double[])
    @Test
    public void testOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {2.0, 2.0};
        SimplexOptimizer optimizer = new SimplexOptimizer();

        PointValuePair result = optimizer.optimize(100, function, goalType, startPoint);

        assertEquals(goalType, result.getGoalType());
        assertEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(-3.0, result.getValue(), 0.001);
    }
}