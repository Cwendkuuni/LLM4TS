package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultistartUnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultistartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test function for optimization.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }
    };

    /**
     * Test function for optimization with bounds.
     */
    private static final UnivariateRealFunction TEST_FUNCTION_BOUNDED = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            if (x <= 0) {
                throw new FunctionEvaluationException(x, Double.POSITIVE_INFINITY);
            }
            if (x >= 10) {
                throw new FunctionEvaluationException(x, Double.NEGATIVE_INFINITY);
            }
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }
    };

    /**
     * Test optimization with multiple starts and no bounds.
     */
    @Test
    public void testMultiStartOptimizationNoBounds() throws Exception {
        // configure optimizer
        UnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.Simplex();
        MultistartUnivariateRealOptimizer multiStartOptimizer = new MultistartUnivariateRealOptimizer(optimizer, 10, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        multiStartOptimizer.setMaxIterations(1000);

        // optimization
        double optimum = multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertEquals(Math.PI, optimum, 0.0001); // approximate value
        PointValuePair result = multiStartOptimizer.getOptima(); // get all the optima
        Assert.assertEquals(10, result.getDimension()); // number of starts
        double[] optima = result.getPoint();
        double[] optimaValues = result.getValue();
        for (int i = 0; i < 10; i++) {
            Assert.assertFalse(Double.isNaN(optima[i]));
            Assert.assertFalse(Double.isNaN(optimaValues[i]));
            Assert.assertFalse(Math.abs(optima[i] - Math.PI) > 0.01);
            Assert.assertFalse(Math.abs(optimaValues[i] - Math.PI * Math.PI) > 0.01);
        }
    }

    /**
     * Test optimization with multiple starts and bounds.
     */
    @Test
    public void testMultiStartOptimizationWithBounds() throws Exception {
        // configure optimizer
        UnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.BoundedBrent();
        MultistartUnivariateRealOptimizer multiStartOptimizer = new MultistartUnivariateRealOptimizer(optimizer, 10, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        multiStartOptimizer.setMaxIterations(1000);

        // optimization
        double optimum = multiStartOptimizer.optimize(TEST_FUNCTION_BOUNDED, GoalType.MINIMIZE, 0.0, 10.0);
        Assert.assertEquals(1.31185357126, optimum, 0.0001); // approximate value
        PointValuePair result = multiStartOptimizer.getOptima(); // get all the optima
        Assert.assertEquals(10, result.getDimension()); // number of starts
        double[] optima = result.getPoint();
        double[] optimaValues = result.getValue();
        for (int i = 0; i < 10; i++) {
            Assert.assertFalse(Double.isNaN(optima[i]));
            Assert.assertFalse(Double.isNaN(optimaValues[i]));
            Assert.assertEquals(1.31185357126, optima[i], 0.0001);
            Assert.assertEquals(1.31185357126 * 1.31185357126, optimaValues[i], 0.0001);
        }
        // Test error cases
        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, Double.NaN, Double.NaN);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, Double.NaN);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, null, 0.0, 0.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, 0.0, -1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, 0.0, 0, 1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, 0.0, 0, 1, -1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, 0.0, 0, 1, 1, -1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 0.0, 0.0, 0.0, 0, 1, 1, 1, -1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

    }

    /**
     * Test optimization with multiple starts and invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartOptimizationInvalidGoalType() throws Exception {
        // configure optimizer
        UnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.Simplex();
        MultistartUnivariateRealOptimizer multiStartOptimizer = new MultistartUnivariateRealOptimizer(optimizer, 10, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        multiStartOptimizer.setMaxIterations(1000);

        multiStartOptimizer.optimize(TEST_FUNCTION, null, 0.0, 0.0);
    }
}