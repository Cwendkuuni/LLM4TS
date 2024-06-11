package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test multi-start optimization for min and max using a simple function
     */
    @Test
    public void testMultiStartOptimization() {

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 1;
            }
        };

        // Initialize optimizer
        UnivariateRealOptimizer singleStartOptimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                singleStartOptimizer, 5, new MersenneTwisterFast());

        // Test minimization
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.1;
        double max = 10.0;
        PointValuePair best = multiStartOptimizer.optimize(f, goal, min, max);
        Assert.assertNotNull(best);
        Assert.assertEquals(0.0, best.getPoint(), 1e-8);

        // Test maximization
        goal = GoalType.MAXIMIZE;
        PointValuePair worst = multiStartOptimizer.optimize(f, goal, min, max);
        Assert.assertNull(worst);

        // Test error scenarios
        try {
            multiStartOptimizer.optimize(null, goal, min, max);
            Assert.fail("Expected IllegalArgumentException for null function");
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            multiStartOptimizer.optimize(f, null, min, max);
            Assert.fail("Expected IllegalArgumentException for null goal");
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            multiStartOptimizer.optimize(f, goal, max, min);
            Assert.fail("Expected IllegalArgumentException for min > max");
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        // Test max evaluations
        maxEvaluationsChecker(multiStartOptimizer, f, goal, min, max, 100);
    }

    /**
     * Test that maximum number of evaluations is respected
     *
     * @param optimizer The optimizer to test
     * @param function The function to optimize
     * @param goal The goal
     * @param min The minimum bound
     * @param max The maximum bound
     * @param maxEvals Maximum number of evaluations
     */
    private void maxEvaluationsChecker(UnivariateRealOptimizer optimizer,
                                       UnivariateRealFunction function,
                                       GoalType goal, double min, double max, int maxEvals) {

        int initialEvaluations = optimizer.getEvaluations();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = (MultiStartUnivariateRealOptimizer) optimizer;

        int starts = multiStartOptimizer.getStarts();
        int totalMaxEvaluations = starts * (1 + maxEvals);

        for (int i = 0; i < starts; i++) {
            optimizer.optimize(function, goal, min, max);
        }

        Assert.assertTrue("Expected " + totalMaxEvaluations + " evaluations but got " +
                (optimizer.getEvaluations() - initialEvaluations),
                (optimizer.getEvaluations() - initialEvaluations) >= totalMaxEvaluations);
    }

}