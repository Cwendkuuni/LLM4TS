package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciMinimizer;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair;

public class MultiStartUnivariateRealOptimizerTest {

    private static final int MAX_EVALUATIONS = 100;
    private static final int NUM_STARTS = 3;

    @SuppressWarnings("unchecked")
    private static UnivariateFunction f(final double a, final double b) {
        return new PolynomialFunction(new double[]{-b, 3 * a * b * b + 2 * a * b - a}, new int[]{1, 2});
    }

    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
        Random random = new Random();
        UnivariateRealFunction func = (x) -> Math.pow(Math.sin(x), 2);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciMinimizer(), NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        UnivariateRealPointValuePair result = optimizer.optimize(func, GoalType.MINIMIZE, -5.0, 5.0);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        // Check that optimization was successful
        assertNotNull(result);
        double expectedMin = FastMath.PI / 2.0;
        double tolerance = 1e-8;
        assertTrue(FastMath.abs(result.getPoint() - expectedMin) < tolerance);
        assertTrue(FastMath.abs(result.getValue() - func.value(expectedMin)) < tolerance);

        // Check that multiple starts all converged to the same point
        for (UnivariateRealPointValuePair op : optima) {
            if (op != null) {
                assertTrue(FastMath.abs(op.getPoint() - expectedMin) < tolerance);
                assertTrue(FastMath.abs(op.getValue() - func.value(expectedMin)) < tolerance);
            }
        }

        // Check that correct number of evaluations were performed
        assertEquals(MAX_EVALUATIONS, optimizer.getEvaluations());

        // Test with a function that does not converge
        func = (x) -> Math.sqrt(Math.abs(x));
        optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethod(), NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        try {
            optimizer.optimize(func, GoalType.MINIMIZE, -5.0, 5.0);
            fail("Should have thrown a ConvergenceException");
        } catch (ConvergenceException ce) {
            // Expected
        }

        // Test with a function that converges only for some starts
        func = f;
        optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciMinimizer(), NUM_STARTS, random);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        result = optimizer.optimize(func, GoalType.MINIMIZE, -5.0, 5.0);
        optima = optimizer.getOptima();

        // Check that some starts converged and some did not
        int numConverged = 0;
        for (UnivariateRealPointValuePair op : optima) {
            if (op != null) {
                numConverged++;
            }
        }
        assertEquals(NUM_STARTS / 2, numConverged);
    }

    private void assertNotNull(UnivariateRealPointValuePair result) {
        if (result == null) {
            fail("Expected optimization to converge but it did not");
        }
    }

    private void assertEquals(int expected, int actual) {
        assertEquals(expected, actual);
    }
}