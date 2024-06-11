package org.apache.commons.math3.optimization.direct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker.ConvergenceType;
import org.apache.commons.math3.optimization.abstractopt.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.test.AbstractMultivariateOptimizerTest;
import org.junit.jupiter.api.Test;

/**
 * Testing SimplexOptimizer class from the Apache Commons Math library.
 */
public class SimplexOptimizerTest extends AbstractMultivariateOptimizerTest {

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer()}.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(ConvergenceType.REL_ABS, optimizer.getConvergenceChecker()
                .getConvergenceType());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(SimpleValueChecker)}.
     */
    @Test
    public void testConstructorWithChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6));
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(ConvergenceType.REL_ABS, optimizer.getConvergenceChecker()
                .getConvergenceType());
        assertEquals(1.0e-6, optimizer.getConvergenceChecker().getRelativeThreshold(), 1.0e-7);
        assertEquals(1.0e-6, optimizer.getConvergenceChecker().getAbsoluteThreshold(), 1.0e-7);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize() {
        double[] startPoint = new double[] { 1.0, 1.0 };
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = new OptimizationData() {
            public AbstractSimplex getSimplex() {
                return new RegularSimplex(2);
            }
        };

        double[] expectedPoint = new double[] { 0.0, 0.0 };
        double expectedValue = 0.0;
        PointValuePair expectedResult = new PointValuePair(expectedPoint, expectedValue);

        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData, startPoint);
        assertEquals(expectedResult, result);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * Checks that the optimizer returns the expected error message when the
     * optimization data does not contain the expected simplex.
     */
    @Test
    public void testOptimizeWithoutSimplexInData() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1], 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = new OptimizationData();

        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(100, function, goalType, optimizationData, new double[] { 0.0, 0.0 }));
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        double[] startPoint = new double[] { 1.0, 1.0 };
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new RegularSimplex(2);

        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, function, goalType, startPoint);
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * Checks that the optimizer throws an exception when the goal type is not valid.
     */
    @Test
    public void testOptimizeWithInvalidGoalType() {
        double[] startPoint = new double[] { 1.0, 1.0 };
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE; // Invalid goal type for minimization-based optimizer
        SimplexOptimizer optimizer = new SimplexOptimizer();

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(100, function, goalType, startPoint, new Object[] {}));
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * Checks that the optimizer throws an exception when the optimization data contains an incorrect number of points.
     */
    @Test
    public void testOptimizeWithIncorrectNumberOfPoints() {
        double[] startPoint = new double[] { 1.0, 1.0 };
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = new OptimizationData() {
            public AbstractSimplex getSimplex() {
                return new RegularSimplex(1); // Should be 2 points
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(100, function, goalType, optimizationData, startPoint));
    }
}