package org.apache.commons.math.optimization.test;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.RandomStartInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultistartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexSearch;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link MultistartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-8;
    private static final int MAX_ITERATIONS = 1000;
    private static final int MAX_EVALUATIONS = 1000;
    private static final int STARTS = 10;

    private UnivariateRealFunction function;
    private UnivariateRealOptimizer optimizer;
    private MultistartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator randomGenerator;

    @BeforeEach
    public void setUp() {
        function = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        optimizer = new SimplexSearch(function, EPSILON);
        randomGenerator = new MersenneTwister();
    }

    /**
     * Test optimization with given {@link MultistartUnivariateRealOptimizer}.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOptimization() throws Exception {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double result = optimizerWrapper.optimize(function, goalType, min, max);
        double expected = -1.0;
        assertEquals(expected, result, EPSILON);

        // check that all starts have been performed
        double[] optima = optimizerWrapper.getOptima();
        assertEquals(STARTS, optima.length);
        for (double optimum : optima) {
            assertNotNull(optimum);
        }
    }

    /**
     * Test optimization with too few starts.
     * @throws Exception if optimization fails
     */
    @Test
    public void testTooFewStarts() throws Exception {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, 0, randomGenerator);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        assertThrows(IllegalArgumentException.class, () -> optimizerWrapper.optimize(function, goalType, min, max));
    }

    /**
     * Test optimization with {@code null} optimizer.
     */
    @Test
    public void testNullOptimizer() {
        assertThrows(NullPointerException.class, () -> new MultistartUnivariateRealOptimizer(null, STARTS, randomGenerator));
    }

    /**
     * Test optimization with {@code null} goal type.
     */
    @Test
    public void testNullGoalType() {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        assertThrows(NullPointerException.class, () -> optimizerWrapper.optimize(null, GoalType.MINIMIZE, -10.0, 10.0));
    }

    /**
     * Test optimization with {@code null} function.
     */
    @Test
    public void testNullFunction() {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        assertThrows(NullPointerException.class, () -> optimizerWrapper.optimize(null, GoalType.MINIMIZE, -10.0, 10.0));
    }

    /**
     * Test optimization with {@code null} random generator.
     */
    @Test
    public void testNullRandomGenerator() {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, null);
        assertThrows(NullPointerException.class, () -> optimizerWrapper.optimize(function, GoalType.MINIMIZE, -10.0, 10.0));
    }

    /**
     * Test optimization with given bounds out of range.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOutOfRangeBounds() throws Exception {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        assertThrows(IllegalArgumentException.class, () -> optimizerWrapper.optimize(function, GoalType.MINIMIZE, min, max));
    }

    /**
     * Test optimization with given bounds equal.
     * @throws Exception if optimization fails
     */
    @Test
    public void testBoundsEqual() throws Exception {
        double min = 1.0;
        double max = 1.0;
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        double result = optimizerWrapper.optimize(function, GoalType.MINIMIZE, min, max);
        double expected = min;
        assertEquals(expected, result, EPSILON);
    }

    /**
     * Test optimization with {@code null} optima array.
     */
    @Test
    public void testNullOptima() {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        assertThrows(NullPointerException.class, () -> optimizerWrapper.getOptima());
    }

    /**
     * Test optimization with {@code null} optima values array.
     */
    @Test
    public void testNullOptimaValues() {
        UnivariateRealOptimizer optimizerWrapper = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        assertThrows(NullPointerException.class, () -> optimizerWrapper.getOptimaValues());
    }

    /**
     * Test optimization with given {@link MultistartUnivariateRealOptimizer} and {@link AroundMinimumInitializer}.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOptimizationWithAroundMinimumInitializer() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(2, new double[]{0.0, 1.0, 1.0});
        UnivariateRealOptimizer optimizer = new SimplexSearch(function, EPSILON);
        MultistartUnivariateRealOptimizer multiStartOptimizer = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double result = multiStartOptimizer.optimize(function, goalType, min, max);
        double expected = 0.0;
        assertEquals(expected, result, EPSILON);

        // check that all starts have been performed
        double[] optima = multiStartOptimizer.getOptima();
        assertEquals(STARTS, optima.length);
        for (double optimum : optima) {
            assertTrue(optimum >= min && optimum <= max);
        }

        // check that all starts have converged
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        for (double optimaValue : optimaValues) {
            assertFalse(Double.isNaN(optimaValue));
            assertEquals(0.0, optimaValue, EPSILON);
        }

        // check that the optimizer is not throwing any exceptions
        assertThrows(ConvergenceException.class, () -> optimizer.optimize(function, goalType, min, max));
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, goalType, min, max));
        assertThrows(MathRuntimeException.class, () -> optimizer.optimize(function, goalType, min, max));
    }

    /**
     * Test optimization with given {@link MultistartUnivariateRealOptimizer} and {@link RandomStartInitializer}.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOptimizationWithRandomStartInitializer() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(2, new double[]{0.0, 1.0, 1.0});
        UnivariateRealOptimizer optimizer = new SimplexSearch(function, EPSILON);
        MultistartUnivariateRealOptimizer multiStartOptimizer = new MultistartUnivariateRealOptimizer(optimizer, STARTS, randomGenerator);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double result = multiStartOptimizer.optimize(function, goalType, min, max);
        double expected = 0.0;
        assertEquals(expected, result, EPSILON);

        // check that all starts have been performed
        double[] optima = multiStartOptimizer.getOptima();
        assertEquals(STARTS, optima.length);
        for (double optimum : optima) {
            assertTrue(optimum >= min && optimum <= max);
        }

        // check that all starts have not converged
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        for (double optimaValue : optimaValues) {
            assertTrue(Double.isNaN(optimaValue));
        }
    }
}