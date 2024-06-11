package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.simple.WeighedObservedPoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private double[] initialGuess;
    private double[] expectedResult;
    private double[] observationsX;
    private double[] observationsY;
    private WeightedObservedPoint[] observations;

    public GaussianFitterTest(double[] initialGuess, double[] expectedResult, double[] observationsX, double[] observationsY) {
        this.initialGuess = initialGuess;
        this.expectedResult = expectedResult;
        this.observationsX = observationsX;
        this.observationsY = observationsY;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{1.0, 2.0, 1.0}, new double[]{3.0, 2.0, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {3.5, 3.5}},
                        {new double[]{3.0, 2.0, 1.0}, new double[]{3.0, 3.0, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {3.5, 3.5}},
                {new double[]{1.0, 2.0, 1.0}, new double[]{2.5, 2.5, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {3.5, 3.5}},
                {new double[]{1.0, 2.0, 1.0}, new double[]{2.5, 2.5, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {3.5, 2.5}},
                {new double[]{1.0, 2.0, 1.0}, new double[]{2.5, 2.5, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {3.5, 4.5}},
                {new double[]{1.0, 2.0, 1.0}, new double[]{2.5, 2.5, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {3.5, 5.0}},
                {new double[]{1.0, 2.0, 1.0}, new double[]{2.5, 2.5, 1.0}, {1.5, 2.5}, {2.0, 3.5}, {4.5, 5.0}}
        });
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(new WeighedObservedPoint(observationsX[0], observationsY[0], 1.0));
        // Add other observations
        double[] result = fitter.fit(initialGuess);
        Assert.assertArrayEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] negativeInitialGuess = {-1.0, 2.0, 1.0};
        fitter.fit(negativeInitialGuess);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitZeroInitialGuessNorm() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] zeroInitialGuess = {0.0, 2.0, 1.0};
        fitter.fit(zeroInitialGuess);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitZeroInitialGuessMean() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] zeroInitialGuess = {1.0, 0.0, 1.0};
        fitter.fit(zeroInitialGuess);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitZeroInitialGuessSigma() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] zeroInitialGuess = {1.0, 2.0, 0.0};
        fitter.fit(zeroInitialGuess);
    }
}