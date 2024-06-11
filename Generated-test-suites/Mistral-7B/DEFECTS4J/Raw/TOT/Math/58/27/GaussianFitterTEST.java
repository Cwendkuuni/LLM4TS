package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Tests for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a simple set of points.
     */
    @Test
    public void testSimpleFit() {
        double[] dataX = { 0.0, 0.5, 1.0, 1.5, 2.0 };
        double[] dataY = { 1.0, 1.5, 2.0, 2.5, 3.0 };
        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new BrentOptimizer());
        fitter.addObservedPoints(points);
        double[] params = fitter.fit();
        assertEquals(3, params.length);
        double mean = params[1];
        double stdDev = params[2];
        assertEquals(1.5, mean, 0.01);
        assertEquals(0.5, stdDev, 0.01);
    }

    /**
     * Test fitting a set of points with no solution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoSolution() {
        double[] dataX = { 0.0, 0.5, 1.0, 1.5, 2.0 };
        double[] dataY = { 1.0, 1.4, 1.9, 2.5, 3.0 };
        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new BrentOptimizer());
        fitter.addObservedPoints(points);
        fitter.fit();
    }

    /**
     * Test fitting a set of points with wrong initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongInitialGuess() {
        double[] dataX = { 0.0, 0.5, 1.0, 1.5, 2.0 };
        double[] dataY = { 1.0, 1.5, 2.0, 2.5, 3.0 };
        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new BrentOptimizer());
        double[] initialGuess = new double[] { 1.0, 0.5, -1.0 };
        fitter.fit(initialGuess);
    }

    /**
     * Test fitting a set of points with non-positive initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonPositiveInitialGuess() {
        double[] dataX = { 0.0, 0.5, 1.0, 1.5, 2.0 };
        double[] dataY = { 1.0, 1.5, 2.0, 2.5, 3.0 };
        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new BrentOptimizer());
        double[] initialGuess = new double[] { 0.0, 1.0, 0.0 };
        fitter.fit(initialGuess);
    }

    /**
     * Test calculating the standard deviation of a Gaussian function.
     */
    @Test
    public void testStandardDeviation() {
        double mean = 0.5;
        double stdDev = 0.5;
        ParametricUnivariateRealFunction function = new Gaussian(mean, stdDev);
        double[] x = new double[] { mean - 3.0, mean - 2.0, mean - 1.0, mean,
                                    mean + 1.0, mean + 2.0, mean + 3.0 };
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = function.value(x[i]);
        }
        WeightedObservedPoint[] points = new WeightedObservedPoint[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = new WeightedObservedPoint(x[i], y[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new BrentOptimizer());
        fitter.addObservedPoints(points);
        double[] params = fitter.fit();
        assertEquals(mean, params[1], 0.01);
        assertEquals(stdDev, params[2], 0.01);
    }

    /**
     * Test the {@code GaussianFitter.ParameterGuesser} class.
     */
    @Test
    public void testParameterGuesser() {
        double[] dataX = { 0.0, 1.0, 2.0, 3.0, 4.0 };
        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            double x = dataX[i];
            double y = Math.exp(-Math.pow(x, 2) / 2.0);
            points[i] = new WeightedObservedPoint(x, y, 1.0);
        }
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] params = guesser.guess();
        assertEquals(3, params.length);
        assertEquals(2.0, params[0], 0.01);
        assertEquals(1.0, params[1], 0.01);
        assertEquals(1.0, params[2], 0.01);
    }
}