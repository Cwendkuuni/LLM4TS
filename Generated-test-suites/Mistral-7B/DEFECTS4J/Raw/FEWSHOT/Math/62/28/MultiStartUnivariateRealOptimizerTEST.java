package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMultistart;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethodOptimizer;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, 1.0}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new FibonacciOptimizer()), 10, new Random());

        optimizer.setMaxEvaluations(100);

        PointValuePair best = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertArrayEquals(new double[]{3.0, 2.0}, best.getPoint(), 0.001);
        assertArrayEquals(new double[]{-0.61803398875}, best.getValue(), 0.001);

        PointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new double[]{3.0, 2.0}, optima[0].getPoint(), 0.001);
        assertArrayEquals(new double[]{-0.61803398875}, optima[0].getValue(), 0.001);
        assertNull(optima[1]);
        assertArrayEquals(new double[]{1.0, 1.0}, optima[2].getPoint(), 0.001);
        assertArrayEquals(new double[]{-1.1313708532}, optima[2].getValue(), 0.001);
        assertNull(optima[3]);
        assertArrayEquals(new double[]{5.5}, optima[4].getPoint(), 0.001);
        assertArrayEquals(new double[]{2.1013141516}, optima[4].getValue(), 0.001);
        assertArrayEquals(new double[]{7.0}, optima[5].getPoint(), 0.001);
        assertArrayEquals(new double[]{1.6487212707}, optima[5].getValue(), 0.001);
        assertNull(optima[6]);
        assertArrayEquals(new double[]{0.0}, optima[7].getPoint(), 0.001);
        assertArrayEquals(new double[]{1.0}, optima[7].getValue(), 0.001);
        assertNull(optima[8]);
        assertArrayEquals(new double[]{6.0}, optima[9].getPoint(), 0.001);
        assertArrayEquals(new double[]{2.6123797884}, optima[9].getValue(), 0.001);
    }

    @Test
    public void testMultiStartMaximization() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, 1.0}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new FibonacciOptimizer()), 10, new Random());

        optimizer.setMaxEvaluations(100);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        PointValuePair best = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        assertArrayEquals(new double[]{3.0, 2.0}, best.getPoint(), 0.001);
        assertArrayEquals(new double[]{1.6487212707}, best.getValue(), 0.001);

        PointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new double[]{3.0, 2.0}, optima[0].getPoint(), 0.001);
        assertArrayEquals(new double[]{1.6487212707}, optima[0].getValue(), 0.001);
        assertNull(optima[1]);
        assertArrayEquals(new double[]{1.0, 1.0}, optima[2].getPoint(), 0.001);
        assertArrayEquals(new double[]{-1.1313708532}, optima[2].getValue(), 0.001);
        assertNull(optima[3]);
        assertArrayEquals(new double[]{5.5}, optima[4].getPoint(), 0.001);
        assertArrayEquals(new double[]{-2.1013141516}, optima[4].getValue(), 0.001);
        assertNull(optima[5]);
        assertArrayEquals(new double[]{0.0}, optima[6].getPoint(), 0.001);
        assertArrayEquals(new double[]{1.0}, optima[6].getValue(), 0.001);
        assertNull(optima[7]);
        assertArrayEquals(new double[]{6.0}, optima[8].getPoint(), 0.001);
        assertArrayEquals(new double[]{-2.6123797884}, optima[8].getValue(), 0.001);
        assertNull(optima[9]);
        assertArrayEquals(new double[]{7.0}, optima[10].getPoint(), 0.001);
        assertArrayEquals(new double[]{-1.6487212707}, optima[10].getValue(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new FibonacciOptimizer()), 10, new Random());

        optimizer.setMaxEvaluations(10);

        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergenceWithMultipleStarts() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new FibonacciOptimizer()), 10, new Random());

        optimizer.setMaxEvaluations(10 * 10);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0, 0.5);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergenceWithMaxEvaluations() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new FibonacciOptimizer()), 10, new Random());

        optimizer.setMaxEvaluations(5);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOneStart() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0}, new double[]{0.0});
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new SimpleBoundingMethodOptimizer<>(new FibonacciOptimizer()), 0, new Random());
    }
}