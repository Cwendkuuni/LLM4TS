package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;
import org.apache.commons.math3.optim.nonlinear.SimpleValueVector;
import org.apache.commons.math3.optim.nonlinear.vector.VectorValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    private GaussianFitter fitter;
    private double[] dataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
    private double[] dataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (int i = 0; i < dataX.length; i++) {
            fitter.addObservedPoint(dataX[i], dataY[i]);
        }
    }

    @Test
    public void testFitGaussianFunction() {
        double[] expectedParameters = {2.53262417, 4.03488134, 0.37979938};
        double[] parameters = fitter.fit();
        Assert.assertArrayEquals(expectedParameters, parameters, 0.0001);
    }

    @Test
    public void testFitDefaultGaussianFunction() {
        double[] parameters = fitter.fit();
        Assert.assertArrayEquals(new double[]{1.0, 0.0, 1.0}, parameters, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullOptimizer() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.fit();
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullInitialGuess() {
        double[] initialGuess = null;
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(initialGuess);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithLessThanThreePoints() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (int i = 0; i < observations.length; i++) {
            fitter.addObservedPoint(0.0, i);
        }
        fitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuess() {
        double[] initialGuess = {1.0, 2.0, -1.0};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitWithInvalidInitialGuessButFitsAnyway() {
        double[] initialGuess = {1.0, 2.0, -1.0};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(initialGuess);
        double[] parameters = fitter.fit();
        Assert.assertArrayNotEquals(initialGuess, parameters, 0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithEmptyObservationsArray() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit();
    }

    @Test
    public void testBasicGuess() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[3];
        observations[0] = new WeightedObservedPoint(3.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(4.0, 2.0, 2.0);
        observations[2] = new WeightedObservedPoint(5.0, 3.0, 3.0);
        ParameterGuesser parameterGuesser = new ParameterGuesser(observations);
        double[] parameters = parameterGuesser.guess();
        Assert.assertArrayEquals(new double[]{2.30940081, 4.0, 1.12838253}, parameters, 0.0001);
    }

    @Test
    public void testInterpolateXAtY() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[4];
        observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 4.0, 2.0);
        observations[2] = new WeightedObservedPoint(3.0, 9.0, 3.0);
        observations[3] = new WeightedObservedPoint(4.0, 12.0, 4.0);
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        double x = fitter.interpolateXAtY(2.5);
        Assert.assertEquals(3.5, x, 0.0001);
    }

    @Test(expected = ZeroException.class)
    public void testInterpolateXAtYWithZeroStep() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[4];
        observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 4.0, 2.0);
        observations[2] = new WeightedObservedPoint(3.0, 9.0, 3.0);
        observations[3] = new WeightedObservedPoint(4.0, 12.0, 4.0);
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        fitter.interpolateXAtY(0, 2.5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInterpolateXAtYWithOutOfRangeY() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[4];
        observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 4.0, 2.0);
        observations[2] = new WeightedObservedPoint(3.0, 9.0, 3.0);
        observations[3] = new WeightedObservedPoint(4.0, 12.0, 4.0);
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        fitter.interpolateXAtY(-1, 10.0);
    }

    @Test
    public void testFindMaxY() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[4];
        observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 4.0, 2.0);
        observations[2] = new WeightedObservedPoint(3.0, 9.0, 3.0);
        observations[3] = new WeightedObservedPoint(4.0, 12.0, 4.0);
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        int maxYIndex = fitter.findMaxY(observations);
        Assert.assertEquals(1, maxYIndex);
    }
}