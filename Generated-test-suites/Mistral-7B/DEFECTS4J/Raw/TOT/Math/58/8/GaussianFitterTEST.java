package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.stat.descript.Statistics;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /** Optimizer for fitting. */
    private DifferentiableMultivariateVectorialOptimizer optimizer;
    /** Fitter for testing. */
    private GaussianFitter fitter;
    /** Observed points for testing. */
    private WeightedObservedPoint[] points;

    /**
     * Sets up test environment.
     */
    @Before
    public void setUp() {
        optimizer = new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        fitter = new GaussianFitter(optimizer);

        double[] x = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982,
            4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] y = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0,
            2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };
        points = new WeightedObservedPoint[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = new WeightedObservedPoint(x[i], y[i], 1.0);
        }
    }

    /**
     * Tests fitting with given initial guess.
     */
    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = { 1.0, 4.0, 0.5 };
        double[] result = fitter.fit(initialGuess);
        double[] expected = { 516395.8888888888, 4.04435228240952, 0.13360108636629 };
        assertArrayEquals(expected, result, 1e-6);
    }

    /**
     * Tests fitting without initial guess.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        double[] result = fitter.fit();
        double[] expected = { 516395.8888888888, 4.04435228240952, 0.13360108636629 };
        assertArrayEquals(expected, result, 1e-6);
    }

    /**
     * Tests that fitting fails when there are less than three points.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithTooFewPoints() {
        WeightedObservedPoint[] points = { new WeightedObservedPoint(1.0, 1.0, 1.0) };
        fitter.fit(points);
    }

    /**
     * Tests that fitting fails when initial guess is not positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNegativeInitialGuess() {
        double[] initialGuess = { -1.0, 4.0, 0.5 };
        fitter.fit(initialGuess);
    }

    /**
     * Tests that fitting with given initial guess returns the same result as
     * fitting without an initial guess.
     */
    @Test
    public void testFitWithAndWithoutInitialGuessAreEqual() {
        double[] resultWithInitialGuess = fitter.fit(new double[] { 1.0, 4.0, 1.0 });
        double[] resultWithoutInitialGuess = fitter.fit();
        assertArrayEquals(resultWithInitialGuess, resultWithoutInitialGuess, 1e-6);
    }

    /**
     * Tests that mean is correct.
     */
    @Test
    public void testMean() {
        double[] result = fitter.fit();
        double expected = 4.04435228240952;
        assertEquals(expected, result[1], 1e-6);
    }

    /**
     * Tests that variance is correct.
     */
    @Test
    public void testVariance() {
        double[] result = fitter.fit();
        double expected = 0.013360108636629;
        assertEquals(expected, result[2], 1e-6);
    }

    /**
     * Tests that standard deviation is correct.
     */
    @Test
    public void testStandardDeviation() {
        double[] result = fitter.fit();
        double expected = 0.11565315893299;
        assertEquals(expected, Statistics.standardDeviation(points), 1e-6);
    }

    /**
     * Tests that fitting a function to non-Gaussian data results in NaN.
     */
    @Test
    public void testFitNonGaussianData() {
        double[] x = { 1.0, 2.0, 3.0 };
        double[] y = { 1.0, 4.0, 9.0 };
        WeightedObservedPoint[] points = new WeightedObservedPoint[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = new WeightedObservedPoint(x[i], y[i], 1.0);
        }
        try {
            fitter.fit(points);
            fail("Expected IllegalArgumentException or ArrayIndexOutOfBoundsException");
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // expected
        }
        UnivariateFunction function = fitter.getFittedFunction();
        assertNotNull(function);
        double[] testValues = { -1.0, 0.0, 1.0, 2.0, 3.0 };
        double[] testFunctionValues = new double[testValues.length];
        for (int i = 0; i < testValues.length; i++) {
            testFunctionValues[i] = Double.NaN;
        }
        PointValuePairList result = fitter.optimize(new UnivariateFunction() {
            public double value(double x) {
                return function.value(x);
            }

            public double gradient(double x) {
                return function.gradient(x);
            }
        }, testValues);
        for (int i = 0; i < testValues.length; i++) {
            assertEquals(Double.NaN, result.getPoint(i), 0.0);
            assertEquals(Double.NaN, result.getValue(i), 0.0);
        }
    }
}