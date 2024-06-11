package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.Maximize;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.Minimize;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.PolynomialFunctionWithDerivative;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.UnivariateFunction;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMinimization() throws Exception {
        UnivariateFunction f = new PolynomialFunctionWithDerivative(new PolynomialFunction(1, 2, -1, 1, 1), x -> 1L);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new MersenneTwister());

        GoalType goalType = new Minimize();
        double min = 2.0;
        double max = 4.0;

        PointValuePair result = optimizer.optimize(f, goalType, min, max);

        assertFalse(Double.isNaN(result.getPoint()));
        assertFalse(Double.isNaN(result.getValue()));
        assertEquals(E.E_E, result.getValue(), 1e-15);
        assertEquals(2.0, result.getPoint(), 1e-15);

        PointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(2.0, E.E_E)}, optima);

        optimizer.setMaxEvaluations(20);
        optima = optimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(1.99999999999999999, 1.99999999999999999E-15),
                new PointValuePair(2.00000000000000001, 1.99999999999999999E-15),
                new PointValuePair(2.0, E.E_E)}, optima);
    }

    @Test
    public void testMaximization() throws Exception {
        UnivariateFunction f = new PolynomialFunctionWithDerivative(new PolynomialFunction(1, 2, -1, 1, 1), x -> -1L);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new MersenneTwister());

        GoalType goalType = new Maximize();
        double min = 2.0;
        double max = 4.0;

        PointValuePair result = optimizer.optimize(f, goalType, min, max);

        assertFalse(Double.isNaN(result.getPoint()));
        assertFalse(Double.isNaN(result.getValue()));
        assertEquals(E.E_E, result.getValue(), 1e-15);
        assertEquals(4.0, result.getPoint(), 1e-15);

        PointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(4.0, E.E_E)}, optima);

        optimizer.setMaxEvaluations(20);
        optima = optimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(4.00000000000000001, 1.99999999999999999E-15),
                new PointValuePair(4.0, E.E_E),
                new PointValuePair(3.99999999999999999, 1.99999999999999999E-15)}, optima);
    }

    @Test
    public void testNoConvergence() throws Exception {
        UnivariateFunction f = x -> Math.pow(Math.E, Math.abs(x)) - 1;
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new MersenneTwister());

        GoalType goalType = new Minimize();
        double min = -5.0;
        double max = 5.0;

        try {
            optimizer.optimize(f, goalType, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            PointValuePair[] optima = optimizer.getOptima();
            assertArrayEquals(new PointValuePair[10], optima);
            for (PointValuePair optimum : optima) {
                assertNull(optimum.getPoint());
                assertNull(optimum.getValue());
            }
        }
    }
}