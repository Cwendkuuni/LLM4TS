package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.OptimizationException;
import org.apache.commons.math3.optim.differentiable.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optim.differentiable.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.fitting.CurveFitter;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;
import org.apache.commons.math3.optim.nonlinear.Converter;
import org.apache.commons.math3.optim.nonlinear.converters.DenseVectorConverter;
import org.apache.commons.math3.optim.nonlinear.functions.ParametricFunction;
import org.apache.commons.math3.optim.nonlinear.functions.Gaussian;
import org.apache.commons.math3.optim.nonlinear.vector.dense.DenseVector;
import org.apache.commons.math3.stat.Statistics;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test constant values.
     */
    private static final double EPSILON = 1e-10;
    private static final double[] TEST_DATA = {4.0254623, 531026.0,
        4.03128248, 984167.0, 4.03839603, 1887233.0,
        4.04421621, 2687152.0, 4.05132976, 3461228.0,
        4.05326982, 3580526.0, 4.05779662, 3439750.0,
        4.0636168, 2877648.0, 4.06943698, 2175960.0,
        4.07525716, 1447024.0, 4.08237071, 717104.0,
        4.08366408, 620014.0};

    /**
     * Test constructor with null optimizer.
     */
    @Test(expected = NullPointerException.class)
    public void testGaussianFitterNullOptimizer() {
        new GaussianFitter(null);
    }

    /**
     * Test constructor with valid optimizer.
     */
    @Test
    public void testGaussianFitter() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        CurveFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit(TEST_DATA);
        double[] expected = {1277523.335331, 4.066766, 0.024529};
        Assert.assertArrayEquals(expected, result, EPSILON);
    }

    /**
     * Test fit method with null initialGuess.
     */
    @Test(expected = NullPointerException.class)
    public void testGaussianFitterNullInitialGuess() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null);
    }

    /**
     * Test fit method with too few initial guesses.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitterTooFewInitialGuesses() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[2];
        fitter.fit(initialGuess);
    }

    /**
     * Test fit method with valid initialGuess.
     */
    @Test
    public void testGaussianFitterValidInitialGuess() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = {1300000.0, 4.067, 0.025};
        double[] result = fitter.fit(initialGuess);
        double[] expected = {1277523.335331, 4.066766, 0.024529};
        Assert.assertArrayEquals(expected, result, EPSILON);
    }

    /**
     * Test fit method with fit method without initialGuess.
     */
    @Test
    public void testGaussianFitterFit() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }
        fitter.addObservations(observations);
        double[] result = fitter.fit();
        double[] expected = {1277523.335331, 4.066766, 0.024529};
        Assert.assertArrayEquals(expected, result, EPSILON);
    }

    /**
     * Test fit method with empty observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitterEmptyObservations() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new double[0][0]);
    }

    /**
     * Test fit method with null observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitterNullObservations() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null);
    }

    /**
     * Test fit method with invalid observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitterInvalidObservations() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        observations[0] = new WeightedObservedPoint(TEST_DATA[0], 0.0);
        fitter.addObservations(observations);
    }

    /**
     * Test fit method with observations having zero weight.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitterZeroWeightObservations() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        observations[0] = new WeightedObservedPoint(TEST_DATA[0], 0.0);
        fitter.addObservations(observations);
    }

    /**
     * Test optimizer with NaN initialGuess.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testGaussianFitterNaNInitialGuess() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = {Double.NaN, 4.066766, 0.024529};
        fitter.fit(initialGuess);
    }

    /**
     * Test optimizer with NaN observed point.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGaussianFitterNaNObservedPoint() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length + 1];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }
        observations[TEST_DATA.length] = new WeightedObservedPoint(Double.NaN, 1.0);
        fitter.addObservations(observations);
    }

    /**
     * Test optimizer with observed points out of range.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGaussianFitterOutOfRangeObservedPoints() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length + 1];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }
        observations[TEST_DATA.length] = new WeightedObservedPoint(Double.POSITIVE_INFINITY, 1.0);
        fitter.addObservations(observations);
    }

    /**
     * Test optimizer with initialGuess not strictly positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testGaussianFitterNotStrictlyPositiveInitialGuess() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = {-1.0, 4.066766, 0.024529};
        fitter.fit(initialGuess);
    }

    /**
     * Test optimizer with observations not strictly positive.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGaussianFitterNotStrictlyPositiveObservedPoints() {
        DifferentiableMultivariateVectorOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length + 1];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }
        observations[TEST_DATA.length] = new WeightedObservedPoint(-1.0, 1.0);
        fitter.addObservations(observations);
    }
}