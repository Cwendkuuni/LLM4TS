package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ScaledVariableBisection;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.points.ExtremeValueLocator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test minimization with a polynomial function.
     */
    @Test
    public void testMinimizePolynomial() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Quartic(0.1, 0.3, -3.0, 1.0, -2.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());
        optimizer.setMaxEvaluations(30);

        // Call optimize method.
        PointValuePair result = null;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Check result.
        Assert.assertNotNull(result);
        Assert.assertEquals(1.1496551924642412E-3, result.getValue(), 1.0e-6);

        // Check optima.
        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(10, optima.length);
        Assert.assertNotNull(optima[0]);
        Assert.assertEquals(1.1496551924642412E-3, optima[0].getValue(), 1.0e-6);
        for (int i = 1; i < optima.length; i++) {
            Assert.assertNull(optima[i]);
        }
    }

    /**
     * Test maximization with a quadratic function.
     */
    @Test
    public void testMaximizeQuadratic() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Quadratic(1.0, 2.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());
        optimizer.setMaxEvaluations(30);

        // Call optimize method.
        PointValuePair result = null;
        try {
            result = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Check result.
        Assert.assertNotNull(result);
        Assert.assertEquals(5.5, result.getValue(), 1.0e-6);

        // Check optima.
        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(10, optima.length);
        Assert.assertNotNull(optima[optima.length - 1]);
        Assert.assertEquals(5.5, optima[optima.length - 1].getValue(), 1.0e-6);
        for (int i = 0; i < optima.length - 1; i++) {
            Assert.assertNull(optima[i]);
        }
    }

    /**
     * Test invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Quartic(0.1, 0.3, -3.0, 1.0, -2.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());

        // Call optimize method with an invalid goal type.
        optimizer.optimize(f, GoalType.INVALID, -10.0, 10.0);
    }

    /**
     * Test invalid function.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidFunction() {
        // Set up test data.
        UnivariateRealFunction f = null;
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());

        // Call optimize method.
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with a constant function.
     */
    @Test
    public void testConstantFunction() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Const(1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());
        optimizer.setMaxEvaluations(30);

        // Call optimize method.
        PointValuePair result = null;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Check result.
        Assert.assertNotNull(result);
        Assert.assertEquals(1.0, result.getValue(), 1.0e-6);

        // Check optima.
        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(10, optima.length);
        Assert.assertNotNull(optima[0]);
        Assert.assertEquals(1.0, optima[0].getValue(), 1.0e-6);
        for (int i = 1; i < optima.length; i++) {
            Assert.assertNull(optima[i]);
        }
    }

    /**
     * Test maximization with a function having no extremum in the given interval.
     */
    @Test
    public void testNoExtremum() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Linear(1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());
        optimizer.setMaxEvaluations(30);

        // Call optimize method.
        PointValuePair result = null;
        try {
            result = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.assertFalse(e instanceof ConvergenceException);
            Assert.assertNull(result);
        }

        // Check optima.
        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(10, optima.length);
        for (int i = 0; i < optima.length; i++) {
            Assert.assertNull(optima[i]);
        }
    }

    /**
     * Test optimization with a function having multiple extremum in the given interval.
     */
    @Test
    public void testMultipleExtrema() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Quadratic(1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());
        optimizer.setMaxEvaluations(30);

        // Call optimize method.
        PointValuePair result = null;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Check result.
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.0, result.getValue(), 1.0e-6);

        // Check optima.
        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(10, optima.length);
        Assert.assertNotNull(optima[0]);
        Assert.assertEquals(-1.0, optima[0].getValue(), 1.0e-6);
        for (int i = 1; i < optima.length; i++) {
            Assert.assertNotNull(optima[i]);
            List<Double> x = optima[i].getPoint();
            Assert.assertTrue(x.get(0) >= -4.9 && x.get(0) <= 4.9);
            Assert.assertTrue(Math.abs(f.value(x.get(0)) - result.getValue()) <= 1.0e-6);
        }
    }

    /**
     * Test optimization with a function having no minimum in the given interval.
     */
    @Test
    public void testNoMinimum() {
        // Set up test data.
        UnivariateRealFunction f = new PolynomialFunctions.Linear(-1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
            new ScaledVariableBisection(), 10, new SimpleRandomGenerator());
        optimizer.setMaxEvaluations(30);

        // Call optimize method.
        PointValuePair result = null;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        } catch (Exception e) {
            Assert.assertFalse(e instanceof ConvergenceException);
            Assert.assertNull(result);
        }

        // Check optima.
        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(10, optima.length);
        for (int i = 0; i < optima.length; i++) {
            Assert.assertNull(optima[i]);
        }
    }
}