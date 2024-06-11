package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.initialization.Guess;
import org.apache.commons.math3.optim.initialization.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundsVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.value.VectorValue;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.points.WeightedObservedPoint;
import org.apache.commons.math3.optim.vector.RealVector;
import org.apache.commons.math3.optim.vector.StandardRealVector;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Tests for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a Gaussian function to a set of points.
     */
    @Test
    public void testFitGaussian() {
        // Test with initial guess
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[] { 1.0, 1.0, 1.0 };
        double[] expectedResult = new double[] { 3.0, 2.5, 0.5 };
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(expectedResult, result, 1e-12);

        // Test without initial guess
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        result = fitter.fit();
        assertNotNull(result);
        assertArrayEquals(expectedResult, result, 1e-12);

        // Test with invalid initial guess
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        initialGuess = new double[] { 1.0, -1.0, 1.0 };
        try {
            result = fitter.fit(initialGuess);
            fail("IllegalArgumentException expected");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with empty data
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            result = fitter.fit();
            fail("EmptyObservedPointException expected");
        } catch (EmptyObservedPointException e) {
            // Expected
        }

        // Test with too few data
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.0, 5.0),
            new WeightedObservedPoint(3.0, 8.0),
        };
        try {
            result = fitter.fit(data);
            fail("GaussianFittingException expected");
        } catch (GaussianFittingException e) {
            // Expected
        }

        // Test with invalid data
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.0, Double.NaN),
            new WeightedObservedPoint(3.0, 9.0),
        };
        try {
            result = fitter.fit(data);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with data without weights
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.0, 5.0),
            new WeightedObservedPoint(3.0, 8.0),
            new WeightedObservedPoint(4.0, 11.0),
            new WeightedObservedPoint(5.0, 13.0),
        };
        result = fitter.fit(data);
        assertNotNull(result);
        assertArrayEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test guessing initial parameters.
     */
    @Test
    public void testGuessParameters() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.0, 5.0),
            new WeightedObservedPoint(3.0, 8.0),
            new WeightedObservedPoint(4.0, 11.0),
            new WeightedObservedPoint(5.0, 13.0),
        };
        double[] initialGuess = fitter.getParameterGuesser().guess(data);
        double[] expectedResult = new double[] { 13.0, 3.5, 1.5 };
        assertArrayEquals(expectedResult, initialGuess, 1e-12);
    }

    /**
     * Test optimizer.
     */
    @Test
    public void testOptimizer() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] x = new double[] { 1.0, 2.0, 3.0 };
        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = new Gaussian.Parametric().value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }

            @Override
            public double[] gradient(double x, double[] p) {
                double[] v = new double[3];
                try {
                    v = new Gaussian.Parametric().gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };
        RealVector initialGuess = new StandardRealVector(3, 3.0);
        VectorValue<Double> expectedResult = new VectorValue<Double>(new StandardRealVector(3, 3.0), 3.0);
        VectorValue<Double> result = fitter.optimize(f, initialGuess, null, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test optimizer with bounds.
     */
    @Test
    public void testOptimizerWithBounds() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] x = new double[] { 1.0, 2.0, 3.0 };
        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = new Gaussian.Parametric().value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }

            @Override
            public double[] gradient(double x, double[] p) {
                double[] v = new double[3];
                try {
                    v = new Gaussian.Parametric().gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };
        RealVector initialGuess = new StandardRealVector(3, 3.0);
        SimpleBoundsVectorValuePair bounds = new SimpleBoundsVectorValuePair(
                new SimpleBounds(0.0, 10.0, 3),
                new SimpleBounds(0.0, 10.0, 3)
        );
        VectorValue<Double> expectedResult = new VectorValue<Double>(new StandardRealVector(3, 3.0), 3.0);
        VectorValue<Double> result = fitter.optimize(f, initialGuess, bounds, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test optimizer with initial guess and bounds.
     */
    @Test
    public void testOptimizerWithGuessAndBounds() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] x = new double[] { 1.0, 2.0, 3.0 };
        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = new Gaussian.Parametric().value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }

            @Override
            public double[] gradient(double x, double[] p) {
                double[] v = new double[3];
                try {
                    v = new Gaussian.Parametric().gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };
        RealVector initialGuess = new StandardRealVector(3, 4.0);
        SimpleBoundsVectorValuePair bounds = new SimpleBoundsVectorValuePair(
                new SimpleBounds(0.0, 10.0, 3),
                new SimpleBounds(0.0, 10.0, 3)
        );
        VectorValue<Double> expectedResult = new VectorValue<Double>(new StandardRealVector(3, 3.0), 3.0);
        VectorValue<Double> result = fitter.optimize(f, initialGuess, bounds, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test optimizer with bounds only.
     */
    @Test
    public void testOptimizerWithBoundsOnly() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] x = new double[] { 1.0, 2.0, 3.0 };
        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = new Gaussian.Parametric().value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }

            @Override
            public double[] gradient(double x, double[] p) {
                double[] v = new double[3];
                try {
                    v = new Gaussian.Parametric().gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };
        SimpleBoundsVectorValuePair bounds = new SimpleBoundsVectorValuePair(
                new SimpleBounds(0.0, 10.0, 3),
                new SimpleBounds(0.0, 10.0, 3)
        );
        VectorValue<Double> expectedResult = new VectorValue<Double>(new StandardRealVector(3, 3.0), 3.0);
        VectorValue<Double> result = fitter.optimize(f, null, bounds, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test optimizer with initial guess, bounds and guess type.
     */
    @Test
    public void testOptimizerWithGuessAndBoundsAndType() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] x = new double[] { 1.0, 2.0, 3.0 };
        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = new Gaussian.Parametric().value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }

            @Override
            public double[] gradient(double x, double[] p) {
                double[] v = new double[3];
                try {
                    v = new Gaussian.Parametric().gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };
        RealVector initialGuess = new StandardRealVector(3, 4.0);
        SimpleBoundsVectorValuePair bounds = new SimpleBoundsVectorValuePair(
                new SimpleBounds(0.0, 10.0, 3),
                new SimpleBounds(0.0, 10.0, 3)
        );
        Guess guess = new Guess(initialGuess);
        VectorValue<Double> expectedResult = new VectorValue<Double>(new StandardRealVector(3, 3.0), 3.0);
        VectorValue<Double> result = fitter.optimize(f, guess, bounds, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }
}