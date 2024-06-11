package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleOptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.simpledirect.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.simpledirect.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.simpledirect.SimplexOptimizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {

    /** Test function for optimization. */
    private MultivariateFunction testFunction;

    /** Test simplex. */
    private AbstractSimplex simplex;

    /** Optimizer under test. */
    private SimplexOptimizer optimizer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        testFunction = new PolynomialFunction(new double[]{-10.0, 1.0, 1.0}, 3, 1.0);
        simplex = new NelderMeadSimplex(new double[]{-1.0, -1.0, -1.0}, 3);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
    }

    /**
     * Test the optimization of a 3D polynomial function.
     */
    @Test
    public void testOptimizePoly() {
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MINIMIZE, new OptimizationData[]{data});
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 1.0E-6);
        assertEquals(0.0, result.getValue(), 1.0E-6);
    }

    /**
     * Test the optimization of a function with a local minimum.
     */
    @Test
    public void testOptimizeLocalMinimum() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{data});
        assertEquals(-1.0, result.getValue(), 1.0E-6);
        assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 1.0E-6);
    }

    /**
     * Test the optimization of a function with a maximum.
     */
    @Test
    public void testOptimizeMaximum() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) - Math.pow(point[1] - 2.0, 2.0);
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        try {
            optimizer.optimize(100, function, GoalType.MAXIMIZE, new OptimizationData[]{data});
            assertFalse("Expected UnboundedOptimizationException", true);
        } catch (UnboundedOptimizationException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with an invalid goal type.
     */
    @Test
    public void testOptimizeInvalidGoalType() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        try {
            optimizer.optimize(100, function, GoalType.MAXIMIZE_BOUNDED, new OptimizationData[]{data});
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with a null objective function.
     */
    @Test
    public void testOptimizeNullObjectiveFunction() {
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        try {
            optimizer.optimize(100, null, GoalType.MINIMIZE, new OptimizationData[]{data});
            assertFalse("Expected NullPointerException", true);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with an invalid number of dimensions.
     */
    @Test
    public void testOptimizeInvalidNumberOfDimensions() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        try {
            optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{data});
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with a null initial guess.
     */
    @Test
    public void testOptimizeNullInitialGuess() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        OptimizationData data = null;
        try {
            optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{data});
            assertFalse("Expected NullPointerException", true);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with an invalid number of dimensions in the optimization data.
     */
    @Test
    public void testOptimizeInvalidNumberOfDimensionsInData() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        data.setInput(0, new double[]{1.0});
        try {
            optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{data});
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the optimization with an illegal simplex.
     */
    @Test
    public void testOptimizeIllegalSimplex() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, null);
        try {
            optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{data});
            assertFalse("Expected NullPointerException", true);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with an illegal initial guess.
     */
    @Test
    public void testOptimizeIllegalInitialGuess() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        OptimizationData data = new SimpleOptimizationData(null, simplex);
        try {
            optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{data});
            assertFalse("Expected NullPointerException", true);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test the optimization of a function with a large number of evaluations.
     */
    @Test
    public void testOptimizeLargeNumberOfEvaluations() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        OptimizationData data = new SimpleOptimizationData(initialGuess, simplex);
        try {
            optimizer.optimize(Integer.MAX_VALUE, function, GoalType.MINIMIZE, new OptimizationData[]{data});
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

}