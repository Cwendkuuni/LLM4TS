package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test cases for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting with custom initial guess.
     */
    @Test
    public void testFitWithCustomInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, 0.0, 1.0 };
        double[] expected = new double[] { 1.5, 0.5, 0.5 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);
        double[] result = fitter.fit(initialGuess);

        assertArrayEquals(expected, result, 0.0001);
    }

    /**
     * Test fitting without initial guess.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();

        double[] expected = new double[] { 1.5, 0.5, 0.5 };
        assertArrayEquals(expected, result, 0.0001);
    }

    /**
     * Test fitting with wrong number of observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithWrongNumberOfObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5)
        };

        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    /**
     * Test fitting with empty observations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        fitter.addObservedPoints(new WeightedObservedPoint[0]);
        fitter.fit();
    }

    /**
     * Test optimizer's fit method.
     */
    @Test
    public void testOptimizerFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, 0.0, 1.0 };
        double[] expected = new double[] { 1.5, 0.5, 0.5 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);
        UnivariateOptimizer optimizer = fitter.getOptimizer();

        PointValuePair result = optimizer.optimize(new ParametricUnivariateRealFunction() {
            public double value(double[] p, double x) {
                Gaussian g = new Gaussian.Parametric();
                return g.value(x, p) - optimizer.getMaximumSearchThreshold();
            }

            public double[] gradient(double[] p, double x) {
                Gaussian g = new Gaussian.Parametric();
                return g.gradient(x, p);
            }
        }, new InitialGuess(initialGuess), new SimpleBounds(-10.0, 10.0),
                GoalType.MAXIMIZE, new MaxEval(1000));

        assertArrayEquals(expected, result.getPoint(), 0.0001);
    }

    /**
     * Test exception handling when fitting with NaN initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNaNInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, Double.NaN, 1.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);
        fitter.fit(initialGuess);
    }

    /**
     * Test exception handling when fitting with negative initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNegativeInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, 0.0, -1.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);
        fitter.fit(initialGuess);
    }

    /**
     * Test exception handling when fitting with non-positive initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNonPositiveInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, 0.0, 0.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);

        // Set initial guess norm to 0.
        initialGuess[0] = 0.0;
        fitter.fit(initialGuess);
    }

    /**
     * Test exception handling when fitting with too small initial guess sigma.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithTooSmallInitialGuessSigma() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, 0.0, 1e-10 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 1.0),
            new WeightedObservedPoint(3.0, 2.5),
            new WeightedObservedPoint(3.5, 5.0),
            new WeightedObservedPoint(4.0, 10.0),
            new WeightedObservedPoint(4.5, 15.0),
            new WeightedObservedPoint(5.0, 20.0)
        };

        fitter.addObservedPoints(observations);
        fitter.fit(initialGuess);
    }
}

class LevenbergMarquardtOptimizer extends NonLinearLeastSquaresOptimizer {
    /**
     * Constructor.
     */
    public LevenbergMarquardtOptimizer() {
        super();
    }
}