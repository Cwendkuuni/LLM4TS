package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.interpolation.InterpolatingPolynomial;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test cases for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting with initial guess.
     */
    @Test
    public void testFitWithInitialGuess() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.0, 1.0),
            new WeightedObservedPoint(3.0, 4.0, 2.0),
            new WeightedObservedPoint(3.5, 0.5, 3.0),
            new WeightedObservedPoint(4.0, 0.1, 4.0),
            new WeightedObservedPoint(4.5, 0.02, 5.0),
            new WeightedObservedPoint(5.0, 0.0, 10.0) };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] p = fitter.fit(new double[] { 10.0, 3.0, 1.0 });

        assertEquals(3.0, p[0], 1e-8);
        assertEquals(3.5, p[1], 1e-8);
        assertEquals(0.531, p[2], 1e-8);
    }

    /**
     * Test fitting without initial guess.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.0, 1.0),
            new WeightedObservedPoint(3.0, 4.0, 2.0),
            new WeightedObservedPoint(3.5, 0.5, 3.0),
            new WeightedObservedPoint(4.0, 0.1, 4.0),
            new WeightedObservedPoint(4.5, 0.02, 5.0),
            new WeightedObservedPoint(5.0, 0.0, 10.0) };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] p = fitter.fit();

        assertEquals(3.0, p[0], 1e-8);
        assertEquals(3.5, p[1], 1e-8);
        assertEquals(0.531, p[2], 1e-8);
    }

    /**
     * Test guessing parameters.
     */
    @Test
    public void testGuessParameters() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.0, 1.0),
            new WeightedObservedPoint(3.0, 4.0, 2.0),
            new WeightedObservedPoint(3.5, 0.5, 3.0),
            new WeightedObservedPoint(4.0, 0.1, 4.0),
            new WeightedObservedPoint(4.5, 0.02, 5.0),
            new WeightedObservedPoint(5.0, 0.0, 10.0) };

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(data);
        double[] p = guesser.guess();

        assertEquals(3.0, p[0], 1e-8);
        assertEquals(3.5, p[1], 1e-8);
        assertEquals(0.531, p[2], 1e-8);
    }

    /**
     * Test fitting with incorrect data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithIncorrectData() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[0];
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(data);
    }

    /**
     * Test fitting with insufficient data.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithInsufficientData() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] { new WeightedObservedPoint(0.0, 0.0) };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(data);
    }

    /**
     * Test fitting with too many data points.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithTooManyDataPoints() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.0, 1.0),
            new WeightedObservedPoint(3.0, 4.0, 2.0),
            new WeightedObservedPoint(3.5, 0.5, 3.0),
            new WeightedObservedPoint(4.0, 0.1, 4.0),
            new WeightedObservedPoint(4.5, 0.02, 5.0),
            new WeightedObservedPoint(5.0, 0.0, 10.0),
            new WeightedObservedPoint(6.0, 0.0, 10.0) };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(data);
    }

    /**
     * Test fitting with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuess() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.0, 1.0),
            new WeightedObservedPoint(3.0, 4.0, 2.0),
            new WeightedObservedPoint(3.5, 0.5, 3.0),
            new WeightedObservedPoint(4.0, 0.1, 4.0),
            new WeightedObservedPoint(4.5, 0.02, 5.0),
            new WeightedObservedPoint(5.0, 0.0, 10.0) };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 0.0, 0.0, 0.0 };
        fitter.fit(initialGuess);
    }

    /**
     * Test interpolation.
     */
    @Test
    public void testInterpolation() throws MaxCountExceededException, OutOfRangeException {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.0, 1.0),
            new WeightedObservedPoint(3.0, 4.0, 2.0),
            new WeightedObservedPoint(3.5, 0.5, 3.0),
            new WeightedObservedPoint(4.0, 0.1, 4.0),
            new WeightedObservedPoint(4.5, 0.02, 5.0),
            new WeightedObservedPoint(5.0, 0.0, 10.0) };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] p = fitter.fit();
        double mean = p[1];
        double sigma = p[2];

        UnivariateInterpolator interpolator = new InterpolatingPolynomial(5);
        for (WeightedObservedPoint point : data) {
            interpolator.interpolate(point.getX());
        }

        double x1 = mean - 3 * sigma;
        double y1 = interpolator.value(x1);
        double x2 = mean + 3 * sigma;
        double y2 = interpolator.value(x2);
        double x3 = mean;
        double y3 = interpolator.value(x3);

        assertEquals(y1, Gaussian.Parametric.value(x1, p), 1e-8);
        assertEquals(y2, Gaussian.Parametric.value(x2, p), 1e-8);
        assertEquals(y3, Gaussian.Parametric.value(x3, p), 1e-8);

        StandardDeviation stdDev = new StandardDeviation();
        double[] yValues = new double[interpolator.getN()];
        for (int i = 0; i < yValues.length; i++) {
            yValues[i] = interpolator.value(i - interpolator.getIntervalSize() / 2.0);
        }
        double stdDeviation = stdDev.evaluate(yValues);
        assertEquals(sigma, stdDeviation, 1e-6);
    }
}