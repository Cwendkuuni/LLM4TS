package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeInterval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.junit.Test;

import org.apache.commons.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with a simple function and default settings.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 2.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;
        double expectedMin = -3.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        try {
            PointValuePair result = optimizerMS.optimize(func, goal, startMin, startMax);
            assertTrue(result != null);
            assertEquals(expectedMin, result.getValue(), 1e-6);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test optimization with a simple function and different starting points.
     */
    @Test
    public void testOptimizeSimpleFunctionMultiStart() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 2.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;
        double expectedMin = -3.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        try {
            PointValuePair[] results = optimizerMS.optimize(func, goal, startMin, startMax, 0);
            assertTrue(results != null);
            assertEquals(expectedMin, results[0].getValue(), 1e-6);
            for (int i = 1; i < results.length; i++) {
                assertNull(results[i]);
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test optimization with a function that has multiple local minimums.
     */
    @Test
    public void testOptimizeFunctionWithMultipleLocalMinima() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 4.0, -1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;
        double expectedMin1 = -3.0;
        double expectedMin2 = 3.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        try {
            PointValuePair[] results = optimizerMS.optimize(func, goal, startMin, startMax, 0);
            assertTrue(results != null);
            assertEquals(expectedMin1, results[0].getValue(), 1e-6);
            for (int i = 1; i < results.length; i++) {
                if (i != results.length - 1) {
                    assertEquals(expectedMin2, results[i].getValue(), 1e-6);
                } else {
                    assertNull(results[i]);
                }
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test optimization with a function that has no solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeFunctionWithNoSolution() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        optimizerMS.optimize(func, goal, startMin, startMax, 0);
    }

    /**
     * Test setting and getting the convergence checker.
     */
    @Test
    public void testSetAndGetConvergenceChecker() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 2.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        ConvergenceChecker<PointValuePair> convergenceChecker = new MyConvergenceChecker();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        optimizerMS.setConvergenceChecker(convergenceChecker);
        ConvergenceChecker<PointValuePair> getConvergenceChecker = optimizerMS.getConvergenceChecker();
        assertEquals(convergenceChecker, getConvergenceChecker);
    }

    /**
     * Test setting the maximum number of evaluations.
     */
    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 2.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        optimizerMS.setMaxEvaluations(100);
        assertEquals(100, optimizerMS.getMaxEvaluations());
        optimizerMS.setMaxEvaluations(50);
        assertEquals(50, optimizerMS.getMaxEvaluations());
    }

    /**
     * Test getting the number of evaluations.
     */
    @Test
    public void testGetEvaluations() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 2.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        optimizerMS.optimize(func, goal, startMin, startMax, 0);
        int evaluations = optimizerMS.getEvaluations();
        assertTrue(evaluations > 0);
    }

    /**
     * Test getting the optima.
     */
    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 2.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        PointValuePair[] results = optimizerMS.optimize(func, goal, startMin, startMax, 0);
        PointValuePair[] optima = optimizerMS.getOptima();
        assertArrayEquals(results, optima);
    }

    /**
     * Test getting the optima with no solution.
     */
    @Test
    public void testGetOptimaNoSolution() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        GoalType goal = GoalType.MINIMIZE;
        double startMin = -10.0;
        double startMax = 10.0;

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DifferentialEvolution(new SimpleBoundingMethod(), new Random());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());

        try {
            optimizerMS.optimize(func, goal, startMin, startMax, 0);
            PointValuePair[] optima = optimizerMS.getOptima();
            assertFalse(optima.length > 0);
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Custom convergence checker for testing.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean isConverged(PointValuePair currentValue, PointValuePair previousValue) {
            return currentValue.getValue() == previousValue.getValue();
        }
    }

}