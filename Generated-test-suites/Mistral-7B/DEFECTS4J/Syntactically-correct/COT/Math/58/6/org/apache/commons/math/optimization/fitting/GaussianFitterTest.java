package org.apache.commons.math.optimization.fitting.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.vector.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.LinearEqualityConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.LinearInequalityConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.*;
import org.apache.commons.math3.optim.points.PointValues;
import org.apache.commons.math3.optim.points.RelPointValuePair;
import org.apache.commons.math3.optim.points.SimplePoints;
import org.apache.commons.math3.optim.initializers.GaussNewtonInitialGuess;
import org.apache.commons.math3.optim.initializers.LarsInitialGuess;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleStoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LUDecomposition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LUPDecomposition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.CholeskyDecomposition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.CholeskyDecompositionWithPivot;
import org.apache.commons.math3.optim.nonlinear.vector.linear.QRDecomposition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.SVDDecomposition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.DenseLinearConstraintSet;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math.optimization.fitting.ParameterGuesser;

/**
 * Test suite for {@link GaussianFitter}.
 */
public class GaussianFitterTest {
    /** Optimizer. */
    private UnivariateOptimizer optimizer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
    }

    /**
     * Basic test for {@link GaussianFitter#fit(double[])}.
     *
     * @throws NotStrictlyPositiveException if {@code initialGuess[0]} is not
     * strictly positive.
     * @throws IllegalArgumentException if {@code observations} or
     * {@code initialGuess} are {@code null} or if {@code initialGuess} has
     * length less than 3.
     */
    @Test
    public void testFit() throws NotStrictlyPositiveException {
        double[] initialGuess = new double[] { 1.0, 2.0, 1.0 };
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, 1.0),
                new WeightedObservedPoint(4.0, 1.0),
                new WeightedObservedPoint(5.0, 1.0)
        };
        double[] result = fitter.fit(initialGuess);
        assertEquals("Result should have length 3.", 3, result.length);
        assertEquals("First element should be norm.", 1.0, result[0], 1e-6);
        assertEquals("Second element should be mean.", 2.0, result[1], 1e-6);
        assertEquals("Third element should be sigma.", 1.0, result[2], 1e-6);
    }

    /**
     * Test for {@link GaussianFitter#fit()} with empty {@code observations}.
     *
     * @throws IllegalArgumentException if {@code observations} is {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = null;
        fitter.fit(observations);
    }

    /**
     * Test for {@link GaussianFitter#fit()} with {@code observations} having
     * length less than 3.
     *
     * @throws IllegalArgumentException if {@code observations} is {@code null}.
     * @throws NotStrictlyPositiveException if {@code initialGuess[0]} is not
     * strictly positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitShortObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, 1.0)
        };
        fitter.fit(new double[] { 1.0, 2.0, 1.0 });
    }

    /**
     * Test for {@link GaussianFitter#guess()} with empty {@code observations}.
     *
     * @throws IllegalArgumentException if {@code observations} is {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessEmptyObservations() {
        ParameterGuesser guesser = new ParameterGuesser(null);
    }

    /**
     * Test for {@link GaussianFitter#guess()} with {@code observations} having
     * length less than 3.
     *
     * @throws IllegalArgumentException if {@code observations} is {@code null}.
     * @throws IllegalStateException if {@code guess} has length less than 3.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessShortObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, 1.0)
        };
        ParameterGuesser guesser = new ParameterGuesser(observations);
        guesser.guess();
    }

    /**
     * Test for {@link GaussianFitter#guess()} with {@code observations} having
     * null points.
     *
     * @throws IllegalArgumentException if {@code observations} is {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessNullPoints() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                null
        };
        ParameterGuesser guesser = new ParameterGuesser(observations);
        guesser.guess();
    }

    /**
     * Test for {@link GaussianFitter#guess()} with {@code observations} having
     * points with null weights.
     *
     * @throws IllegalArgumentException if {@code observations} is {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessNullWeights() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, null)
        };
        ParameterGuesser guesser = new ParameterGuesser(observations);
        guesser.guess();
    }

    /**
     * Test for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     *
     * @throws NotStrictlyPositiveException if initialGuess[0] is not strictly
     * positive.
     * @throws IllegalArgumentException if {@code observations} or
     * {@code f} or {@code initialGuess} are {@code null} or if
     * {@code observations} has length less than 3 or if
     * {@code initialGuess} has length less than 3.
     */
    @Test
    public void testFitFunction() throws NotStrictlyPositiveException {
        ParametricUnivariateRealFunction f = new Gaussian.Parametric();
        double[] initialGuess = new double[] { 1.0, 2.0, 1.0 };
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, 1.0),
                new WeightedObservedPoint(4.0, 1.0),
                new WeightedObservedPoint(5.0, 1.0)
        };
        double[] result = fitter.fit(f, initialGuess);
        assertEquals("Result should have length 3.", 3, result.length);
        assertEquals("First element should be norm.", 1.0, result[0], 1e-6);
        assertEquals("Second element should be mean.", 2.0, result[1], 1e-6);
        assertEquals("Third element should be sigma.", 1.0, result[2], 1e-6);
        assertEquals("Function value at mean should be the norm.", result[0],
                     f.value(result[1]), 1e-6);
    }

    /**
     * Test for {@link GaussianFitter#fit(ParametricUnivariateRealFunction)} with
     * empty {@code observations}.
     *
     * @throws IllegalArgumentException if {@code observations} or {@code f} are
     * {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitFunctionEmptyObservations() {
        ParametricUnivariateRealFunction f = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = null;
        fitter.fit(f, observations);
    }

    /**
     * Test for {@link GaussianFitter#fit(ParametricUnivariateRealFunction)} with
     * {@code observations} having length less than 3.
     *
     * @throws IllegalArgumentException if {@code observations} or {@code f} are
     * {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitFunctionShortObservations() {
        ParametricUnivariateRealFunction f = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, 1.0)
        };
        fitter.fit(f, new double[] { 1.0, 2.0, 1.0 });
    }

    /**
     * Test for {@link GaussianFitter#fit(ParametricUnivariateRealFunction)} with
     * {@code observations} having null points.
     *
     * @throws IllegalArgumentException if {@code observations} or {@code f} are
     * {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testFitFunctionNullPoints() {
        ParametricUnivariateRealFunction f = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                null
        };
        fitter.fit(f, observations);
    }

    /**
     * Test for {@link GaussianFitter#fit(ParametricUnivariateRealFunction)} with
     * {@code observations} having points with null weights.
     *
     * @throws IllegalArgumentException if {@code observations} or {@code f} are
     * {@code null}.
     */
    @Test(expected = NullPointerException.class)
    public void testFitFunctionNullWeights() {
        ParametricUnivariateRealFunction f = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, null)
        };
        fitter.fit(f, observations);
    }
}