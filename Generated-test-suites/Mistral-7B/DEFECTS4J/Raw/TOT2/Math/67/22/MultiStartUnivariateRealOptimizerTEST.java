package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.jgrad.JGradientOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.E;

import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testBasicUsage() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 });
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            new JGradientOptimizer(10), 5, new MersenneTwister());
        try {
            PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
            assertEquals(E.EULER_NUMBER, result.getValue(), 1e-8);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMaxIterations() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 });
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            new JGradientOptimizer(10), 5, new MersenneTwister());
        optimizer.setMaxIterations(1);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
            fail("Expected ConvergenceException not thrown.");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testMaxEvaluations() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 });
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            new JGradientOptimizer(10), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(2);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
            fail("Expected FunctionEvaluationException not thrown.");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testConvergenceException() {
        UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 });
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            new JGradientOptimizer(1), 5, new MersenneTwister());
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
            fail("Expected ConvergenceException not thrown.");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testIllegalArgumentException() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            null, 5, new MersenneTwister());
        try {
            optimizer.optimize(new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 }),
                GoalType.MINIMIZE, -1.0, 1.0);
            fail("Expected IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullGoalType() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            new JGradientOptimizer(10), 5, new MersenneTwister());
        try {
            optimizer.optimize(new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 }),
                null, -1.0, 1.0);
            fail("Expected IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullMinMax() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
            new JGradientOptimizer(10), 5, new MersenneTwister());
        try {
            optimizer.optimize(new PolynomialFunction(3, new double[] { 1.0, -2.0, 1.0 }),
                GoalType.MINIMIZE, null, null);
            fail("Expected IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}