package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonLinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.fitting.GaussianCurveFitter;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /** Instance under test. */
    private GaussianFitter fitter;

    /** Test observation points. */
    private WeightedObservedPoint[] observations;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        // Set up test observations
        double[] xs = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976,
            4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071,
            4.08366408 };
        double[] ys = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0,
            3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };
        observations = new WeightedObservedPoint[xs.length];
        for (int i = 0; i < observations.length; ++i) {
            observations[i] = new WeightedObservedPoint(xs[i], ys[i], 1.0);
        }

        // Set up fitter
        DifferentiableMultivariateVectorFunction function = new Gaussian(InitialGuess.IDENTITY, 3);
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer(1e-8, 1e-8, 100, 100));
        fitter.addObservedPoints(observations);
    }

    /**
     * Tests that {@link GaussianFitter#fit()} returns correct results.
     */
    @Test
    public void testFit() {
        double[] expected = { 250000.0, 4.05, 0.15 };
        assertArrayEquals(expected, fitter.fit(), 1e-10);
    }

    /**
     * Tests that {@link GaussianFitter#fit(double[])} returns correct results
     * when initial guess is correct.
     */
    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = { 250000.0, 4.05, 0.15 };
        double[] expected = { 250000.0, 4.05, 0.15 };
        assertArrayEquals(expected, fitter.fit(initialGuess), 1e-10);
    }

    /**
     * Tests that {@link GaussianFitter#fit(double[])} returns correct results
     * when initial guess is incorrect.
     */
    @Test
    public void testFitWithIncorrectInitialGuess() {
        double[] initialGuess = { 1.0, 1.0, 1.0 };
        double[] expected = { 250000.0, 4.05, 0.15 };
        assertArrayEquals(expected, fitter.fit(initialGuess), 1e-10);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * returns correct results.
     */
    @Test
    public void testFitWithFunction() throws Exception {
        DifferentiableMultivariateVectorFunction function = new Gaussian(InitialGuess.IDENTITY, 3);
        double[] expected = { 250000.0, 4.05, 0.15 };
        assertArrayEquals(expected, fitter.fit(function).getParameterVector(), 1e-10);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when function is null.
     */
    @Test(expected = NullPointerException.class)
    public void testFitWithNullFunction() throws Exception {
        fitter.fit((DifferentiableMultivariateVectorFunction) null);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when observations are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservations() {
        fitter.fit((WeightedObservedPoint[]) null);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when observations are empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservations() {
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), new WeightedObservedPoint[0]);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when observations do not match the function's dimensionality.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithMismatchedObservations() {
        DifferentiableMultivariateVectorFunction function = new LinearObjectiveFunction(3);
        fitter.fit(function, observations);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when function's gradient is not defined.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFitWithNonDifferentiableFunction() throws Exception {
        DifferentiableMultivariateVectorFunction function = new LinearObjectiveFunction(3) {
            @Override
            public int getNumberOfVariables() {
                return 3;
            }

            @Override
            public PointValuePair value(PointValuePair point) throws Exception {
                throw new UnsupportedOperationException();
            }

            @Override
            public PointValuePair gradient(PointValuePair point) throws Exception {
                throw new UnsupportedOperationException();
            }
        };
        fitter.fit(function, observations);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when function has no solution.
     */
    @Test(expected = org.apache.commons.math3.optim.MaxEvalException.class)
    public void testFitWithNoSolutionFunction() throws Exception {
        DifferentiableMultivariateVectorFunction function = new LinearObjectiveFunction(3) {
            @Override
            public PointValuePair value(PointValuePair point) throws Exception {
                return new LinearPointValuePair(1.0, 1.0,
                        Math.pow(point.getVector()[0], 2.0) +
                                Math.pow(point.getVector()[1], 2.0) +
                                Math.pow(point.getVector()[2], 2.0) - 1.0);
            }
        };
        fitter.fit(function, observations);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when function has multiple solutions.
     */
    @Test(expected = org.apache.commons.math3.optim.ConvergenceException.class)
    public void testFitWithMultipleSolutionsFunction() throws Exception {
        DifferentiableMultivariateVectorFunction function = new LinearObjectiveFunction(3) {
            @Override
            public PointValuePair value(PointValuePair point) throws Exception {
                return new LinearPointValuePair(1.0, 1.0,
                        Math.pow(point.getVector()[0], 2.0) +
                                Math.pow(point.getVector()[1], 2.0) +
                                Math.pow(point.getVector()[2], 2.0) - 1.0);
            }

            @Override
            public PointValuePair gradient(PointValuePair point) throws Exception {
                return new LinearPointValuePair(1.0, 1.0, new double[] {
                        2.0 * point.getVector()[0],
                        2.0 * point.getVector()[1],
                        2.0 * point.getVector()[2]
                });
            }
        };
        fitter.fit(function, observations);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when bounds are not non-empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyBounds() {
        SimpleBounds bounds = new SimpleBounds();
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), bounds);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when bounds are not valid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidBounds() {
        double[] bounds = new double[] { -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 };
        SimpleBounds boundsObj = new SimpleBounds(bounds);
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), boundsObj);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when bounds are not compatible with the function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithIncompatibleBounds() throws Exception {
        DifferentiableMultivariateVectorFunction function = new Gaussian(InitialGuess.IDENTITY, 3);
        SimpleBounds bounds = new SimpleBounds(new double[] { -1.0, -1.0, -1.0 },
                new double[] { 0.0, 0.0, 0.0 });
        fitter.fit(function, bounds);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when constraints are not empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNonEmptyConstraints() {
        PointValuePairCollection constraints = new PointValuePairCollection();
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), constraints);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when constraints are not valid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidConstraints() {
        LinearConstraint constraint = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        PointValuePairCollection constraints = new PointValuePairCollection();
        constraints.add(new LinearPointValuePair(1.0, constraint));
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), constraints);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when constraints are not compatible with the function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithIncompatibleConstraints() throws Exception {
        DifferentiableMultivariateVectorFunction function = new Gaussian(InitialGuess.IDENTITY, 3);
        LinearConstraint constraint = new LinearConstraint(new double[] { 1.0, 1.0 }, 0.0);
        PointValuePairCollection constraints = new PointValuePairCollection();
        constraints.add(new LinearPointValuePair(1.0, constraint));
        fitter.fit(function, constraints);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when stopping criteria is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNoStoppingCriteria() {
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), observations, null);
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when stopping criteria is not valid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidStoppingCriteria() {
        fitter.fit(new Gaussian(InitialGuess.IDENTITY, 3), observations, new StoppingCriteria() {
            @Override
            public boolean isSatisfied(PointValuePair point, int iteration) {
                throw new IllegalArgumentException();
            }
        });
    }

    /**
     * Tests that {@link GaussianFitter#fit(DifferentiableMultivariateVectorFunction)}
     * throws an exception when stopping criteria is incompatible with the function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithIncompatibleStoppingCriteria() throws Exception {
        DifferentiableMultivariateVectorFunction function = new Gaussian(InitialGuess.IDENTITY, 3);
        fitter.fit(function, observations, new StoppingCriteria() {
            @Override
            public boolean isSatisfied(PointValuePair point, int iteration) {
                throw new IllegalArgumentException();
            }
        });
    }
}