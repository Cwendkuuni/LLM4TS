package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizationException;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.optim.points.WelchPointsGenerator;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test cases for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Tests fitting a Gaussian function to a set of observed points.
     */
    @Test
    public void testFit() {
        // Given:
        double[][] observations = new double[][] {
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
        WeightedObservedPoint[] points = new WeightedObservedPoint[observations.length];
        for (int i = 0; i < observations.length; i++) {
            points[i] = new WeightedObservedPoint(observations[i][0], observations[i][1], 1.0);
        }

        // When:
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(points);
        double[] result = fitter.fit();

        // Then:
        assertNotNull(result);
        ParametricUnivariateRealFunction g = new Gaussian.Parametric();
        assertEquals(3, result.length);
        assertArrayEquals(new double[]{ result[0], result[1], result[2] },
                         g.estimateParameters(points, new MaxEval(1000)), 1e-6);
    }

    /**
     * Tests guessing the initial parameters for a Gaussian function.
     */
    @Test
    public void testGuess() {
        // Given:
        double[][] observations = new double[][] {
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
        WeightedObservedPoint[] points = new WeightedObservedPoint[observations.length];
        for (int i = 0; i < observations.length; i++) {
            points[i] = new WeightedObservedPoint(observations[i][0], observations[i][1], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(points);

        // When:
        double[] initialGuess = fitter.guess();

        // Then:
        assertNotNull(initialGuess);
        assertEquals(3, initialGuess.length);
    }

    /**
     * Tests the optimization function using Levenberg-Marquardt algorithm.
     */
    @Test
    public void testOptimizationFunction() throws NonlinearOptimizationException {
        // Given:
        double[][] observations = new double[][] {
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
        WeightedObservedPoint[] points = new WeightedObservedPoint[observations.length];
        for (int i = 0; i < observations.length; i++) {
            points[i] = new WeightedObservedPoint(observations[i][0], observations[i][1], 1.0);
        }
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(points);

        // When:
        double[] initialGuess = fitter.guess();
        NonlinearOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair result = optimizer.optimize(new ParameterFunction(), initialGuess, new SimpleBounds(1.0, Double.MAX_VALUE), new MaxEval(1000));

        // Then:
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getFunctionValue(), 1e-6);
    }

    /**
     * Test function for optimization.
     */
    static class ParameterFunction implements ParametricUnivariateRealFunction {
        @Override
        public double value(double x, double[] parameters) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double x, double[] parameters) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] estimateParameters(double[] x, int maxIterations, double stepSize) {
            return new Gaussian.Parametric().estimateParameters(x, maxIterations, stepSize);
        }
    }

    /**
     * Tests the fitting with random data.
     */
    @Test
    public void testFitRandomData() {
        // Given:
        int dataSize = 100;
        double mean = 5.0;
        double stdDev = 2.0;
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        Points2D points = new WelchPointsGenerator().generate(dataSize, mean, stdDev);

        // When:
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(points.toArray(new double[0][0]));

        // Then:
        assertEquals(3, fitter.getNumberOfParameters());
        assertNotNull(fitter.getParameters());
        ParametricUnivariateRealFunction g = new Gaussian.Parametric();
        double[] parameters = fitter.getParameters();
        double[] expectedParameters = g.estimateParameters(points.toArray(new double[0][0]), 1000, 1e-6);
        assertArrayEquals(expectedParameters, parameters, 1e-6);

        // Test standard deviation
        double[] observedValues = new double[dataSize];
        for (int i = 0; i < dataSize; i++) {
            observedValues[i] = g.value(points.getPoint(i)[0], parameters);
        }
        StandardDeviation observedStdDev = new StandardDeviation().evaluate(observedValues);
        assertEquals(stdDev, observedStdDev.getValue(), 1e-6);
    }
}