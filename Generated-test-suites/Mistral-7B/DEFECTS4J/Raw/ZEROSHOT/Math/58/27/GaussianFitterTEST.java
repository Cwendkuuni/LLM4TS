package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.points.WeightedObservedPoint;
import org.junit.Test;

/**
 * Tests for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * and {@link GaussianFitter#fit()}.
     */
    @Test
    public void testFitDefaultOptimizer() {
        final WeightedObservedPoint[] points = new WeightedObservedPoint[] {
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

        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(points);
        final double[] parameters = fitter.fit();
        final double norm = parameters[0];
        final double mean = parameters[1];
        final double sigma = parameters[2];
        final double expectedNorm = 4152166.78841397;
        final double expectedMean = 4.0636168;
        final double expectedSigma = 0.15555464;
        assertEquals(expectedNorm, norm, 1e-12);
        assertEquals(expectedMean, mean, 1e-12);
        assertEquals(expectedSigma, sigma, 1e-12);
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * and {@link GaussianFitter#fit(double[])}.
     */
    @Test
    public void testFitCustomizedInitGuess() {
        final WeightedObservedPoint[] points = new WeightedObservedPoint[] {
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

        final double[] initialGuess = { 4150000.0, 4.06, 0.17 };

        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(points);
        final PointValuePair result = fitter.optimize(new ParametricUnivariateRealFunction() {
            public double value(double x, double[] p) {
                final Gaussian g = new Gaussian.Parametric();
                return g.value(x, p) - initialGuess[0];
            }

            public double[] gradient(double x, double[] p) {
                final Gaussian g = new Gaussian.Parametric();
                return g.gradient(x, p);
            }
        }, new InitialGuess(initialGuess), new MaxEval(300), new SimpleBounds(new double[] { -2.0, -2.0, -2.0 }, new double[] { 2.0, 2.0, 2.0 }));
        final double[] parameters = result.getPoint();
        assertEquals(3, result.getIterations());
        final double norm = parameters[0];
        final double mean = parameters[1];
        final double sigma = parameters[2];
        final double expectedNorm = 4152166.78841397;
        final double expectedMean = 4.0636168;
        final double expectedSigma = 0.15555464;
        assertEquals(expectedNorm, norm, 1e-12);
        assertEquals(expectedMean, mean, 1e-12);
        assertEquals(expectedSigma, sigma, 1e-12);
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullOptimizer() {
        new GaussianFitter(null);
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * and {@link GaussianFitter#addObservedPoints(WeightedObservedPoint[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyObservedPoints() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(new WeightedObservedPoint[] {});
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * and {@link GaussianFitter#addObservedPoint(WeightedObservedPoint)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyObservedPoint() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(new WeightedObservedPoint[] {});
        fitter.addObservedPoint(null);
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * and {@link GaussianFitter#addObservedPoint(WeightedObservedPoint)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testAddObservedPointWithNegativeWeight() {
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(new WeightedObservedPoint[] {});
        fitter.addObservedPoint(new WeightedObservedPoint(-5.0, 0.0, -1.0));
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * and {@link GaussianFitter#optimize(ParametricUnivariateRealFunction, InitialGuess, MaxEval, SimpleBounds)}.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testOptimizeOutOfBounds() {
        final GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        final ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            public double value(double x, double[] p) {
                final Gaussian g = new Gaussian.Parametric();
                return g.value(x, p);
            }

            public double[] gradient(double x, double[] p) {
                final Gaussian g = new Gaussian.Parametric();
                return g.gradient(x, p);
            }
        };
        final InitialGuess initialGuess = new InitialGuess(new double[] { 1.0, 1.0, 1.0 });
        final MaxEval maxEval = new MaxEval(1);
        final SimpleBounds bounds = new SimpleBounds(new double[] { -100.0, -100.0, -100.0 }, new double[] { 100.0, 100.0, 100.0 });
        fitter.optimize(f, initialGuess, maxEval, bounds);
    }
}