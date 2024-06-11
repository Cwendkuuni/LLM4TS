package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.WeightedObservedPoint;
import org.junit.Test;

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
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0)
        };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] params = fitter.fit(observations);
        Double[] expectedParams = new Double[] { 432512.616220594, 4.059806777262396, 857.8448717241702 };
        assertArrayEquals(expectedParams, params);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitLessThanThreeObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        fitter.addObservedPoint(observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0));
        fitter.addObservedPoint(observations[1] = new WeightedObservedPoint(2.0, 2.0, 1.0));
        fitter.fit(observations);
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitInvalidInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] initialGuess = new Double[] { 1.0, 2.0, 3.0 };
        try {
            fitter.fit(initialGuess);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInitialGuess() {
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
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0)
        };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        Double[] initialGuess = new Double[] { 5000.0, 4.0, 500.0 };
        Double[] params = fitter.fit(initialGuess, observations);
        Double[] expectedParams = new Double[] { 432512.616220594, 4.059806777262396, 857.8448717241702 };
        assertArrayEquals(expectedParams, params);
    }
}

class LevenbergMarquardtOptimizer implements DifferentiableMultivariateVectorialOptimizer {
    @Override
    public PointValuePair minimize(ParametricUnivariateRealFunction function, Double[] initialGuess) {
        // Implementation not required for test.
        return null;
    }
}