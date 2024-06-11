package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.parametric.ParametricFunction;
import org.apache.commons.math3.analysis.parametric.ParametricUnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GaussianFitterTest {

    @Test
    public void testFitGaussianFunction() {
        // Test data
        UnivariateFunction function = new Gaussian(1.0, 0.5);
        double[] xData = {1.2, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5};
        double[] yData = {4.54, 2.85, 1.51, 0.54, 0.11, 0.03, -0.21};

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        // Test fit method
        double[] parameters = fitter.fit();
        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.length);

        // Test result verification
        ParametricFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-6;
        for (int i = 0; i < xData.length; i++) {
            double expected = function.value(xData[i], parameters);
            double calculated = gaussian.value(xData[i], parameters);
            Assert.assertEquals(expected, calculated, tolerance);
        }
    }

    @Test
    public void testFitGaussianFunctionWithInitialGuess() {
        // Test data
        UnivariateFunction function = new Gaussian(1.0, 0.5);
        double[] xData = {1.2, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5};
        double[] yData = {4.54, 2.85, 1.51, 0.54, 0.11, 0.03, -0.21};
        double[] initialGuess = {2.0, 2.5, 0.5};

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        // Test fit method with initial guess
        double[] parameters = fitter.fit(initialGuess);
        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.length);

        // Test result verification
        ParametricFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-6;
        for (int i = 0; i < xData.length; i++) {
            double expected = function.value(xData[i], parameters);
            double calculated = gaussian.value(xData[i], parameters);
            Assert.assertEquals(expected, calculated, tolerance);
        }

        // Test initial guess verification
        Assert.assertArrayEquals(initialGuess, parameters, tolerance);
    }

    @Test
    public void testFitGaussianFunctionWithBounds() {
        // Test data
        UnivariateFunction function = new Gaussian(1.0, 0.5);
        double[] xData = {1.2, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5};
        double[] yData = {4.54, 2.85, 1.51, 0.54, 0.11, 0.03, -0.21};
        double lowerBound = 0.0;
        double upperBound = 5.0;

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        // Test bounds
        SimpleBounds bounds = new SimpleBounds(lowerBound, upperBound);
        fitter.setBounds(bounds);

        // Test fit method
        double[] parameters = fitter.fit();
        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.length);

        // Test result verification
        ParametricFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-6;
        for (int i = 0; i < xData.length; i++) {
            double expected = function.value(xData[i], parameters);
            double calculated = gaussian.value(xData[i], parameters);
            Assert.assertEquals(expected, calculated, tolerance);
        }

        // Test bounds verification
        SimpleBounds fitBounds = fitter.getBounds();
        Assert.assertEquals(lowerBound, fitBounds.getMin(), tolerance);
        Assert.assertEquals(upperBound, fitBounds.getMax(), tolerance);
    }

    @Test
    public void testFitGaussianFunctionWithInvalidInitialGuess() {
        // Test data
        double[] xData = {};
        double[] yData = {};
        double[] initialGuess = {0.0, -1.0, 0.0};

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method
        try {
            for (int i = 0; i < xData.length; i++) {
                fitter.addObservedPoint(xData[i], yData[i]);
            }
            fitter.fit(initialGuess);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFitGaussianFunctionWithInvalidObservedPoints() {
        // Test data
        double[] xData = {};
        double[] yData = {0.0, 1.0, 2.0};

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method with empty xData
        try {
            for (int i = 0; i < xData.length; i++) {
                fitter.addObservedPoint(xData[i], yData[i]);
            }
            fitter.fit();
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test fitting with invalid yData
        try {
            double[] observations = new double[3][2];
            observations[0] = new double[]{Double.NaN, yData[0]};
            observations[1] = new double[]{xData[0], Double.POSITIVE_INFINITY};
            observations[2] = new double[]{xData[1], yData[1]};
            fitter.setObservations(Collections.unmodifiableList(Arrays.asList(observations)));
            fitter.fit();
            Assert.fail();
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testFitGaussianFunctionWithZeroVariance() {
        // Test data
        double[] xData = {1.2, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5};
        double[] yData = {4.54, 2.85, 1.51, 0.54, 0.11, 0.03, -0.21};
        double[] initialGuess = {2.0, 2.5, 0.0};

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        // Test fit method with zero variance
        try {
            fitter.fit(initialGuess);
            Assert.fail();
        } catch (ZeroException e) {
            // Expected
        }

        // Test result verification
        ParametricFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-6;
        for (int i = 0; i < xData.length; i++) {
            double expected = function.value(xData[i], initialGuess);
            double calculated = gaussian.value(xData[i], initialGuess);
            Assert.assertEquals(expected, calculated, tolerance);
        }

        // Test initial guess verification
        Assert.assertArrayEquals(initialGuess, fitter.getInitialGuess(), tolerance);
    }

    @Test
    public void testFitGaussianFunctionWithInvalidInitialBounds() {
        // Test data
        double[] xData = {1.2, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5};
        double[] yData = {4.54, 2.85, 1.51, 0.54, 0.11, 0.03, -0.21};
        double lowerBound = Double.NEGATIVE_INFINITY;
        double upperBound = 0.0;

        // Test initialization
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Assert.assertNotNull(fitter);

        // Test addObservedPoint method
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        // Test bounds
        SimpleBounds bounds = new SimpleBounds(lowerBound, upperBound);
        try {
            fitter.setBounds(bounds);
            fitter.fit();
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test result verification
        ParametricFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-6;
        for (int i = 0; i < xData.length; i++) {
            double expected = function.value(xData[i], fitter.getParameters());
            double calculated = gaussian.value(xData[i], fitter.getParameters());
            Assert.assertEquals(expected, calculated, tolerance);
        }

        // Test bounds verification
        SimpleBounds fitBounds = fitter.getBounds();
        Assert.assertEquals(0.0, fitBounds.getMin(), tolerance);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fitBounds.getMax(), tolerance);
    }
}