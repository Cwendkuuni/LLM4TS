package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.optimize.LevenbergMarquardtOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.math3.optim.Points.*;
import static org.apache.commons.math3.optim.fitting.WeightedObservedPoint.WEIGHT_ONE;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    private GaussianFitter fitter;
    private WeightedObservedPoint[] points;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        points = new WeightedObservedPoint[]{
                new WeightedObservedPoint(4.0254623, 531026.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.03128248, 984167.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.03839603, 1887233.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.04421621, 2687152.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.05132976, 3461228.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.05326982, 3580526.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.05779662, 3439750.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.0636168, 2877648.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.06943698, 2175960.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.07525716, 1447024.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.08237071, 717104.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.08366408, 620014.0, WEIGHT_ONE)};
    }

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = new double[]{50000.0, 4.0, 1.0};
        double[] result = fitter.fit(initialGuess);

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        double[] result = fitter.fit();

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservedPoints() {
        fitter.fit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservedPoints() {
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithLessThanThreeObservedPoints() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[]{
                new WeightedObservedPoint(4.0254623, 531026.0, WEIGHT_ONE),
                new WeightedObservedPoint(4.03128248, 984167.0, WEIGHT_ONE)};
        fitter.fit(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNonDifferentiableFunction() {
        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double... p) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double x, double... p) {
                throw new UnsupportedOperationException();
            }
        };

        fitter.fit(f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNonOptimizableFunction() {
        DifferentiableMultivariateVectorFunction f = new DifferentiableMultivariateVectorFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };

        fitter.fit(f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNonPositiveInitialGuess() {
        double[] initialGuess = new double[]{-1.0, 4.0, 1.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNegativeInitialGuessNorm() {
        double[] initialGuess = new double[]{-100.0, 4.0, 1.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeInitialGuessSigma() {
        double[] initialGuess = new double[]{50000.0, 4.0, -1.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNonPositiveInitialGuessMean() {
        double[] initialGuess = new double[]{50000.0, -4.0, 1.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitWithOutOfRangeInitialGuessNorm() {
        double[] initialGuess = new double[]{Double.POSITIVE_INFINITY, 4.0, 1.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitWithOutOfRangeInitialGuessMean() {
        double[] initialGuess = new double[]{50000.0, Double.POSITIVE_INFINITY, 1.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitWithOutOfRangeInitialGuessSigma() {
        double[] initialGuess = new double[]{50000.0, 4.0, Double.POSITIVE_INFINITY};
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitWithZeroInitialGuessNorm() {
        double[] initialGuess = new double[]{0.0, 4.0, 1.0};
        double[] result = fitter.fit(initialGuess);

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testFitWithZeroInitialGuessMean() {
        double[] initialGuess = new double[]{50000.0, 0.0, 1.0};
        double[] result = fitter.fit(initialGuess);

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testFitWithZeroInitialGuessSigma() {
        double[] initialGuess = new double[]{50000.0, 4.0, 0.0};
        double[] result = fitter.fit(initialGuess);

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testFitWithInitialGuessCloseToExpectedResult() {
        double[] initialGuess = new double[]{51176.799, 4.053825, 0.1559286};
        double[] result = fitter.fit(initialGuess);

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testFitWithInitialGuessFarFromExpectedResult() {
        double[] initialGuess = new double[]{1000.0, 10.0, 2.0};
        double[] result = fitter.fit(initialGuess);

        double[] expected = {51176.7993219031, 4.05382544838636, 0.15592865534552};

        Assert.assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testGuessParameters() {
        ParameterGuesser guesser = new ParameterGuesser(points);
        double[] result = guesser.guess();

        double[] expected = {36316.645353784, 4.06125292132136, 0.26377582431478};

        Assert.assertArrayEquals(expected, result, 0.001);
    }
}