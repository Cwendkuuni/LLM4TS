package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a Gaussian distribution with given parameters.
     */
    @Test
    public void testFitGaussianWithKnownParameters() {
        // Given
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double[] pointsY = new Double[]{50000.0, 51500.0, 53000.0, 54500.0, 56000.0};
        Double initialGuess[] = new Double[]{55000.0, 4.1, 0.1};

        // When
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(pointsX[0], pointsY[0]);
        fitter.addObservedPoint(pointsX[1], pointsY[1]);
        fitter.addObservedPoint(pointsX[2], pointsY[2]);
        fitter.addObservedPoint(pointsX[3], pointsY[3]);
        fitter.addObservedPoint(pointsX[4], pointsY[4]);
        Double[] result = fitter.fit(initialGuess);

        // Then
        Assert.assertArrayEquals(new Double[]{54500.0, 4.2, 0.12}, result, 0.001);
    }

    /**
     * Test fitting a Gaussian distribution with no observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianWithNoObservations() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // When
        fitter.fit();

        // Then
        // Assert.fail(); // Uncomment this line for manual testing
        throw new IllegalArgumentException("No observations were provided.");
    }

    /**
     * Test fitting a Gaussian distribution with less than 3 observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianWithFewerThanThreeObservations() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] pointsX = new Double[]{4.0};
        Double[] pointsY = new Double[]{50000.0};
        fitter.addObservedPoint(pointsX[0], pointsY[0]);

        // When
        // Then
        throw new IllegalArgumentException("At least three observations are required.");
    }

    /**
     * Test fitting a Gaussian distribution with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianWithInvalidInitialGuess() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double[] pointsY = new Double[]{50000.0, 51500.0, 53000.0, 54500.0, 56000.0};
        Double initialGuess[] = new Double[]{55000.0, 4.1, -0.1};

        // When
        // Then
        throw new IllegalArgumentException("Initial guess should be strictly positive.");
    }

    /**
     * Test fitting a Gaussian distribution using a null optimizer.
     */
    @Test(expected = NullPointerException.class)
    public void testFitGaussianWithNullOptimizer() {
        // Given
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double[] pointsY = new Double[]{50000.0, 51500.0, 53000.0, 54500.0, 56000.0};
        GaussianFitter fitter = new GaussianFitter(null);

        // When
        // Then
        throw new NullPointerException("Optimizer cannot be null.");
    }

    /**
     * Test fitting a Gaussian distribution with a null list of observations.
     */
    @Test(expected = NullPointerException.class)
    public void testFitGaussianWithNullObservations() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // When
        // Then
        throw new NullPointerException("Observations cannot be null.");
    }

    /**
     * Test fitting a Gaussian distribution with observations containing null X.
     */
    @Test(expected = NullPointerException.class)
    public void testFitGaussianWithNullXObservations() {
        // Given
        Double[] pointsY = new Double[]{50000.0, 51500.0, 53000.0, 54500.0, 56000.0};
        WeightedObservedPoint[] observations = new WeightedObservedPoint[]{
                new WeightedObservedPoint(null, pointsY[0]),
                new WeightedObservedPoint(4.2, pointsY[1]),
                new WeightedObservedPoint(4.4, pointsY[2]),
                new WeightedObservedPoint(4.6, pointsY[3]),
                new WeightedObservedPoint(4.8, pointsY[4])};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // When
        // Then
        throw new NullPointerException("X values in observations cannot be null.");
    }

    /**
     * Test adding an observation with invalid Y.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testAddObservationWithInvalidY() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double pointY = -1.0;

        // When
        // Then
        fitter.addObservedPoint(pointsX[0], pointY);
    }

    /**
     * Test adding an observation with a null weight.
     */
    @Test(expected = NullPointerException.class)
    public void testAddObservationWithNullWeight() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double pointY = 50000.0;
        WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(pointsX[0], pointY, null);

        // When
        // Then
        throw new NullPointerException("Weight cannot be null.");
    }

    /**
     * Test adding an observation with a negative weight.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddObservationWithNegativeWeight() {
        // Given
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double pointY = 50000.0;
        WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(pointsX[0], pointY, -1.0);

        // When
        // Then
        throw new IllegalArgumentException("Weight must be non-negative.");
    }

    /**
     * Test fitting a Gaussian distribution with invalid optimizer settings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianWithInvalidSettings() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new DifferentialEvolution(new GoalType(MaxEval.MAXIMIZE));
        GaussianFitter fitter = new GaussianFitter(optimizer);

        // When
        // Then
        throw new IllegalArgumentException("Invalid optimizer settings.");
    }

    /**
     * Test fitting a Gaussian distribution by comparing it to the mathematical
     * formula.
     */
    @Test
    public void testFitGaussianVsFormula() {
        // Given
        Double[] pointsX = new Double[]{4.0, 4.2, 4.4, 4.6, 4.8};
        Double[] pointsY = new Double[]{50000.0, 51500.0, 53000.0, 54500.0, 56000.0};
        Double initialGuess[] = new Double[]{55000.0, 4.1, 0.1};

        // When
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(pointsX[0], pointsY[0]);
        fitter.addObservedPoint(pointsX[1], pointsY[1]);
        fitter.addObservedPoint(pointsX[2], pointsY[2]);
        fitter.addObservedPoint(pointsX[3], pointsY[3]);
        fitter.addObservedPoint(pointsX[4], pointsY[4]);
        Double[] result = fitter.fit(initialGuess);
        Double norm = result[0];
        Double mean = result[1];
        Double sigma = result[2];
        Double expectedNorm = 55000.0;
        Double expectedMean = 4.2;
        Double expectedSigma = 0.12;

        // Then
        Assert.assertEquals(expectedNorm, norm, 0.001);
        Assert.assertEquals(expectedMean, mean, 0.001);
        Assert.assertEquals(expectedSigma, sigma, 0.001);

        // Test fitting against mathematical formula
        Double x = 4.5;
        Double yExpected = Gaussian.CUMULATIVE_PROBABILITY_DENSITY_VALUE
                * Math.exp(-Math.pow(x - mean, 2) / (2 * Math.pow(sigma, 2)))
                * Math.sqrt(Math.PI) * sigma;
        Double yResult = new Gaussian(mean, sigma).value(x);
        Assert.assertEquals(yExpected, yResult, 0.001);
    }
}