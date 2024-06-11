package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.optimization.GoalType;
import org.apache.commons.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.RandomUtils;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        assertNotNull(optimizerMultiStart);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setConvergenceChecker(ConvergenceChecker)} and {@link MultiStartUnivariateRealOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testSetAndGetConvergenceChecker() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        optimizerMultiStart.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizerMultiStart.getConvergenceChecker());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        int maxEvaluations = optimizerMultiStart.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, maxEvaluations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        int evaluations = optimizerMultiStart.getEvaluations();
        assertEquals(0, evaluations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        optimizerMultiStart.setMaxEvaluations(10);
        assertEquals(10, optimizerMultiStart.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test(expected = AssertionError.class)
    public void testGetOptimaBeforeOptimize() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        optimizerMultiStart.getOptima();
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * and {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testOptimizeAndGetOptima() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        optimizerMultiStart.setMaxEvaluations(50);
        UnivariateRealPointValuePair result = optimizerMultiStart.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        UnivariateRealPointValuePair[] optima = optimizerMultiStart.getOptima();
        assertEquals(-1.61803398875, result.getPoint(), 0.001);
        assertEquals(-2.0, optima[0].getPoint(), 0.001);
        assertTrue(Precision.isClose(1.0, optima[1].getValue(), 0.01));
        assertTrue(Precision.isClose(2.0, optima[2].getPoint(), 0.001));
        assertNull(optima[3]);
        assertNull(optima[4]);
        assertNull(optima[5]);
        assertNull(optima[6]);
        assertNull(optima[7]);
        assertNull(optima[8]);
        assertNull(optima[9]);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * and {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testOptimizeAndGetOptimaWithStartValue() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        optimizerMultiStart.setMaxEvaluations(50);
        UnivariateRealPointValuePair result = optimizerMultiStart.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 5.0);
        UnivariateRealPointValuePair[] optima = optimizerMultiStart.getOptima();
        assertEquals(5.384615384615385, result.getPoint(), 0.001);
        assertEquals(5.0, optima[0].getPoint(), 0.001);
        assertEquals(4.0, optima[1].getPoint(), 0.001);
        assertTrue(Precision.isClose(2.0, optima[2].getPoint(), 0.01));
        assertTrue(Precision.isClose(3.0, optima[3].getPoint(), 0.01));
        assertNull(optima[4]);
        assertNull(optima[5]);
        assertNull(optima[6]);
        assertNull(optima[7]);
        assertNull(optima[8]);
        assertNull(optima[9]);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * when the optimization fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        optimizerMultiStart.setMaxEvaluations(5);
        optimizerMultiStart.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * when the optimization fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergenceWithStartValue() {
        UnivariateRealFunction f = new PolynomialFunction(-1.0, 2.0, 1.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(1.0, 1.0);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new Random());
        optimizerMultiStart.setMaxEvaluations(5);
        optimizerMultiStart.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 5.0);
    }
}