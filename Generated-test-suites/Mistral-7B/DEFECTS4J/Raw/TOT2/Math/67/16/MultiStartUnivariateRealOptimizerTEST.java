package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomValueGenerator;
import org.apache.commons.math3.special.Polynomials;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimizeSingleStart() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        UnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 1, new Random());

        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0);
        double result = 0.0;
        try {
            result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown when optimizing a simple polynomial with single start.");
        }

        double expected = Polynomials.root(2, -1.0);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    public void testOptimizeMultiStart() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        UnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new Random());

        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0);
        double[] optima = new double[10];
        double[] optimaValues = new double[10];

        for (int i = 0; i < 10; i++) {
            try {
                optima[i] = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
                optimaValues[i] = multiStartOptimizer.getFunctionValue();
            } catch (ConvergenceException | FunctionEvaluationException e) {
                optima[i] = Double.NaN;
                optimaValues[i] = Double.NaN;
            }
        }

        double expected = Polynomials.root(2, -1.0);
        for (int i = 0; i < 10; i++) {
            if (!Double.isNaN(optima[i])) {
                assertEquals(expected, optima[i], 1e-10);
                assertEquals(expected, optimaValues[i], 1e-10);
            }
        }
    }

    @Test
    public void testOptimizeFunctionWithMultipleLocalMinima() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        UnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new Random());

        UnivariateRealFunction function = (x) -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        double[] optima = new double[10];
        double[] optimaValues = new double[10];

        for (int i = 0; i < 10; i++) {
            try {
                optima[i] = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0);
                optimaValues[i] = multiStartOptimizer.getFunctionValue();
            } catch (ConvergenceException | FunctionEvaluationException e) {
                optima[i] = Double.NaN;
                optimaValues[i] = Double.NaN;
            }
        }

        double expected1 = 0.0;
        double expected2 = 1.57079632679489661;

        for (int i = 0; i < 10; i++) {
            if (!Double.isNaN(optima[i])) {
                if (Precision.isClose(expected1, optima[i], 1e-12)) {
                    assertEquals(expected1, optima[i], 1e-12);
                    assertEquals(expected1, optimaValues[i], 1e-12);
                } else if (Precision.isClose(expected2, optima[i], 1e-12)) {
                    assertEquals(expected2, optima[i], 1e-12);
                    assertEquals(expected2, optimaValues[i], 1e-12);
                } else {
                    fail("Unexpected optimization result.");
                }
            }
        }
    }

    @Test
    public void testOptimizeFunctionWithMaxEvalExceeded() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        UnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new Random());

        UnivariateRealFunction function = (x) -> Math.pow(Math.sin(x / 0.1), 2);
        try {
            multiStartOptimizer.setMaxEvaluations(1);
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
            fail("Function evaluation exception should have been thrown.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizeFunctionWithIterationLimitExceeded() {
        UnivariateRealOptimizer optimizer = new FixedPointUnivariateRealOptimizer(0.01);
        UnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new Random());

        UnivariateRealFunction function = (x) -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        multiStartOptimizer.setMaxIterationCount(1);
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0);
            fail("Convergence exception should have been thrown.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}