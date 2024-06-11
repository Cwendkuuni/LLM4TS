package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedOptimizationException;
import org.apache.commons.math3.optim.initializer.SimpleStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.vector.linear.ArrayRealVector;
import org.apache.commons.math3.optim.nonlinear.vector.linear.RealVector;
import org.apache.commons.math3.optim.simpledirect.SimpleDirectMethod;
import org.apache.commons.math3.optim.simpledirect.SimplexMethod;
import org.junit.Test;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {

    /**
     * Test constructor with default convergence checker.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with custom convergence checker.
     */
    @Test
    public void testConstructorWithCustomChecker() {
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-5, 1.0e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with relative and absolute thresholds.
     */
    @Test
    public void testConstructorWithThresholds() {
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-5, 1.0e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker.getRelativeAccuracy(),
                                                         checker.getAbsoluteAccuracy());
        assertEquals(checker.getRelativeAccuracy(), optimizer.getConvergenceChecker()
                                                            .getRelativeAccuracy(), 0.0);
        assertEquals(checker.getAbsoluteAccuracy(), optimizer.getConvergenceChecker()
                                                         .getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test setSimplex method.
     */
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());
        SimplexMethod simplex = new SimplexMethod(new SimpleBounds(new double[]{-1.0, 1.0}));
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Test optimize method with valid argument.
     */
    @Test
    public void testOptimizeValidArgument() {
        double[] startPoint = new double[]{-1.0, 1.0};
        RealVector startVector = new ArrayRealVector(startPoint);
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return FunctionUtils.square(point[0]) + FunctionUtils.square(point[1]) + 1.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        SimpleStartingPointGenerator pointGen = new SimpleStartingPointGenerator();
        PointValuePair result = optimizer.optimize(10, f, goalType, new Object[]{pointGen,
            new SimplexMethod(new SimpleBounds(new double[]{-3.0, 3.0}))});
        double[] expected = new double[]{1.0, 1.0};
        assertArrayEquals(expected, result.getPoint().toArray(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test optimize method with null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullArgument() {
        MultivariateFunction f = null;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, f, GoalType.MINIMIZE, new Object[0]);
    }

    /**
     * Test optimize method with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidGoalType() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, f, GoalType.MAXIMIZE, new Object[0]);
    }

    /**
     * Test optimize method with unbounded optimization.
     */
    @Test(expected = UnboundedOptimizationException.class)
    public void testOptimizeUnboundedOptimization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0];
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, f, GoalType.MINIMIZE, new Object[0]);
    }

    /**
     * Test optimize method with not initialized simplex.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNotInitializedSimplex() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, f, GoalType.MINIMIZE, new Object[0]);
    }

    /**
     * Test optimize method with valid argument and convergence checker.
     */
    @Test
    public void testOptimizeValidArgumentWithChecker() {
        double[] startPoint = new double[]{-1.0, 1.0};
        RealVector startVector = new ArrayRealVector(startPoint);
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return FunctionUtils.square(point[0]) + FunctionUtils.square(point[1]) + 1.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-5, 1.0e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        SimpleStartingPointGenerator pointGen = new SimpleStartingPointGenerator();
        PointValuePair result = optimizer.optimize(10, f, goalType, new Object[]{pointGen,
            new SimplexMethod(new SimpleBounds(new double[]{-3.0, 3.0}))});
        double[] expected = new double[]{1.0, 1.0};
        assertArrayEquals(expected, result.getPoint().toArray(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test optimize method with custom convergence checker and different goal type.
     */
    @Test
    public void testOptimizeCustomCheckerAndDifferentGoalType() {
        double[] startPoint = new double[]{-1.0, 1.0};
        RealVector startVector = new ArrayRealVector(startPoint);
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return FunctionUtils.square(point[0]) + FunctionUtils.square(point[1]) + 1.0;
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-5, 1.0e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        SimpleStartingPointGenerator pointGen = new SimpleStartingPointGenerator();
        PointValuePair result = optimizer.optimize(10, f, goalType, new Object[]{pointGen,
            new SimplexMethod(new SimpleBounds(new double[]{-3.0, 3.0}))});
        double[] expected = new double[]{-1.0, -1.0};
        assertArrayEquals(expected, result.getPoint().toArray(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
}