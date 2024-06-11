package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class GaussianFitterTest {
    @Test
    public void testFit() throws NotStrictlyPositiveException {
        Double[][] observations = new Double[][] {
                { 4.0254623, 531026.0 },
                { 4.03128248, 984167.0 },
                { 4.03839603, 1887233.0 },
                { 4.04421621, 2687152.0 },
                { 4.05132976, 3461228.0 },
                { 4.05326982, 3580526.0 },
                { 4.05779662, 3439750.0 },
                { 4.0636168, 2877648.0 },
                { 4.06943698, 2175960.0 },
                { 4.07525716, 1447024.0 },
                { 4.08237071, 717104.0 },
                { 4.08366408, 620014.0 }
        };

        WeightedObservedPoint[] weightedObservations = new WeightedObservedPoint[observations.length];
        for (int i = 0; i < observations.length; i++) {
            weightedObservations[i] = new WeightedObservedPoint(observations[i][0], observations[i][1], 1.0);
        }

        DifferentiableMultivariateVectorialOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        gaussianFitter.addObservedPoints(weightedObservations);

        double[] parameters = gaussianFitter.fit();
        assertEquals(3, parameters.length);
        assertTrue(parameters[0] > 0.0);
        assertTrue(parameters[1] >= 0.0);
        assertTrue(parameters[2] > 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testFitWithNullOptimizer() {
        GaussianFitter gaussianFitter = new GaussianFitter(null);
        gaussianFitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservedPoints() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        gaussianFitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNullObservedPoints() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        gaussianFitter.addObservedPoints((WeightedObservedPoint[]) null);
        gaussianFitter.fit();
    }

    @Test
    public void testFitWithInitialGuess() {
        Double[][] observations = new Double[][] {
                { 4.0254623, 531026.0 },
                { 4.03128248, 984167.0 },
                { 4.03839603, 1887233.0 },
                { 4.04421621, 2687152.0 },
                { 4.05132976, 3461228.0 },
                { 4.05326982, 3580526.0 },
                { 4.05779662, 3439750.0 },
                { 4.0636168, 2877648.0 },
                { 4.06943698, 2175960.0 },
                { 4.07525716, 1447024.0 },
                { 4.08237071, 717104.0 },
                { 4.08366408, 620014.0 }
        };

        WeightedObservedPoint[] weightedObservations = new WeightedObservedPoint[observations.length];
        for (int i = 0; i < observations.length; i++) {
            weightedObservations[i] = new WeightedObservedPoint(observations[i][0], observations[i][1], 1.0);
        }

        DifferentiableMultivariateVectorialOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        gaussianFitter.addObservedPoints(weightedObservations);

        double[] initialGuess = { 1.0, 2.0, 3.0 };
        double[] parameters = gaussianFitter.fit(initialGuess);
        assertEquals(3, parameters.length);
        assertTrue(parameters[0] > 0.0);
        assertTrue(parameters[1] >= 0.0);
        assertTrue(parameters[2] > 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        gaussianFitter.addObservedPoints(new WeightedObservedPoint[] {});
        gaussianFitter.fit((double[]) null);
    }
}