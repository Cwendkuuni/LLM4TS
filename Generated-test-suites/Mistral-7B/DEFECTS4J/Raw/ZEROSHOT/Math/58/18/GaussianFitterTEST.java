package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.points.PointValues;
import org.apache.commons.math3.optim.points.RealVectorValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a Gaussian function to a set of observed points.
     */
    @Test
    public void testFit() {
        final double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        final double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        final NonlinearOptimizer optimizer = new LevenbergMarquardtOptimizer();
        final PointValues initialGuess = new AveragedStartingPointGenerator(3).generateInitialGuess(2.0, 3.0, 1.0);
        final double[] parameters = fitter.fit(initialGuess.toVector());

        assertNotNull(parameters);
        assertEquals(3, parameters.length);

        final Gaussian g = new Gaussian(parameters[0], parameters[1], parameters[2]);
        final double[] testX = { 3.0, 4.0, 5.0, 6.0 };
        final double[] testY = new double[testX.length];
        for (int i = 0; i < testX.length; i++) {
            testY[i] = g.value(testX[i]);
        }

        assertArrayEquals(new double[] { 2255893.54567206, 4.03676235936125, 16627882.62224316, 27596684.41789104 }, testY, YData.clone(), 0.0001);
    }

    /**
     * Test fitting a Gaussian function to a set of observed points with no data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyData() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit();
    }

    /**
     * Test fitting a Gaussian function with null data.
     */
    @Test(expected = NullArgumentException.class)
    public void testFitNullData() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setObservations(null);
        fitter.fit();
    }

    /**
     * Test fitting a Gaussian function with invalid data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitInvalidData() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setObservations(new WeightedObservedPoint[0]);
        fitter.addObservedPoint(4.0, 5.0);
        fitter.fit();
    }

    /**
     * Test fitting a Gaussian function with a null optimizer.
     */
    @Test(expected = NullPointerException.class)
    public void testFitNullOptimizer() {
        final GaussianFitter fitter = new GaussianFitter(null);
        fitter.addObservedPoint(4.0, 5.0);
        fitter.fit();
    }

    /**
     * Test guessing the parameters of a Gaussian function.
     */
    @Test
    public void testGuess() {
        final double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        final double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        final double[] parameters = fitter.guess();

        assertNotNull(parameters);
        assertEquals(3, parameters.length);

        final Gaussian g = new Gaussian(parameters[0], parameters[1], parameters[2]);
        final double[] testX = { 3.0, 4.0, 5.0, 6.0 };
        final double[] testY = new double[testX.length];
        for (int i = 0; i < testX.length; i++) {
            testY[i] = g.value(testX[i]);
        }

        assertArrayEquals(new double[] { 2255893.54567206, 4.03676235936125, 16627882.62224316 }, testY, YData.clone(), 0.0001);
    }

    /**
     * Test guessing the parameters of a Gaussian function with empty data.
     */
    @Test
    public void testGuessEmptyData() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        final double[] parameters = fitter.guess();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    /**
     * Test guessing the parameters of a Gaussian function with null data.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessNullData() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setObservations(null);
        fitter.guess();
    }

    /**
     * Test guessing the parameters of a Gaussian function with invalid data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessInvalidData() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setObservations(new WeightedObservedPoint[0]);
        fitter.guess();
    }

    /**
     * Test guessing the parameters of a Gaussian function with a null optimizer.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessNullOptimizer() {
        final GaussianFitter fitter = new GaussianFitter(null);
        fitter.guess();
    }

    /**
     * Test the construction of a {@link GaussianFitter} instance.
     */
    @Test
    public void testConstruction() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        assertNotNull(fitter);
    }

    /**
     * Test the construction of a {@link GaussianFitter} instance with null optimizer.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructionNullOptimizer() {
        final GaussianFitter fitter = new GaussianFitter(null);
    }

    /**
     * Data used for testing.
     */
    private static final double[] YData = { 2255893.54567206, 4.03676235936125, 16627882.62224316 };

}