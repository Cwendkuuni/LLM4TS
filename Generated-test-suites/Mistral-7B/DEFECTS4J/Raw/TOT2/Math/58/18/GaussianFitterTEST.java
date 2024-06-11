package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedInitialGuess;
import org.apache.commons.math3.optim.initializer.GaussianInitialGuesser;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitNormalData() {
        Double[][] data = { {0.0, 2.5}, {1.0, 3.5}, {2.0, 4.5}, {3.0, 5.5}, {4.0, 6.5} };
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), data);
        DifferentiableMultivariateVectorFunction fitnessFunction = problem.getFitnessFunction();

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        PointValuePair solution = fitter.fit(new double[]{ 2.5, 2.5, 1.0 }); // initial guess: mean, norm, sigma

        assertEquals(2.5, solution.getPoint()[0], 0.01); // mean
        assertEquals(2.5, solution.getPoint()[1], 0.01); // norm
        assertEquals(1.0, solution.getPoint()[2], 0.01); // sigma

        assertEquals(0, fitnessFunction.value(0.0, solution.getPoint()), 0.01);
        assertEquals(0, fitnessFunction.value(1.0, solution.getPoint()), 0.01);
        assertEquals(0, fitnessFunction.value(2.0, solution.getPoint()), 0.01);
        assertEquals(0, fitnessFunction.value(3.0, solution.getPoint()), 0.01);
        assertEquals(0, fitnessFunction.value(4.0, solution.getPoint()), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitDataWithNullObservedPoints() {
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), null);
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new double[]{ 2.5, 2.5, 1.0 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitDataWithEmptyObservedPoints() {
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), new WeightedObservedPoint[0]);
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new double[]{ 2.5, 2.5, 1.0 });
    }

    @Test
    public void testFitDataWithInvalidInitialGuess() {
        Double[][] data = { {0.0, 2.5}, {1.0, 3.5}, {2.0, 4.5}, {3.0, 5.5}, {4.0, 6.5} };
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), data);
        DifferentiableMultivariateVectorFunction fitnessFunction = problem.getFitnessFunction();

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(new double[]{ 1.0, 1.0, -1.0 }); // invalid sigma
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        PointValuePair solution = fitter.fit(new double[]{ 2.5, 2.5, 1.0 }); // valid initial guess
        assertEquals(2.5, solution.getPoint()[0], 0.01); // mean
        assertEquals(2.5, solution.getPoint()[1], 0.01); // norm
        assertEquals(1.0, solution.getPoint()[2], 0.01); // sigma
    }

    @Test
    public void testFitDataWithZeroObservedPoints() {
        Double[][] data = { {0.0, 0.0}, {1.0, 0.0}, {2.0, 0.0}, {3.0, 0.0}, {4.0, 0.0} };
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), data);
        DifferentiableMultivariateVectorFunction fitnessFunction = problem.getFitnessFunction();

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(new double[]{ 2.5, 2.5, 1.0 }); // valid initial guess
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testFitDataWithInvalidObservedPoints() {
        Double[][] data = { {-1.0, 2.5}, {1.0, 3.5}, {2.0, 4.5}, {3.0, 5.5}, {4.0, 6.5} };
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), data);
        DifferentiableMultivariateVectorFunction fitnessFunction = problem.getFitnessFunction();

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(new double[]{ 2.5, 2.5, 1.0 }); // valid initial guess
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testFitDataWithFewerThanThreeObservedPoints() {
        Double[][] data = { {0.0, 2.5}, {1.0, 3.5} };
        OptimizationProblem problem = new LeastSquaresProblem(new GaussianFunction(2.5), data);
        DifferentiableMultivariateVectorFunction fitnessFunction = problem.getFitnessFunction();

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(new double[]{ 2.5, 2.5, 1.0 }); // valid initial guess
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] data = {
                new WeightedObservedPoint(1.0, 2.5, 1.0),
                new WeightedObservedPoint(2.0, 3.5, 2.0),
                new WeightedObservedPoint(3.0, 4.5, 3.0),
                new WeightedObservedPoint(4.0, 5.5, 4.0),
                new WeightedObservedPoint(5.0, 6.5, 5.0)
        };

        ParameterGuesser guesser = new ParameterGuesser(data);
        double[] guess = guesser.guess();

        assertEquals(4.0, guess[0], 0.01);
        assertEquals(3.0, guess[1], 0.01);
        assertEquals(1.25, guess[2], 0.01);
    }
}