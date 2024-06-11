package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.optimizers.SimplexOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValueChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for SimplexOptimizer class.
 */
public class SimplexOptimizerTest {

    private MultivariateFunction testFunction;
    private SimplexOptimizer optimizer;
    private Double[][] testPoints;

    @Before
    public void setUp() {
        testFunction = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.sum(x);
            }
        };
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 1.0e-6));
        testPoints = new Double[][] {{0.0, 0.0}, {1.0, 0.0}, {0.5, 0.5}, {1.5, 0.5}, {1.0, 1.0}};
    }

    @After
    public void tearDown() {
        optimizer = null;
        testFunction = null;
    }

    /**
     * Test that SimplexOptimizer throws NullPointerException if no simplex
     * is set.
     */
    @Test(expected = NullPointerException.class)
    public void testNullSimplex() {
        optimizer = new SimplexOptimizer();
        optimizer.optimize(5, testFunction, GoalType.MINIMIZE, new Object[0]);
    }

    /**
     * Test that SimplexOptimizer correctly optimizes a 1-dimensional function
     * with a simplex of size 3.
     */
    @Test
    public void testOneDimensionalFunction() {
        optimizer.setSimplex(new SimpleSimplex(3));
        PointValuePair result = optimizer.optimize(5, testFunction, GoalType.MINIMIZE,
                new Object[] {new InitialGuess(testPoints[0])});
        assertEquals(testPoints[0][0], result.getPoint()[0], 1.0e-6);
        assertEquals(testPoints[0][0], result.getValue(), 1.0e-6);
    }

    /**
     * Test that SimplexOptimizer correctly optimizes a 2-dimensional function
     * with a simplex of size 5.
     */
    @Test
    public void testTwoDimensionalFunction() {
        optimizer.setSimplex(new SimpleSimplex(5));
        PointValuePair result = optimizer.optimize(5, testFunction, GoalType.MINIMIZE,
                new Object[] {new InitialGuess(testPoints[0])});
        assertArrayEquals(testPoints[0], result.getPoint(), 1.0e-6);
        assertEquals(testPoints[0][0] + testPoints[0][1], result.getValue(), 1.0e-6);

        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair resultMax = optimizer.optimize(5, testFunction, GoalType.MAXIMIZE,
                new Object[] {new InitialGuess(testPoints[4])});
        assertArrayEquals(testPoints[4], resultMax.getPoint(), 1.0e-6);
        assertEquals(testPoints[4][0] + testPoints[4][1], resultMax.getValue(), 1.0e-6);
    }

    /**
     * Test that SimplexOptimizer correctly optimizes a 2-dimensional function
     * with simple bounds.
     */
    @Test
    public void testTwoDimensionalFunctionWithBounds() {
        SimpleBounds bounds = new SimpleBounds(new double[] {-1.0, -1.0}, new double[] {1.0, 1.0});
        optimizer.setSimplex(new SimpleSimplex(5));
        PointValuePair result = optimizer.optimize(5, testFunction, GoalType.MINIMIZE,
                new Object[] {new InitialGuess(testPoints[0]), bounds});
        assertArrayEquals(testPoints[0], result.getPoint(), 1.0e-6);
        assertEquals(testPoints[0][0] + testPoints[0][1], result.getValue(), 1.0e-6);

        SimpleBounds boundsMax = new SimpleBounds(new double[] {1.0, 1.0}, new double[] {3.0, 3.0});
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair resultMax = optimizer.optimize(5, testFunction, GoalType.MAXIMIZE,
                new Object[] {new InitialGuess(testPoints[4]), boundsMax});
        assertArrayEquals(testPoints[4], resultMax.getPoint(), 1.0e-6);
        assertEquals(testPoints[4][0] + testPoints[4][1], resultMax.getValue(), 1.0e-6);
    }

    /**
     * Test that SimplexOptimizer correctly handles GoalTypeValueChecker.
     */
    @Test
    public void testGoalTypeValueChecker() {
        GoalTypeValueChecker checker = new GoalTypeValueChecker();
        optimizer.setConvergenceChecker(checker);
        optimizer.setSimplex(new SimpleSimplex(5));
        PointValuePair result = optimizer.optimize(5, testFunction, GoalType.MINIMIZE,
                new Object[] {new InitialGuess(testPoints[0])});
        assertEquals(testPoints[0], result.getPoint(), 1.0e-6);
        assertEquals(testPoints[0][0] + testPoints[0][1], result.getValue(), 1.0e-6);
        assertTrue(checker.isSatisfied(result.getValue()));

        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair resultMax = optimizer.optimize(5, testFunction, GoalType.MAXIMIZE,
                new Object[] {new InitialGuess(testPoints[4])});
        assertArrayEquals(testPoints[4], resultMax.getPoint(), 1.0e-6);
        assertEquals(testPoints[4][0] + testPoints[4][1], resultMax.getValue(), 1.0e-6);
        assertTrue(checker.isSatisfied(resultMax.getValue()));
    }
}