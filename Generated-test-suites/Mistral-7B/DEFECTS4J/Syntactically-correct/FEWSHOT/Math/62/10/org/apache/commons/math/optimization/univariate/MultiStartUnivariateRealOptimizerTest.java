package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.point.UnivariateRealPointValuePair;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.cli.optim.MultiStartUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private static UnivariateOptimizer<ObjectiveFunction> getDummyOptimizer() {
        return (UnivariateOptimizer<ObjectiveFunction>) new FoolAdam();
    }

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        ObjectiveFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                getDummyOptimizer(), 5, new Random());
        optimizer.setConvergenceChecker(new SimpleBounds(0.001, Double.POSITIVE_INFINITY));
        UnivariateRealPointValuePair best = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertNull(best);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizerWithOptima() {
        ObjectiveFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                getDummyOptimizer(), 5, new Random());
        optimizer.setConvergenceChecker(new SimpleBounds(0.001, Double.POSITIVE_INFINITY));
        UnivariateRealPointValuePair[] optima = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.5);
        assertArrayEquals(new UnivariateRealPointValuePair[] {
            new UnivariateRealPointValuePair(0.0, -1.0),
            new UnivariateRealPointValuePair(3.0, 2.25),
            new UnivariateRealPointValuePair(6.0, 1.0),
            new UnivariateRealPointValuePair(7.0, 0.75),
            new UnivariateRealPointValuePair(10.0, 0.0)
        }, optima);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizerWithMaxEvaluations() {
        ObjectiveFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                getDummyOptimizer(), 5, new Random());
        optimizer.setConvergenceChecker(new SimpleBounds(0.001, Double.POSITIVE_INFINITY));
        optimizer.setMaxEvaluations(10);
        UnivariateRealPointValuePair[] optima = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.5);
        assertArrayEquals(new UnivariateRealPointValuePair[] {
            new UnivariateRealPointValuePair(0.0, -1.0), // Optima from the first start
            null, null, null, null, null // Optima from the other starts
        }, optima);
        assertTrue(optimizer.getEvaluations() >= 10);
    }

    @Test
    public void testMultiStartUnivariateRealOptimizerWithInvalidNumberOfStarts() {
        ObjectiveFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                getDummyOptimizer(), 0, new Random());
        optimizer.setConvergenceChecker(new SimpleBounds(0.001, Double.POSITIVE_INFINITY));
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.5);
            fail("Expected MathIllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}