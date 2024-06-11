package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.initialization.Guess;
import org.apache.commons.math3.optim.initialization.ParameterGuesser;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GaussianFitterTest {

    @Test
    public void testGaussianFitterWithPositiveInitialGuessValues() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[]{1.0, 5.0, 1.5};

        // When
        double[] parameters = gaussianFitter.fit(initialGuess);

        // Then
        assertArrayEquals(new double[]{1.0, 5.0, 1.5}, parameters, 0.001);
    }

    @Test
    public void testGaussianFitterWithNullInitialGuess() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);

        // When and Then
        assertNull(gaussianFitter.fit(null));
    }

    @Test
    public void testGaussianFitterWithEmptyObservedPoints() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] emptyObservedPoints = new WeightedObservedPoint[0];

        // When
        try {
            gaussianFitter.fit(emptyObservedPoints);
            // This point should never be reached
        } catch (IllegalArgumentException e) {
            // Then
            assertEquals("Cannot fit a Gaussian to an empty set of observations", e.getMessage());
        }
    }

    @Test
    public void testGaussianFitterWithNegativeInitialGuessValues() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[]{-1.0, 5.0, -1.5};

        // When
        try {
            gaussianFitter.fit(initialGuess);
            // This point should never be reached
        } catch (NotStrictlyPositiveException e) {
            // Then
            assertEquals("The initial guess for the standard deviation (norm) should be strictly positive.", e.getMessage());
        }
    }

    @Test
    public void testGaussianFitterWithFitMethod() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observedPoints = new WeightedObservedPoint[]{
                new RealPointValuePair(2.5, 100.0),
                new RealPointValuePair(3.0, 150.0),
                new RealPointValuePair(3.5, 210.0),
                new RealPointValuePair(4.0, 320.0),
                new RealPointValuePair(4.5, 490.0),
                new RealPointValuePair(5.0, 720.0)
        };

        // When
        double[] parameters = gaussianFitter.fit(observedPoints);

        // Then
        assertArrayEquals(new double[]{720.0, 4.5, 0.75}, parameters, 0.001);
    }

    @Test
    public void testGaussianFitterWithInitialGuessFromParameterGuesser() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observedPoints = new WeightedObservedPoint[]{
                new RealPointValuePair(2.5, 100.0),
                new RealPointValuePair(3.0, 150.0),
                new RealPointValuePair(3.5, 210.0),
                new RealPointValuePair(4.0, 320.0),
                new RealPointValuePair(4.5, 490.0),
                new RealPointValuePair(5.0, 720.0)
        };

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        assertArrayEquals(new double[]{720.0, 4.5, 0.75}, parameters, 0.001);
    }

    @Test
    public void testGaussianFitterWithCustomParameterGuesser() {
        // Given
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observedPoints = new WeightedObservedPoint[]{
                new RealPointValuePair(2.5, 100.0),
                new RealPointValuePair(3.0, 150.0),
                new RealPointValuePair(3.5, 210.0),
                new RealPointValuePair(4.0, 320.0),
                new RealPointValuePair(4.5, 490.0),
                new RealPointValuePair(5.0, 720.0)
        };
        ParameterGuesser parameterGuesser = new ParameterGuesser(observedPoints);
        double[] initialGuess = parameterGuesser.guess();

        // When
        double[] parameters = gaussianFitter.fit(initialGuess);

        // Then
        assertArrayEquals(new double[]{720.0, 4.5, 0.75}, parameters, 0.001);
    }
}

class ParameterGuesser implements ParameterGuesser {
    private final WeightedObservedPoint[] observations;

    public ParameterGuesser(WeightedObservedPoint[] observations) {
        this.observations = observations;
    }

    @Override
    public double[] guess() {
        return observations[0].getFunctionValue();
    }

    @Override
    public double[] guess(Guess guess) {
        throw new UnsupportedOperationException("Guess is not supported in this test.");
    }
}

class WeightedObservedPointComparator implements Comparator<WeightedObservedPoint> {
    @Override
    public int compare(WeightedObservedPoint o1, WeightedObservedPoint o2) {
        return Double.compare(o1.getX(), o2.getX());
    }
}