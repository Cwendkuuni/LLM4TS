package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleValueChecker.ConvergenceStatus;
import org.apache.commons.math3.optim.UnboundedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeTest;
import org.apache.commons.math3.optim.nonlinear.scalar.example.RastriginFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.example.SphereFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.example.TestFunctions;
import org.apache.commons.math3.optim.simple.AbstractSimplex;
import org.apache.commons.math3.optim.simple.SimplexPoint;
import org.apache.commons.math3.optim.simple.SimplexSimplex;
import org.apache.commons.math3.optim.simple.SimplexSimplex.EvolutionStrategy;
import org.apache.commons.math3.optim.simple.SimplexSimplex.SimplexState;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** The simplex optimizer instance to test. */
    private SimplexOptimizer optimizer;

    /** The simplex instance used in the tests. */
    private AbstractSimplex simplex;

    /** The convergence checker instance used in the tests. */
    private SimpleValueChecker checker;

    /** The goal type for the tests. */
    private GoalType goalType;

    /** The initial guess for the tests. */
    private InitialGuess initialGuess;

    /** The maximum number of evaluations for the tests. */
    private MaxEval maxEval;

    /** The Rastrigin function for the tests. */
    private MultivariateFunction rastriginFunction;

    /** The sphere function for the tests. */
    private MultivariateFunction sphereFunction;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        // Initialize the convergence checker.
        checker = new SimpleValueChecker(1e-6, 1e-6);

        // Initialize the goal type.
        goalType = GoalType.MINIMIZE;

        // Initialize the initial guess.
        initialGuess = new InitialGuess() {
            public double[] getInitialPoint() {
                return new double[] {1.0, 1.0};
            }
        };

        // Initialize the maximum number of evaluations.
        maxEval = new MaxEval(100);

        // Initialize the simplex.
        simplex = new SimplexSimplex(new SimplexPoint[] {
            new SimplexPoint(0.0, 0.0),
            new SimplexPoint(1.0, 0.0),
            new SimplexPoint(0.5, 0.5),
            new SimplexPoint(1.0, 1.0)}, EvolutionStrategy.JAMES_DEMMEL);

        // Initialize the optimizer.
        optimizer = new SimplexOptimizer(checker);
    }

    /**
     * Tests the constructor with no arguments.
     */
    @Test(expected = DeprecatedException.class)
    public void testConstructorNoArguments() {
        new SimplexOptimizer();
    }

    /**
     * Tests the constructor with a convergence checker.
     */
    @Test
    public void testConstructorWithChecker() {
        new SimplexOptimizer(checker);
    }

    /**
     * Tests the constructor with a relative and absolute threshold.
     */
    @Test
    public void testConstructorWithRelAndAbs() {
        new SimplexOptimizer(1e-6, 1e-6);
    }

    /**
     * Tests the {@code setSimplex} method with a null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        optimizer.setSimplex(null);
    }

    /**
     * Tests the {@code optimize} method with a null objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullFunction() {
        optimizer.optimize(maxEval, null, goalType, initialGuess.getInitialPoint());
    }

    /**
     * Tests the {@code optimize} method with a null optimization data.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullOptData() {
        optimizer.optimize(maxEval, new SphereFunction(2), goalType, null);
    }

    /**
     * Tests the {@code optimize} method with a null goal type.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullGoalType() {
        optimizer.optimize(maxEval, new SphereFunction(2), null, initialGuess.getInitialPoint());
    }

    /**
     * Tests the {@code optimize} method with a null optimization data array.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullOptDataArray() {
        optimizer.optimize(maxEval, new SphereFunction(2), goalType, new OptimizationData[0]);
    }

    /**
     * Tests the {@code setSimplex} method with a valid simplex.
     */
    @Test
    public void testSetSimplex() {
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Tests the {@code optimize} method with a Rastrigin function and the
     * default goal type.
     */
    @Test
    public void testOptimizeRastrigin() {
        rastriginFunction = new RastriginFunction(2);

        // Initialize the simplex.
        simplex.build(new double[] {1.1, 1.1});

        // Initialize the optimization data.
        OptimizationData[] optData = new OptimizationData[] {simplex};

        PointValuePair result = optimizer.optimize(maxEval, rastriginFunction, null, optData);

        // Check the result.
        assertFalse(Double.isNaN(result.getValue()));
        assertTrue(result.getValue() >= 0.0);
        assertTrue(result.getValue() <= FunctionUtils.min(rastriginFunction));

        // Check the optimization data.
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Tests the {@code optimize} method with a Rastrigin function and a
     * custom goal type.
     */
    @Test
    public void testOptimizeRastriginCustomGoalType() {
        rastriginFunction = new RastriginFunction(2);

        // Initialize the simplex.
        simplex.build(new double[] {1.1, 1.1});

        // Initialize the optimization data.
        OptimizationData[] optData = new OptimizationData[] {simplex};

        // Set a custom goal type.
        goalType = GoalTypeTest.OPTIMAL_POINT;

        PointValuePair result = optimizer.optimize(maxEval, rastriginFunction, goalType, optData);

        // Check the result.
        assertFalse(Double.isNaN(result.getValue()));
        assertTrue(result.getValue() >= 0.0);
        assertEquals(GoalTypeTest.OPTIMAL_POINT, result.getGoalType());
        assertEquals(rastriginFunction.getMinimum(), result.getValue(), 1e-6);

        // Check the optimization data.
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Tests the {@code optimize} method with a sphere function and the
     * default goal type.
     */
    @Test
    public void testOptimizeSphere() {
        sphereFunction = new SphereFunction(2);

        PointValuePair result = optimizer.optimize(maxEval, sphereFunction, goalType, initialGuess);

        // Check the result.
        assertFalse(Double.isNaN(result.getValue()));
        assertTrue(result.getValue() >= 0.0);
        assertTrue(Math.abs(result.getValue()) <= 1e-6);

        // Check the optimization data.
        assertNull(optimizer.getSimplex());
    }

    /**
     * Tests the {@code optimize} method with a sphere function and a
     * custom goal type.
     */
    @Test
    public void testOptimizeSphereCustomGoalType() {
        sphereFunction = new SphereFunction(2);

        // Set a custom goal type.
        goalType = GoalTypeTest.OPTIMAL_POINT;

        PointValuePair result = optimizer.optimize(maxEval, sphereFunction, goalType, initialGuess);

        // Check the result.
        assertFalse(Double.isNaN(result.getValue()));
        assertEquals(GoalTypeTest.OPTIMAL_POINT, result.getGoalType());
        assertEquals(sphereFunction.getMinimum(), result.getValue(), 1e-6);

        // Check the optimization data.
        assertNull(optimizer.getSimplex());
    }

    /**
     * Tests the {@code optimize} method with a Rastrigin function and a
     * custom convergence checker.
     */
    @Test
    public void testOptimizeRastriginCustomChecker() {
        rastriginFunction = new RastriginFunction(2);

        // Initialize the simplex.
        simplex.build(new double[] {1.1, 1.1});

        // Initialize the optimization data.
        OptimizationData[] optData = new OptimizationData[] {simplex};

        // Set a custom convergence checker.
        checker = new SimpleValueChecker() {
            public ConvergenceStatus checkConvergence(int iter, PointValuePair current, PointValuePair previous) {
                ConvergenceStatus status = super.checkConvergence(iter, current, previous);
                if (status != ConvergenceStatus.CONVERGED) {
                    return status;
                }
                assertEquals(current.getValue(), previous.getValue(), 1e-6);
                return ConvergenceStatus.CONVERGED;
            }
        };

        optimizer = new SimplexOptimizer(checker);

        PointValuePair result = optimizer.optimize(maxEval, rastriginFunction, goalType, optData);

        // Check the result.
        assertFalse(Double.isNaN(result.getValue()));
        assertTrue(result.getValue() >= 0.0);
        assertEquals(result.getValue(), FunctionUtils.min(rastriginFunction), 1e-6);

        // Check the optimization data.
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Tests the {@code optimize} method with a Rastrigin function and a
     * converged optimization data.
     */
    @Test(expected = UnboundedOptimizationException.class)
    public void testOptimizeRastriginConvergedData() {
        rastriginFunction = new RastriginFunction(2);

        // Initialize the simplex.
        simplex.build(new double[] {1.1, 1.1});

        // Initialize the optimization data.
        OptimizationData[] optData = new OptimizationData[] {
            new OptimizationData() {
                public AbstractSimplex getSimplex() {
                    return simplex;
                }
            },
            new OptimizationData() {
                public AbstractSimplex getSimplex() {
                    throw new RuntimeException();
                }
            }
        };

        PointValuePair result = optimizer.optimize(maxEval, rastriginFunction, goalType, optData);

        // This test should not reach here.
        fail("Unexpected result.");
    }

    /**
     * Tests the {@code optimize} method with a Rastrigin function and a
     * non-converged optimization data.
     */
    @Test(expected = IllegalStateException.class)
    public void testOptimizeRastriginNonConvergedData() {
        rastriginFunction = new RastriginFunction(2);

        // Initialize the simplex.
        simplex.build(new double[] {1.1, 1.1});

        // Initialize the optimization data.
        OptimizationData[] optData = new OptimizationData[] {
            new OptimizationData() {
                public AbstractSimplex getSimplex() {
                    return simplex;
                }
            },
            new OptimizationData() {
                public AbstractSimplex getSimplex() {
                    return new SimplexSimplex(new SimplexPoint[] {
                        new SimplexPoint(0.1, 0.1),
                        new SimplexPoint(1.1, 0.1),
                        new SimplexPoint(0.5, 0.5),
                        new SimplexPoint(1.0, 1.0)
                    }, SimplexState.EVOLVE);
                }
            }
        };

        PointValuePair result = optimizer.optimize(maxEval, rastriginFunction, goalType, optData);

        // This test should not reach here.
        fail("Unexpected result.");
    }
}