package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// Add necessary imports for WeightedObservedPoint, DifferentiableMultivariateVectorialOptimizer, GaussianFitter, and ParameterGuesser if not already imported

public class GaussianFitterTest {

    private WeightedObservedPoint[] observations;
    private GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        observations = new WeightedObservedPoint[] { new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                                                      new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
                                                      new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
                                                      new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
                                                      new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
                                                      new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
                                                      new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
                                                      new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
                                                      new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
                                                      new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
                                                      new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
                                                      new WeightedObservedPoint(4.08366408, 620014.0, 1.0),
                                                      new WeightedObservedPoint(4.08888889, 432100.0, 1.0) };
    }

    @Test
    public void testGaussianFitting() {
        double[] expectedParameters = { 1000.0, 4.055, 0.1 };
        double[] result = fitter.fit(expectedParameters);
        Assert.assertArrayEquals(expectedParameters, result, 0.001);

        PointValuePair pvp = fitter.findParameterMinimum();
        Assert.assertEquals(expectedParameters[0], pvp.getPoint()[0], 0.001);
        Assert.assertEquals(expectedParameters[1], pvp.getPoint()[1], 0.001);
        Assert.assertEquals(expectedParameters[2], pvp.getValue(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObservations() {
        fitter.fit(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEmptyObservations() {
        fitter.fit(new double[0]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidYValue() {
        observations[0].setY(Double.NEGATIVE_INFINITY);
        fitter.addObservedPoint(observations[0]);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Invalid Y value: " + Double.NEGATIVE_INFINITY, e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSigmaValue() {
        observations[0].setParameter(2, -1.0);
        fitter.addObservedPoint(observations[0]);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Sigma must be strictly positive: -1.0", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialGuess() {
        double[] initialGuess = { -1.0, 4.055, -1.0 };
        try {
            fitter.fit(initialGuess);
        } catch (Exception e) {
            Assert.assertEquals("Invalid initial guess: expected array having 3 elements but found array of size: 3",
                               e.getMessage());
        }
    }
}