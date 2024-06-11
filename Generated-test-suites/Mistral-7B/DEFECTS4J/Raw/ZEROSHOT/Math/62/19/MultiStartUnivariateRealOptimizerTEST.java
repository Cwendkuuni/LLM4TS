package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Optimizer to use for testing. */
    private UnivariateOptimizer optimizer;
    /** Multi-start optimizer to test. */
    private MultiStartUnivariateRealOptimizer<UnivariateFunction> multiOptimizer;
    /** Random generator for multi-start. */
    private RandomGenerator generator;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());
    }

    /**
     * Test optimization with simple bounds.
     */
    @Test
    public void testOptimizeWithSimpleBounds() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1., 1., 2.}, 3);
        double min = -10.0;
        double max = 10.0;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair result = multiOptimizer.optimize(f, goal, min, max);

        assertEquals(GoalType.MINIMIZE, goal);
        assertEquals(min, result.getPoint(), 1e-12);
        assertEquals(-3.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with custom bounds.
     */
    @Test
    public void testOptimizeWithCustomBounds() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1., 1., 2.}, 3);
        double min = 1.0;
        double max = 5.0;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair result = multiOptimizer.optimize(f, goal, new SimpleBounds(min, max));

        assertEquals(GoalType.MINIMIZE, goal);
        assertEquals(min + 2.0, result.getPoint(), 1e-12);
        assertEquals(-1.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with a custom initial guess.
     */
    @Test
    public void testOptimizeWithCustomInitialGuess() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1., 1., 2.}, 3);
        double min = -10.0;
        double max = 10.0;
        GoalType goal = GoalType.MAXIMIZE;
        double initialGuess = 3.0;

        UnivariateRealPointValuePair result = multiOptimizer.optimize(f, goal, new SimpleBounds(min, max),
                new InitialGuess(initialGuess));

        assertEquals(GoalType.MAXIMIZE, goal);
        assertEquals(max, result.getPoint(), 1e-12);
        assertEquals(3.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with a custom convergence checker.
     */
    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1., 1., 2.}, 3);
        double min = -10.0;
        double max = 10.0;
        GoalType goal = GoalType.MINIMIZE;

        // Custom convergence checker that always returns true.
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean test(UnivariateRealPointValuePair candidate, double fx,
                               UnivariateRealPointValuePair previousBest, double fxPreviousBest) {
                return true;
            }
        };

        multiOptimizer.setConvergenceChecker(convergenceChecker);

        UnivariateRealPointValuePair result = multiOptimizer.optimize(f, goal, min, max);

        assertNotNull(result);
        assertEquals(min, result.getPoint(), 1e-12);
        assertEquals(-3.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with a custom optimization strategy.
     */
    @Test
    public void testOptimizeWithCustomOptimizationStrategy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1., 1., 2.}, 3);
        double min = -10.0;
        double max = 10.0;
        GoalType goal = GoalType.MINIMIZE;

        // Custom optimization strategy that always returns 2 evaluations.
        MaxEval<UnivariateRealPointValuePair> maxEval = new MaxEval<UnivariateRealPointValuePair>(2);
        multiOptimizer.setMaxEvaluations(maxEval.getMaxEvaluations());

        UnivariateRealPointValuePair result = multiOptimizer.optimize(f, goal, min, max);

        assertEquals(min, result.getPoint(), 1e-12);
        assertEquals(-3.0, result.getValue(), 1e-12);
        assertEquals(2, multiOptimizer.getEvaluations());
    }
}