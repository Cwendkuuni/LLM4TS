package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.simple.SimpleValuesIterator;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math.optimization.fitting.GaussianFitter;

/**
 * Test class for GaussianFitter.
 */
public class GaussianFitterTest {

    // Test instance
    private GaussianFitter fitter;

    // Test data
    private double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408, 4.08825107 };
    private double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0, 564076.0 };

    @Before
    public void setUp() {
        // Setup the test instance with default optimizer
        fitter = new GaussianFitter(new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer());

        // Add observed points
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }
    }

    // Test case for fit(initialGuess) method

    @Test(expected = NullArgumentException.class)
    public void testFitInitialGuessNull() {
        fitter.fit(null);
    }

    @Test
    public void testFitInitialGuess() {
        double[] initialGuess = new double[] { 2.0, 3.0, 1.0 };
        double[] result = fitter.fit(initialGuess);
        double[] expected = new double[] { 1000.0, 3.5, 0.5 };
        assertArrayEquals(expected, result);
    }

    // Test case for fit() method

    @Test(expected = NullArgumentException.class)
    public void testFitNull() {
        fitter.fit();
    }

    @Test
    public void testFit() {
        double[] result = fitter.fit();
        double[] expected = new double[] { 1000.0, 3.5, 0.5 };
        assertArrayEquals(expected, result);
    }

    // Test case for ParameterGuesser class

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserNull() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] result = guesser.guess();
        double[] expected = new double[] { 1000.0, 3.5, 0.5 };
        assertArrayEquals(expected, result);
    }

    // Test case for isBetween method in getInterpolationPointsForY

    @Test
    public void testIsBetween() {
        double value = 3.0;
        double boundary1 = 1.0;
        double boundary2 = 5.0;
        assertTrue(isBetween(value, boundary1, boundary2));
        value = 5.0;
        assertTrue(isBetween(value, boundary1, boundary2));
        value = 2.0;
        assertFalse(isBetween(value, boundary1, boundary2));
        value = 6.0;
        assertFalse(isBetween(value, boundary1, boundary2));
    }

    // Test case for findMaxY method

    @Test
    public void testFindMaxY() {
        double[] data = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        int expected = 4;
        int result = findMaxY(data);
        assertEquals(expected, result);
    }

    // Test case for interpolateXAtY method

    @Test(expected = ZeroException.class)
    public void testInterpolateXAtYZero() {
        interpolateXAtY(xData, 0, 0, 3.5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInterpolateXAtYOutOfRange() {
        interpolateXAtY(xData, 0, 1, -1.0);
    }

    @Test
    public void testInterpolateXAtY() {
        double result = interpolateXAtY(xData, 0, 1, 3.5);
        double expected = 4.03128248;
        assertEquals(expected, result, 1e-6);
    }

    // Test case for fit method in GaussianFitter

    @Test
    public void testFit_GaussianFunction() {
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuess = new double[] { 1000.0, 3.5, 0.5 };
        PointValuePair result = fitter.fit(function, initialGuess);
        double[] expected = new double[] { 1000.0, 3.5, 0.5 };
        assertArrayEquals(expected, result.getPoint(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testFit_NullFunction() {
        fitter.fit(null, new double[3]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_NegativeInitialGuess() {
        double[] initialGuess = new double[] { 1000.0, -3.5, -0.5 };
        fitter.fit(new Gaussian.Parametric(), initialGuess);
    }

    private int findMaxY(double[] data) {
        int maxYIdx = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i] > data[maxYIdx]) {
                maxYIdx = i;
            }
        }
        return maxYIdx;
    }

    private double interpolateXAtY(double[] xData, int startIdx, int idxStep, double y)
            throws OutOfRangeException {
        WeightedObservedPoint[] points = new WeightedObservedPoint[xData.length];
        System.arraycopy(new WeightedObservedPoint[] { new WeightedObservedPoint(xData[0], yData[0], 1.0) }, 0, points, 0, 1);
        System.arraycopy(new WeightedObservedPoint[] { new WeightedObservedPoint(xData[xData.length - 1], yData[yData.length - 1], 1.0) }, 1, points, 1, xData.length - 1);
        SimpleValuesIterator<WeightedObservedPoint> it = new SimpleValuesIterator<>(points);
        int i = startIdx;
        do {
            WeightedObservedPoint point = it.next();
            if (point.getY() == y) {
                return point.getX();
            }
            i++;
        } while (i < points.length - 1 && idxStep > 0 || i > 0 && idxStep < 0);
        throw new OutOfRangeException(y, Double.MIN_VALUE, Double.MAX_VALUE);
    }

}