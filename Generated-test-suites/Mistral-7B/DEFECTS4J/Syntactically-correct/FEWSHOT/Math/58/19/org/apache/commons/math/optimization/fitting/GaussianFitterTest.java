package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.InterpolatingPolynomial;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test cases for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test that {@link GaussianFitter#fit(double[])} works correctly.
     */
    @Test
    public void testFit() {
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 3.5, 5.0, 6.5, 8.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(neworg.apache.commons.math.optim.nonlinear.DiffGradConjGradOptimizer());
        fitter.addObservations(observations);
        double[] result = fitter.fit();
        assertArrayEquals(new double[] { 5.5, 3.25, 0.625 }, result, 1e-5);

        // Test with different initial guess
        double[] initialGuess = new double[] { 4.0, 3.0, 1.0 };
        result = fitter.fit(initialGuess);
        assertEquals(5.5, result[0], 1e-5);
        assertEquals(3.25, result[1], 1e-5);
        assertEquals(0.625, result[2], 1e-5);
    }

    /**
     * Test that {@link GaussianFitter#fit()} works correctly.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 3.5, 5.0, 6.5, 8.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(neworg.apache.commons.math.optim.nonlinear.DiffGradConjGradOptimizer());
        fitter.addObservations(observations);
        double[] result = fitter.fit();
        assertArrayEquals(new double[] { 5.5, 3.25, 0.625 }, result, 1e-5);
    }

    /**
     * Test that {@link GaussianFitter#fit(DifferentiableMultivariateVectorialOptimizer)} works correctly.
     */
    @Test
    public void testFitWithCustomOptimizer() {
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 3.5, 5.0, 6.5, 8.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }
        DifferentiableMultivariateVectorialOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.closedform.GaussNewtonOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservations(observations);
        double[] result = fitter.fit();
        assertArrayEquals(new double[] { 5.5, 3.25, 0.625 }, result, 1e-5);
    }

    /**
     * Test that {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])} works correctly.
     */
    @Test
    public void testFitWithCustomFunction() throws Exception {
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 3.5, 5.0, 6.5, 8.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(neworg.apache.commons.math.optim.nonlinear.DiffGradConjGradOptimizer());
        fitter.addObservations(observations);
        double[] result = fitter.fit(function, null);

        assertArrayEquals(new double[] { 5.5, 3.25, 0.625 }, result, 1e-5);
    }

    /**
     * Test that {@link GaussianFitter#guess()} works correctly.
     */
    @Test
    public void testGuess() {
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 3.5, 5.0, 6.5, 8.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(neworg.apache.commons.math.optim.nonlinear.DiffGradConjGradOptimizer());
        fitter.addObservations(observations);
        ParameterGuesser guesser = fitter.new ParameterGuesser(observations);
        double[] result = guesser.guess();
        List<Double> expected = Arrays.asList(Precision.round(5.5), Precision.round(3.25), Precision.round(0.625));
        assertArrayEquals(expected.stream().mapToDouble(d -> d).toArray(), result, 1e-5);
    }
}