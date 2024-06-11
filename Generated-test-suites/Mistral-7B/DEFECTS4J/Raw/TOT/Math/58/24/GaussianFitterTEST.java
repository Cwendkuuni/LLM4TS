package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.vector.linear.IdentityConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.NullSpaceConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.SimpleVector;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorValues;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test the {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Test that {@link GaussianFitter} can fit a Gaussian function to a set of
     * observations.
     */
    @Test
    public void testGaussianFit() {
        // Create a GaussianFitter instance with a LevenbergMarquardtOptimizer
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        // Create some observations
        double[] xs = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] ys = { 1.1, 2.2, 3.3, 4.4, 5.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xs.length];
        for (int i = 0; i < xs.length; i++) {
            observations[i] = new WeightedObservedPoint(xs[i], ys[i], 1.0);
            fitter.addObservedPoint(observations[i]);
        }

        // Fit the observations to a Gaussian function
        double[] parameters = fitter.fit();

        // Create a Gaussian function with the fitted parameters
        UnivariateFunction gaussian = new Gaussian.Parametric(parameters[0], parameters[1],
                parameters[2]);

        // Check the fitted function against the observations
        for (WeightedObservedPoint obs : observations) {
            double expectedY = gaussian.value(obs.getX());
            double error = Math.abs(expectedY - obs.getY());
            double tolerance = 1e-6;
            assertTrue("Error at x = " + obs.getX() + " should be less than " + tolerance,
                    error < tolerance);
        }
    }

    /**
     * Test that {@link GaussianFitter} throws an exception when the initial
     * guess vector is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussianFitNullInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit((double[]) null);
    }

    /**
     * Test that {@link GaussianFitter} throws an exception when the
     * observations array is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussianFitNullObservations() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit((WeightedObservedPoint[]) null);
    }

    /**
     * Test that {@link GaussianFitter} can fit a Gaussian function to a set of
     * observations with a custom optimizer.
     */
    @Test
    public void testGaussianFitCustomOptimizer() {
        // Define a custom optimizer
        class MyOptimizer extends UnivariateOptimizer {
            @Override
            public PointValuePair optimize(UnivariateFunction function,
                    SimpleBounds bounds, double stepSize, int maxIterations,
                    boolean allowGradient, VectorConstraint[] constraints,
                    VectorValues initialX, double tolerance, int maxFunctionEvaluations) {
                // TODO: implement custom optimization algorithm here
                fail("Not implemented");
            }
        }
        MyOptimizer myOptimizer = new MyOptimizer();

        // Create a GaussianFitter instance with the custom optimizer
        GaussianFitter fitter = new GaussianFitter(myOptimizer);

        // Create some observations
        double[] xs = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] ys = { 1.1, 2.2, 3.3, 4.4, 5.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xs.length];
        for (int i = 0; i < xs.length; i++) {
            observations[i] = new WeightedObservedPoint(xs[i], ys[i], 1.0);
            fitter.addObservedPoint(observations[i]);
        }

        // Fit the observations to a Gaussian function
        double[] parameters = fitter.fit();

        // Create a Gaussian function with the fitted parameters
        UnivariateFunction gaussian = new Gaussian.Parametric(parameters[0], parameters[1],
                parameters[2]);

        // Check the fitted function against the observations
        for (WeightedObservedPoint obs : observations) {
            double expectedY = gaussian.value(obs.getX());
            double error = Math.abs(expectedY - obs.getY());
            double tolerance = 1e-6;
            assertTrue("Error at x = " + obs.getX() + " should be less than " + tolerance,
                    error < tolerance);
        }
    }

    /**
     * Test that {@link GaussianFitter} throws an exception when the initial
     * guess vector length is not 3.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitInvalidInitialGuessLength() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = { 1.0, 2.0 };
        fitter.fit(initialGuess);
    }

    /**
     * Test that {@link GaussianFitter} throws an exception when the bounds are
     * null.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussianFitNullBounds() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        SimpleBounds bounds = null;
        try {
            fitter.optimize(new Gaussian.Parametric(1.0, 1.0, 1.0), bounds, 1.0, 1, false,
                    (VectorConstraint[]) null, null, 1e-6, 100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test that {@link GaussianFitter} throws an exception when the bounds
     * violate the constraints.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussianFitterBoundsViolatingConstraints() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        // Define invalid bounds
        SimpleBounds bounds = new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                new SimpleBounds(-1, 0), new SimpleBounds(1, 2));

        try {
            fitter.optimize(new Gaussian.Parametric(1.0, 1.0, 1.0), bounds, 1.0, 1, false,
                    (VectorConstraint[]) null, null, 1e-6, 100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test that {@link GaussianFitter} throws an exception when the constraints
     * are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussianFitterNullConstraints() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        // Create some observations
        double[] xs = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] ys = { 1.1, 2.2, 3.3, 4.4, 5.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xs.length];
        for (int i = 0; i < xs.length; i++) {
            observations[i] = new WeightedObservedPoint(xs[i], ys[i], 1.0);
            fitter.addObservedPoint(observations[i]);
        }

        // Define some constraints
        VectorConstraint constraint1 = new IdentityConstraint(1);
        VectorConstraint constraint2 = new NullSpaceConstraint();
        List<VectorConstraint> constraints = Arrays.asList(constraint1, constraint2);

        // Fit the observations to a Gaussian function with constraints
        try {
            fitter.optimize(new Gaussian.Parametric(1.0, 1.0, 1.0), null, 1.0, 1, false,
                    constraints.toArray(new VectorConstraint[0]), null, 1e-6, 100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test that {@link GaussianFitter} can fit a Gaussian function to a set of
     * observations with constraints.
     */
    @Test
    public void testGaussianFitWithConstraints() {
        // Create a GaussianFitter instance with a LevenbergMarquardtOptimizer
        DifferentiableMultivariateVectorialOptimizer optimizer =
                new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        // Create some observations
        double[] xs = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] ys = { 1.1, 2.2, 3.3, 4.4, 5.5 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[xs.length];
        for (int i = 0; i < xs.length; i++) {
            observations[i] = new WeightedObservedPoint(xs[i], ys[i], 1.0);
            fitter.addObservedPoint(observations[i]);
        }

        // Define some constraints
        LinearConstraint constraint1 = new LinearConstraint(1, new SimpleVector(1.0, 0.0));
        LinearConstraint constraint2 = new LinearConstraint(1, new SimpleVector(0.0, 1.0));
        NullSpaceConstraint constraint3 = new NullSpaceConstraint();
        List<VectorConstraint> constraints = Arrays.asList(constraint1, constraint2, constraint3);

        // Fit the observations to a Gaussian function with constraints
        double[] parameters = fitter.fit(new Gaussian.Parametric(1.0, 1.0, 1.0), constraints.toArray(new VectorConstraint[0]));

        // Create a Gaussian function with the fitted parameters
        UnivariateFunction gaussian = new Gaussian.Parametric(parameters[0], parameters[1],
                parameters[2]);

        // Check the fitted function against the observations
        for (WeightedObservedPoint obs : observations) {
            double expectedY = gaussian.value(obs.getX());
            double error = Math.abs(expectedY - obs.getY());
            double tolerance = 1e-6;
            assertTrue("Error at x = " + obs.getX() + " should be less than " + tolerance,
                    error < tolerance);
        }
    }
}