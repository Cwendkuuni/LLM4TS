package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.BesselFunction;
import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimize;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximize;
import org.apache.commons.math3.optim.nonlinear.scalar.goose.GooseFminBroydenFletcherGoldfarbShanno;
import org.apache.commons.math3.optim.nonlinear.scalar.goose.GooseFminBroydenFletcherGoldfarbShanno.Options;
import org.apache.commons.math3.optim.nonlinear.scalar.goose.GooseFminBroydenFletcherGoldfarbShanno.Options.MaxIterations;
import org.apache.commons.math3.optim.nonlinear.scalar.goose.GooseFminBroydenFletcherGoldfarbShanno.Options.Precision;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Brent;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer.UnivariateRealPointValuePair;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMinimize() {
        final UnivariateFunction func = new PolynomialFunctions.Quartic(1.0, -1.0, 0.0, 1.0, 0.1);
        final GoalType goal = new GoalTypeMinimize();
        final double min = -10.0;
        final double max = 10.0;
        final int maxEvaluations = 50;
        final int starts = 3;

        final RandomGenerator rg = new Random();

        final MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Brent(), starts, rg);
        optimizer.setMaxEvaluations(maxEvaluations);

        final UnivariateRealPointValuePair[] optima = optimizer.optimize(func, goal, min, max);
        assertArrayEquals(new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(0.0, 0.0),
                                                               new UnivariateRealPointValuePair(-1.0, 1.0),
                                                               new UnivariateRealPointValuePair(1.0, 1.0) },
                optima);

        assertEquals(starts * maxEvaluations / 2, optimizer.getTotalEvaluations());
        assertNull(optimizer.getOptima());

        // Test with Quartic function with a different minimum
        final UnivariateFunction func2 = new PolynomialFunctions.Quartic(-1.0, 1.0, 0.0, 1.0, 0.1);
        optimizer.setOptimizer(new GooseFminBroydenFletcherGoldfarbShanno());
        optimizer.setMaxEvaluations(maxEvaluations * 2);
        optimizer.setStarts(starts);

        final UnivariateRealPointValuePair[] optima2 = optimizer.optimize(func2, goal, min, max);
        assertArrayEquals(new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(-1.0, 1.0),
                                                               new UnivariateRealPointValuePair(1.0, 1.0),
                                                               new UnivariateRealPointValuePair(0.0, 0.0) },
                optima2);

        assertEquals(starts * maxEvaluations, optimizer.getTotalEvaluations());
        assertArrayEquals(new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(-1.0, 1.0),
                                                               new UnivariateRealPointValuePair(1.0, 1.0),
                                                               new UnivariateRealPointValuePair(0.0, 0.0) },
                optimizer.getOptima());
    }

    @Test
    public void testMaximize() {
        final UnivariateFunction func = new BesselFunction(BesselFunction.BESSEL_K);
        final GoalType goal = new GoalTypeMaximize();
        final double min = 0.0;
        final double max = Math.PI / 2.0;
        final int maxEvaluations = 100;
        final int starts = 5;

        final MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Brent(), starts, new Random());
        optimizer.setMaxEvaluations(maxEvaluations);

        final UnivariateRealPointValuePair[] optima = optimizer.optimize(func, goal, min, max);
        assertArrayEquals(new UnivariateRealPointValuePair[] { new UnivariateRealPointValuePair(Math.PI / 2.0,
                                                                                                Math.PI / 2.0),
                                                                 new UnivariateRealPointValuePair(Math.PI / 4.0,
                                                                                                Math.PI / 4.0),
                                                                 new UnivariateRealPointValuePair(Math.PI / 8.0,
                                                                                                Math.PI / 8.0),
                                                                 new UnivariateRealPointValuePair(Math.PI / 16.0,
                                                                                                Math.PI / 16.0),
                                                                 new UnivariateRealPointValuePair(0.0, 0.0) },
                optima);

        assertEquals(starts * maxEvaluations / 2, optimizer.getTotalEvaluations());
        assertNull(optimizer.getOptima());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testNoConvergence() {
        final UnivariateFunction func = new PolynomialFunctions.Quartic(1.0, -1.0, 0.0, 1.0, 0.1);
        final GoalType goal = new GoalTypeMinimize();
        final double min = -10.0;
        final double max = 10.0;
        final int maxEvaluations = 50;
        final int starts = 0;

        final MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Brent(), starts, new Random());
        optimizer.setMaxEvaluations(maxEvaluations);

        optimizer.optimize(func, goal, min, max);
    }
}

class UnivariateRealPointValuePair implements PointValuePair<Double, Double> {
    private final Double point;
    private final Double value;

    public UnivariateRealPointValuePair(Double point, Double value) {
        this.point = point;
        this.value = value;
    }

    @Override
    public Double getPoint() {
        return point;
    }

    @Override
    public Double getValue() {
        return value;
    }
}