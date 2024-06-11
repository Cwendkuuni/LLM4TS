package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.LocalSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.optim.restart.Restart;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.optim.statistics.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.math3.optim.InitialGuess.AUTOMATIC;
import static org.apache.commons.math3.optim.InitialGuess.RESTART_MAX_ITER;
import static org.apache.commons.math3.optim.InitialGuess.RESTART_MIN_STEP_SIZE;
import static org.apache.commons.math3.optim.InitialGuess.RESTART_STEP_SIZE;

public class GaussianFitterTest {

    private WeightedObservedPoint[] points;
    private UnivariateOptimizer optimizer;
    private double[] parameters;

    @Before
    public void setUp() {
        optimizer = new LocalSearch(new GoalType(0));
        optimizer.setMaxEval(new MaxEval(100));
        optimizer.setRestart(new Restart(RESTART_MIN_STEP_SIZE, RESTART_MAX_ITER, RESTART_STEP_SIZE));
    }

    @Test
    public void testFitGaussianFunction() {
        double[] initialGuess = { 1.0, 2.0, 0.5 };
        points = new WeightedObservedPoint[] {
                new WeightedObservedPoint(2.5, 12.5, 1.0),
                new WeightedObservedPoint(3.0, 37.0, 1.0),
                new WeightedObservedPoint(3.5, 82.0, 1.0),
                new WeightedObservedPoint(4.0, 132.0, 1.0),
                new WeightedObservedPoint(4.5, 195.0, 1.0),
                new WeightedObservedPoint(5.0, 265.0, 1.0),
                new WeightedObservedPoint(5.5, 345.0, 1.0),
                new WeightedObservedPoint(6.0, 433.0, 1.0),
                new WeightedObservedPoint(6.5, 525.0, 1.0),
                new WeightedObservedPoint(7.0, 620.0, 1.0)
        };

        GaussianFitter fitter = new GaussianFitter(optimizer);

        try {
            parameters = fitter.fit(initialGuess);
            double[] expectedParameters = { 2.0, 4.0, 0.5 };
            Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
        } catch (NumberIsTooSmallException e) {
            // This exception should not be thrown when the test is successful.
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        ObjectiveFunction gaussianFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                double norm = parameters[0];
                double mean = parameters[1];
                double sigma = parameters[2];

                Gaussian g = new Gaussian.Parametric(mean, sigma);
                return g.value(2.5) - 12.5;
            }

            @Override
            public double[] gradient(double[] parameters) {
                Double[] result = new Double[3];

                double norm = parameters[0];
                double mean = parameters[1];
                double sigma = parameters[2];

                Gaussian g = new Gaussian.Parametric(mean, sigma);
                result[0] = -g.value(2.5);
                result[1] = -g.gradient(2.5, 0)[0];
                result[2] = -g.gradient(2.5, 0)[1];

                return result;
            }
        };

        SimpleValueChecker valueChecker = new SimpleValueChecker(5e-6, 1e-6);
        SimpleRegression simpleRegression = new SimpleRegression();
        OLSMultipleLinearRegression olsMultipleLinearRegression = new OLSMultipleLinearRegression();

        double[][] pointsArray = new double[][] {
                { 2.5, 12.5 },
                { 3.0, 37.0 },
                { 3.5, 82.0 },
                { 4.0, 132.0 },
                { 4.5, 195.0 },
                { 5.0, 265.0 },
                { 5.5, 345.0 },
                { 6.0, 433.0 },
                { 6.5, 525.0 },
                { 7.0, 620.0 }
        };

        Points2D points2D = new Points2D(pointsArray);
        List<PointValuePair> solutions = optimizer.optimize(gaussianFunction, AUTOMATIC, points2D);
        double[] optimizedParameters = solutions.get(0).getPoint();

        double[] expectedGradients = { -12.5, -2.0, -0.5 };
        Assert.assertArrayEquals(expectedGradients, gaussianFunction.gradient(optimizedParameters), 0.001);

        double[] expectedRegressionCoefficients = { 2.0, 4.0, 0.5 };
        simpleRegression.fit(pointsArray, 1, optimizedParameters);
        double[] coeffs = simpleRegression.getParameters();
        Assert.assertArrayEquals(expectedRegressionCoefficients, coeffs, 0.001);

        olsMultipleLinearRegression.fit(pointsArray, 1);
        double[] olsCoeffs = olsMultipleLinearRegression.getParameters();
        Assert.assertArrayEquals(expectedRegressionCoefficients, olsCoeffs, 0.001);

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (WeightedObservedPoint point : points) {
            stats.addValue(point.getX() - mean);
        }
        double expectedStdDev = Math.sqrt(stats.getStandardDeviation());
        Assert.assertEquals(expectedStdDev, sigma, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNullObservedPoints() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservedPoints() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new WeightedObservedPoint[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithObservedPointsLessThanThree() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new WeightedObservedPoint[] { new WeightedObservedPoint(1.0, 1.0) });
    }
}