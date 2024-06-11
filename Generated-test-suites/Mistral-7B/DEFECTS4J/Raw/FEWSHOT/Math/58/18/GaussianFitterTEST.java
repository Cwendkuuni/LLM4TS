package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.function.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.optimize.LeastSquaresOptimizer;
import org.apache.commons.math3.optim.nonlinear.optimize.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.optimize.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.optimize.noderiv.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.points.WeightedObservedPoint;
import org.apache.commons.math3.optim.points.WeightedObservedPoints;
import org.junit.Test;

/**
 * Test cases for {@link GaussianFitter}.
 */
public class GaussianFitterTest {
    /**
     * Test of GaussianFitter constructor.
     */
    @Test
    public void testGaussianFitterConstruction() {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertNotNull(fitter);
    }

    /**
     * Test of fit method with custom initial guess.
     */
    @Test
    public void testFitWithCustomInitialGuess() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 1.0, 2.0, 0.5 };
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(new double[] { 2.0, 2.0, 0.5 }, result, 1e-12);
    }

    /**
     * Test of fit method without initial guess.
     */
    @Test
    public void testFitWithoutInitialGuess() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit();
        assertNotNull(result);
    }

    /**
     * Test of GaussianFitter.fit method with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuess() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 0.0, 2.0, 0.5 };
        fitter.fit(initialGuess);
    }

    /**
     * Test of GaussianFitter.fit method with empty list of observations.
     */
    @Test
    public void testFitWithEmptyObservations() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit();
        assertArrayEquals(new double[0], result, 0);
    }

    /**
     * Test of GaussianFitter.fit method with one observation.
     */
    @Test
    public void testFitWithSingleObservation() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint observation = new WeightedObservedPoint(1.0, 2.0, 1.0);
        fitter.addObservedPoint(observation);
        double[] result = fitter.fit();
        assertArrayEquals(new double[] { 1.0, 2.0, Math.sqrt(1.0) }, result, 1e-12);
    }

    /**
     * Test of GaussianFitter.fit method with multiple observations.
     */
    @Test
    public void testFitWithMultipleObservations() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(1.0, 1.0, 1.0),
                new WeightedObservedPoint(2.0, 4.0, 1.0),
                new WeightedObservedPoint(3.0, 9.0, 1.0),
                new WeightedObservedPoint(4.0, 16.0, 1.0),
                new WeightedObservedPoint(5.0, 25.0, 1.0),
                new WeightedObservedPoint(6.0, 36.0, 1.0),
                new WeightedObservedPoint(7.0, 49.0, 1.0),
                new WeightedObservedPoint(8.0, 64.0, 1.0),
                new WeightedObservedPoint(9.0, 81.0, 1.0)
        };
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();
        assertArrayEquals(new double[] { 3.0, 3.0, 2.0 }, result, 1e-12);
    }

    /**
     * Test of GaussianFitter.fit method with different optimizers.
     */
    @Test
    public void testFitWithDifferentOptimizers() throws Exception {
        // LevenbergMarquardtOptimizer
        DifferentiableMultivariateVectorialOptimizer optimizerLM =
                new LevenbergMarquardtOptimizer();
        GaussianFitter fitterLM = new GaussianFitter(optimizerLM);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(1.0, 1.0, 1.0),
                new WeightedObservedPoint(2.0, 4.0, 1.0),
                new WeightedObservedPoint(3.0, 9.0, 1.0),
                new WeightedObservedPoint(4.0, 16.0, 1.0),
                new WeightedObservedPoint(5.0, 25.0, 1.0),
                new WeightedObservedPoint(6.0, 36.0, 1.0),
                new WeightedObservedPoint(7.0, 49.0, 1.0),
                new WeightedObservedPoint(8.0, 64.0, 1.0),
                new WeightedObservedPoint(9.0, 81.0, 1.0)
        };
        fitterLM.addObservedPoints(observations);
        double[] resultLM = fitterLM.fit();
        assertArrayEquals(new double[] { 3.0, 3.0, 2.0 }, resultLM, 1e-12);

        // GaussNewtonOptimizer
        DifferentiableMultivariateVectorialOptimizer optimizerGN =
                new GaussNewtonOptimizer();
        GaussianFitter fitterGN = new GaussianFitter(optimizerGN);
        fitterGN.addObservedPoints(observations);
        double[] resultGN = fitterGN.fit();
        assertArrayEquals(new double[] { 3.0, 3.0, 2.0 }, resultGN, 1e-12);

        // SimpleBounds
        DifferentiableMultivariateVectorialOptimizer optimizerSB =
                new LeastSquaresOptimizer(new SimpleBounds(0.0, Double.POSITIVE_INFINITY, 3));
        GaussianFitter fitterSB = new GaussianFitter(optimizerSB);
        fitterSB.addObservedPoints(observations);
        double[] resultSB = fitterSB.fit();
        assertArrayEquals(new double[] { 3.0, 3.0, 2.0 }, resultSB, 1e-12);
    }

    /**
     * Test of GaussianFitter.fit method with invalid observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidObservations() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                null,
                new WeightedObservedPoint(Double.NaN, 2.0, 1.0),
                new WeightedObservedPoint(1.0, Double.NaN, 1.0),
                new WeightedObservedPoint(1.0, 2.0, 0.0)
        };
        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    /**
     * Test of GaussianFitter.fit method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidArguments() throws Exception {
        DifferentiableMultivariateVectorialOptimizer optimizer =
            new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(1.0, 2.0, 1.0),
                new WeightedObservedPoint(2.0, 4.0, 1.0),
                new WeightedObservedPoint(3.0, 9.0, 1.0),
                new WeightedObservedPoint(4.0, 16.0, 1.0),
                new WeightedObservedPoint(5.0, 25.0, 1.0),
                new WeightedObservedPoint(6.0, 36.0, 1.0),
                new WeightedObservedPoint(7.0, 49.0, 1.0),
                new WeightedObservedPoint(8.0, 64.0, 1.0),
                new WeightedObservedPoint(9.0, 81.0, 1.0),
                new WeightedObservedPoint(10.0, 90.0, 1.0) // invalid observation
        };
        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    /**
     * Test of GaussianFitter.ParameterGuesser constructor.
     */
    @Test
    public void testParameterGuesserConstruction() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(1.0, 1.0, 1.0),
                new WeightedObservedPoint(2.0, 4.0, 1.0),
                new WeightedObservedPoint(3.0, 9.0, 1.0),
                new WeightedObservedPoint(4.0, 16.0, 1.0),
                new WeightedObservedPoint(5.0, 25.0, 1.0),
                new WeightedObservedPoint(6.0, 36.0, 1.0),
                new WeightedObservedPoint(7.0, 49.0, 1.0),
                new WeightedObservedPoint(8.0, 64.0, 1.0),
                new WeightedObservedPoint(9.0, 81.0, 1.0)
        };
        GaussianFitter.ParameterGuesser g = new GaussianFitter.ParameterGuesser(observations);
        assertNotNull(g);
    }

    /**
     * Test of GaussianFitter.ParameterGuesser.guess method.
     */
    @Test
    public void testParameterGuesserGuess() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(1.0, 1.0, 1.0),
                new WeightedObservedPoint(2.0, 4.0, 1.0),
                new WeightedObservedPoint(3.0, 9.0, 1.0),
                new WeightedObservedPoint(4.0, 16.0, 1.0),
                new WeightedObservedPoint(5.0, 25.0, 1.0),
                new WeightedObservedPoint(6.0, 36.0, 1.0),
                new WeightedObservedPoint(7.0, 49.0, 1.0),
                new WeightedObservedPoint(8.0, 64.0, 1.0),
                new WeightedObservedPoint(9.0, 81.0, 1.0)
        };
        GaussianFitter.ParameterGuesser g = new GaussianFitter.ParameterGuesser(observations);
        double[] result = g.guess();
        assertArrayEquals(new double[] { 3.0, 3.0, 2.0 }, result, 1e-12);
    }

    /**
     * Test of GaussianFitter.ParameterGuesser.guess method with empty list of observations.
     */
    @Test
    public void testParameterGuesserGuessWithEmptyObservations() {
        GaussianFitter.ParameterGuesser g = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[0]);
        assertArrayEquals(new double[0], g.guess(), 0);
    }

    /**
     * Test of GaussianFitter.ParameterGuesser.guess method with one observation.
     */
    @Test
    public void testParameterGuesserGuessWithSingleObservation() {
        WeightedObservedPoint observation = new WeightedObservedPoint(1.0, 1.0, 1.0);
        GaussianFitter.ParameterGuesser g = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[] { observation });
        double[] result = g.guess();
        assertArrayEquals(new double[] { 1.0, 1.0, Math.sqrt(1.0) }, result, 1e-12);
    }
}