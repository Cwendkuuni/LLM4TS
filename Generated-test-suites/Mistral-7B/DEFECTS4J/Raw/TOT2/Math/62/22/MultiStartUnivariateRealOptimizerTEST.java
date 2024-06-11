package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.initializer.MinMaxInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private static final double EPSILON = 1.0e-6;
    private static final int MAX_EVALUATIONS = 10;
    private static final int NUM_STARTS = 5;
    private static final int MAX_ITERATIONS = 200;

    private UnivariateRealOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private FUNC testFunction;
    private RandomGenerator random;

    @Before
    public void setUp() {
        optimizer = new FibonacciOptimizer(EPSILON, MAX_ITERATIONS);
        testFunction = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        multiStartOptimizer.setMaxEvaluations(MAX_EVALUATIONS);
    }

    /**
     * Test that the optimizer returns the correct minimum for a given
     * function and range.
     */
    @Test
    public void testOptimize() throws Exception {
        double min = -3.0;
        double max = 3.0;
        PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertArrayEquals(new double[]{result.getPoint()[0], result.getValue()}, new double[]{-1.0, 1.0}, EPSILON);
    }

    /**
     * Test that the optimizer returns null if no convergence is reached.
     */
    @Test
    public void testNoConvergence() throws Exception {
        double min = -3.0;
        double max = 3.0;
        assertNull(multiStartOptimizer.optimize(new PolynomialFunction(1, new double[]{1.0}),
                GoalType.MINIMIZE, min, max));
    }

    /**
     * Test that the optimizer uses the specified number of starts.
     */
    @Test
    public void testNumberOfStarts() throws Exception {
        double min = -3.0;
        double max = 3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS * NUM_STARTS);
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertTrue(optima.length == NUM_STARTS || Arrays.stream(optima).anyMatch(p -> p == null));
    }

    /**
     * Test that the optimizer uses a random starting point for each run.
     */
    @Test
    public void testRandomStartingPoint() throws Exception {
        double min = -3.0;
        double max = 3.0;
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        for (int i = 1; i < optima.length; i++) {
            assertFalse(optima[0].getPoint()[0] == optima[i].getPoint()[0]);
        }
    }

    /**
     * Test that the optimizer uses the provided optimizer for each run.
     */
    @Test
    public void testWrappedOptimizer() throws Exception {
        double min = -3.0;
        double max = 3.0;
        UnivariateRealOptimizer<FUNC> wrappedOptimizer = new FibonacciOptimizer(EPSILON, MAX_ITERATIONS);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartWrapper = new MultiStartUnivariateRealOptimizer<>(wrappedOptimizer, NUM_STARTS, new MersenneTwister());
        multiStartWrapper.optimize(testFunction, GoalType.MINIMIZE, min, max);
        multiStartWrapper.optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertTrue(multiStartWrapper.optimizer.getEvaluations() >= MAX_EVALUATIONS);
    }

    /**
     * Test that the optimizer returns the correct number of evaluations.
     */
    @Test
    public void testNumberOfEvaluations() throws Exception {
        double min = -3.0;
        double max = 3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        PointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertTrue(multiStartOptimizer.getEvaluations() >= MAX_EVALUATIONS * NUM_STARTS);
    }

    /**
     * Test that the optimizer throws an exception when the optimizer's
     * maximum number of evaluations is exceeded.
     */
    @Test(expected = ConvergenceException.class)
    public void testExceededMaxEvaluations() throws Exception {
        double min = -3.0;
        double max = 3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS / 2);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test that the optimizer throws an exception when the initializer's
     * minimum value is greater than the maximum value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinMaxRange() throws Exception {
        double min = 3.0;
        double max = -3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test that the optimizer throws an exception when the goal type is not
     * recognized.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() throws Exception {
        double min = -3.0;
        double max = 3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        multiStartOptimizer.optimize(testFunction, new GoalType() {}, min, max);
    }

    /**
     * Test that the optimizer throws an exception when the underlying
     * optimizer is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnsetOptimizer() throws Exception {
        double min = -3.0;
        double max = 3.0;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(null, NUM_STARTS, new MersenneTwister());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test that the optimizer uses a valid initializer.
     */
    @Test
    public void testValidInitializer() throws Exception {
        double min = -3.0;
        double max = 3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizerWithInitializer = new MultiStartUnivariateRealOptimizer<>(optimizer, new AverageInitializer(0.5), new MersenneTwister());
        multiStartOptimizerWithInitializer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        multiStartOptimizerWithInitializer.optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertTrue(multiStartOptimizerWithInitializer.optimizer.getInitializer() instanceof AverageInitializer);
    }

    /**
     * Test that the optimizer uses a valid convergence checker.
     */
    @Test
    public void testValidConvergenceChecker() throws Exception {
        double min = -3.0;
        double max = 3.0;
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean test(PointValuePair candidate) {
                return false;
            }
        };
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizerWithChecker = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new MersenneTwister());
        multiStartOptimizerWithChecker.setConvergenceChecker(convergenceChecker);
        multiStartOptimizerWithChecker.optimize(testFunction, GoalType.MINIMIZE, min, max);
        multiStartOptimizerWithChecker.optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max);
        assertTrue(multiStartOptimizerWithChecker.getConvergenceChecker() == convergenceChecker);
    }
}