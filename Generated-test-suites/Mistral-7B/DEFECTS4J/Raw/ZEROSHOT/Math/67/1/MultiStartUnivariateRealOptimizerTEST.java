package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.UnivariateFunction;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Test case for the {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimize method with function f(x) = x^2.
     */
    @org.junit.Test
    public void testOptimizeXSquare() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0}, new double[] {2.0});
        MaxEval maxEval = new MaxEval(1000);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearch(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEval.getMaxEvaluations());
        optimizer.setMaxIterations(maxEval.getMaxIterations());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimize method with function f(x) = 1/x.
     */
    @org.junit.Test
    public void testOptimizeReciprocalX() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0},
                new double[] {-1.0, 0.5});
        MaxEval maxEval = new MaxEval(1000);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEval.getMaxEvaluations());
        optimizer.setMaxIterations(maxEval.getMaxIterations());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(1.0 / Math.sqrt(2.0), result.getPoint(), 1e-6);
        assertEquals(1.0 / (2.0 * Math.sqrt(2.0)), result.getValue(), 1e-6);
    }

    /**
     * Test optimize method with function f(x) = x^3.
     */
    @org.junit.Test
    public void testOptimizeXCube() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0}, new double[] {2.0, 3.0});
        MaxEval maxEval = new MaxEval(1000);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new AroundMinimum(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEval.getMaxEvaluations());
        optimizer.setMaxIterations(maxEval.getMaxIterations());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(-1.0, result.getPoint(), 1e-12);
        assertEquals(1.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimize method with function f(x) = x^2 - 2 * x + 1.
     */
    @org.junit.Test
    public void testOptimizeQuadraticFunction() {
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -2.0, 1.0}, new double[] {});
        MaxEval maxEval = new MaxEval(1000);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearch(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEval.getMaxEvaluations());
        optimizer.setMaxIterations(maxEval.getMaxIterations());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0);
        assertEquals(-1.0, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimize method with function f(x) = sin(x).
     */
    @org.junit.Test
    public void testOptimizeSin() {
        UnivariateFunction f = new PolynomialFunction(new double[] {Math.sin}, new double[] {});
        MaxEval maxEval = new MaxEval(1000);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearch(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEval.getMaxEvaluations());
        optimizer.setMaxIterations(maxEval.getMaxIterations());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -Math.PI, Math.PI);
        assertEquals(0.0, result.getValue(), 1e-12);

        // Check that the results are close to the expected values
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(5, optima.length);
        for (int i = 0; i < 5; i++) {
            double expected = Math.PI / 2.0 + Math.PI * i / 2.0;
            assertEquals(expected, optima[i], 1e-6);
            assertEquals(Math.sin(expected), optimaValues[i], 1e-6);
        }
    }

    /**
     * Test optimize method with function f(x) = cos(x).
     */
    @org.junit.Test
    public void testOptimizeCos() {
        UnivariateFunction f = new PolynomialFunction(new double[] {Math.cos}, new double[] {});
        MaxEval maxEval = new MaxEval(1000);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearch(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEval.getMaxEvaluations());
        optimizer.setMaxIterations(maxEval.getMaxIterations());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -Math.PI, Math.PI);
        assertEquals(0.0, result.getValue(), 1e-12);

        // Check that the results are close to the expected values
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(5, optima.length);
        for (int i = 0; i < 5; i++) {
            double expected = Math.PI / 2.0 + Math.PI * i / 2.0;
            assertEquals(expected, optima[i], 1e-6);
            assertEquals(Math.cos(expected), optimaValues[i], 1e-6);
        }
    }
}