package org.apache.commons.math.optimization.fitting.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AverageInitialGuessGenerator;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.apache.commons.math3.optim.points.GoalType;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.initializer.WeightedObservedPoint;
import org.apache.commons.math3.optim.fitting.GaussianFitter;
import org.junit.Test;

/**
 * Test class for GaussianFitter.
 */
public class GaussianFitterTest {

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}
     * with valid arguments.
     */
    @Test
    public void testGaussianFitter() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
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

        UnivariateOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        for (WeightedObservedPoint point : points) {
            fitter.addObservedPoint(point.getX(), point.getValue());
        }
        double[] parameters = fitter.fit();
        assertEquals(3, parameters.length);
        assertEquals(531026.0, parameters[0], 0.001);
        assertEquals(4.0254623, parameters[1], 0.001);
        assertEquals(1769.295, parameters[2], 0.01);
    }

    /**
     * Test method for {@link GaussianFitter#fit(double[], double[], double[], double[], double[], double[], double[], double[], double[], double[], double[], double[], double[])}.
     */
    @Test
    public void testFit() {
        double[] initialGuess = new double[] { 2.0, 4.0, 1.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
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
        for (WeightedObservedPoint point : points) {
            fitter.addObservedPoint(point.getX(), point.getValue());
        }
        double[] parameters = fitter.fit(initialGuess);
        double[] expectedParameters = new double[] { 531026.0, 4.0254623, 1769.295 };
        assertArrayEquals(expectedParameters, parameters, 0.01);
    }

    /**
     * Test method for {@link GaussianFitter#fit()} with valid arguments.
     */
    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = new double[] { 2.0, 4.0, 1.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
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
        for (WeightedObservedPoint point : points) {
            fitter.addObservedPoint(point.getX(), point.getValue());
        }
        double[] parameters = fitter.fit();
        double[] expectedParameters = new double[] { 531026.0, 4.0254623, 1769.295 };
        assertArrayEquals(expectedParameters, parameters, 0.01);
    }

    /**
     * Test method for {@link GaussianFitter#fit()} with {@link NotStrictlyPositiveException}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeInitialGuess() {
        double[] initialGuess = new double[] { -2.0, 4.0, 1.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
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
        for (WeightedObservedPoint point : points) {
            fitter.addObservedPoint(point.getX(), point.getValue());
        }
        fitter.fit(initialGuess);
    }
}