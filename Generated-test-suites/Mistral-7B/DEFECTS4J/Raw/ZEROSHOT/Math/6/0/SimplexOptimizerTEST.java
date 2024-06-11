package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.initialguess.RandomMinimumInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueCheckerMinimizer;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeMinimizerVector;
import org.apache.commons.math3.optim.nonlinear.vector.SimpleValueCheckerMinimizerVector;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.simplex.NelderMeadSimplex;
import org.apache.commons.math3.optim.simplex.NelderMeadSimplexMinimizer;
import org.apache.commons.math3.optim.simplex.SimplexPoint;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {
    /** Function to optimize. */
    private MultivariateFunction function;

    /** Optimizer under test. */
    private SimplexOptimizer optimizer;

    /** Simplex under test. */
    private NelderMeadSimplex simplex;

    /** Initial point. */
    private double[] initialPoint = {-1.0, -1.0};

    /** Goal type for optimization. */
    private GoalType goalType = GoalType.MINIMIZE;

    /** Convergence checker for optimization. */
    private SimpleValueChecker<PointValuePair> convergenceChecker;

    /** Maximum number of function evaluations allowed. */
    private int maxEval = 1000;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        // Create a simple function for testing.
        function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        optimizer = new SimplexOptimizer(new SimpleValueCheckerMinimizerVector(1e-6, 1e-6));
        simplex = new NelderMeadSimplexMinimizer(10);
        convergenceChecker = new SimpleValueCheckerMinimizer(1e-4, 1e-4);
    }

    /**
     * Test optimization with random initial point.
     */
    @Test
    public void testOptimizationRandomPoint() {
        // Create a random initial point.
        double[] randomPoint = new RandomMinimumInitialGuess(2).getInitialPoint();
        assertArrayEquals(new double[]{-0.2135625034934374, -0.0366220744468193}, randomPoint, 0.0);

        // Define the optimization data with the random point.
        PointValuePair[] optimizationData = new PointValuePair[]{ new PointValuePair(randomPoint, 0.0) };

        // Call the optimize method with a random initial point and check the result.
        PointValuePair result = optimizer.optimize(maxEval, function, goalType, optimizationData);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with user-defined initial point.
     */
    @Test
    public void testOptimizationUserDefinedPoint() {
        // Call the optimize method with a user-defined initial point and check the result.
        PointValuePair result = optimizer.optimize(maxEval, function, goalType, initialPoint);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with convergence checker.
     */
    @Test
    public void testOptimizationConvergenceChecker() {
        // Set the convergence checker for the optimizer.
        optimizer.setConvergenceChecker(convergenceChecker);

        // Call the optimize method and check the result.
        PointValuePair result = optimizer.optimize(maxEval, function, goalType);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertNotNull(simplex.getBestPoint());
        assertNotNull(simplex.getWorstPoint());
    }

    /**
     * Test optimization with simplex.
     */
    @Test
    public void testOptimizationWithSimplex() {
        // Set the simplex for the optimizer.
        optimizer.setSimplex(simplex);

        // Call the optimize method and check the result.
        PointValuePair result = optimizer.optimize(maxEval, function, goalType);
        assertEquals(goalType, result.getGoalType(), 0);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertNotNull(simplex.getBestPoint());
        assertNotNull(simplex.getWorstPoint());
    }

    /**
     * Test optimization with custom goal type.
     */
    @Test
    public void testOptimizationWithCustomGoalType() {
        // Create a custom goal type.
        GoalTypeMinimizerVector customGoalType = new GoalTypeMinimizerVector(true);

        // Set the custom goal type for the optimizer.
        optimizer.setGoalType(customGoalType);

        // Call the optimize method and check the result.
        PointValuePair result = optimizer.optimize(maxEval, function, goalType);
        assertEquals(customGoalType, result.getGoalType());
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizationNullFunction() {
        optimizer.optimize(maxEval, null, goalType);
    }

    /**
     * Test optimization with null goal type.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizationNullGoalType() {
        optimizer.optimize(maxEval, function, null);
    }

    /**
     * Test optimization with null optimization data.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizationNullOptimizationData() {
        optimizer.optimize(maxEval, function, goalType, null);
    }
}