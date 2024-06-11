package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
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
                new WeightedObservedPoint(4.08888889, 555555.5, 1.0),
                new WeightedObservedPoint(4.09444444, 444444.4, 1.0)
        };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();

        double[] expected = { 1000000.0, 4.06448738, 0.11111111 };
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test(expected = NullPointerException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitTooFewObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 1.0)
        };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeInitialGuess() {
        double[] initialGuess = { -1.0, 1.0, 1.0 };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(initialGuess);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(null);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitZeroInitialGuess() {
        double[] initialGuess = { 0.0, 1.0, 1.0 };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(initialGuess);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitTooFewInitialGuess() {
        double[] initialGuess = { 1.0 };
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(initialGuess);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}