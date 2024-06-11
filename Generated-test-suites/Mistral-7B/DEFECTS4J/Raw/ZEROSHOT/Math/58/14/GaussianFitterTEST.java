package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussianFunction;
import org.apache.commons.math3.optim.nonlinear.LocalMinimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundsMinimizer;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitter with simple data.
     */
    @Test
    public void testSimpleFit() {
        double[] initGuess = { 1.0, 2.0, 1.0 };
        GaussianFitter fitter = new GaussianFitter(new SimpleBoundsMinimizer(new LocalMinimizer(new GaussNewtonOptimizer())));
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 4.2, 1.0),
            new WeightedObservedPoint(2.0, 11.2, 1.0),
            new WeightedObservedPoint(3.0, 18.5, 1.0),
            new WeightedObservedPoint(4.0, 27.1, 1.0),
            new WeightedObservedPoint(5.0, 37.8, 1.0)
        };
        double[] result = fitter.fit(initGuess);
        UnivariateFunction g = new GaussianFunction(result[0], result[1], result[2]);
        double error = 0.0;
        for (WeightedObservedPoint p : points) {
            error += Math.pow(p.getY() - g.value(p.getX()), 2.0);
        }
        error = Math.sqrt(error / points.length);
        System.out.printf("RMSE = %.6f\n", error);
        for (int i = 0; i < 5; i++) {
            double x = i + 0.5;
            double y = g.value(x);
            double yExpected = points[i].getY();
            double errorX = Math.abs(x - points[i].getX());
            double tolerance = 0.01 * Math.max(Math.abs(x), Math.abs(y));
            assertTrue(Precision.absOrRel(y, yExpected, tolerance));
            assertTrue(Precision.absOrRel(x, points[i].getX(), tolerance));
            assertTrue(Precision.absOrRel(g.value(x), y, tolerance));
        }
    }

    /**
     * Test fitter with complex data.
     */
    @Test
    public void testComplexFit() {
        double[] initGuess = { 1.0, 2.0, 1.0 };
        GaussianFitter fitter = new GaussianFitter(new SimpleBoundsMinimizer(new LocalMinimizer(new BFGSMinimizer())));
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 100.2, 1.0),
            new WeightedObservedPoint(2.0, 152.7, 2.0),
            new WeightedObservedPoint(3.0, 255.3, 3.0),
            new WeightedObservedPoint(4.0, 438.5, 5.0),
            new WeightedObservedPoint(5.0, 888.8, 10.0),
            new WeightedObservedPoint(6.0, 1577.1, 20.0),
            new WeightedObservedPoint(7.0, 2804.6, 50.0),
            new WeightedObservedPoint(8.0, 5992.7, 100.0),
            new WeightedObservedPoint(9.0, 14555.5, 200.0)
        };
        double[] result = fitter.fit(initGuess);
        UnivariateFunction g = new GaussianFunction(result[0], result[1], result[2]);
        double error = 0.0;
        for (WeightedObservedPoint p : points) {
            error += Math.pow(p.getY() - g.value(p.getX()), 2.0);
        }
        error = Math.sqrt(error / points.length);
        System.out.printf("RMSE = %.6f\n", error);
        for (int i = 0; i < 9; i++) {
            double x = i + 1.0;
            double y = g.value(x);
            double yExpected = points[i].getY();
            double errorX = Math.abs(x - points[i].getX());
            double tolerance = 0.01 * Math.max(Math.abs(x), Math.abs(y));
            assertTrue(Precision.absOrRel(y, yExpected, tolerance));
            assertTrue(Precision.absOrRel(x, points[i].getX(), tolerance));
            assertTrue(Precision.absOrRel(g.value(x), y, tolerance));
        }
        // Check means and standard deviations
        double[] means = new double[10];
        double[] sigmas = new double[10];
        for (int i = 0; i < 10; i++) {
            double x = i + 1.0;
            means[i] = x;
            sigmas[i] = FunctionUtils.sqrt(g.value(x) * g.value(x) + g.getValueStandardDeviation(x) * g.getValueStandardDeviation(x));
        }
        double[] meansExpected = { 2.547277, 3.527103, 4.459144, 5.352018, 6.224571, 7.084693, 7.949751, 8.836699, 9.754793 };
        assertArrayEquals(meansExpected, means, 0.01);
        double[] sigmasExpected = { 0.698924, 0.989263, 1.288851, 1.602085, 1.925806, 2.263853, 2.616167, 3.000039, 3.414771 };
        assertArrayEquals(sigmasExpected, sigmas, 0.01);
    }
}