package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.optimize.GoalType;
import org.apache.commons.math3.optim.nonlinear.optimize.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.optimize.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.optimize.WeightedObservedPoint;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        double[] initialGuess = new double[]{1.0, 2.5, 0.5};
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        WeightedObservedPoint[] observations = new WeightedObservedPoint[]{
                new WeightedObservedPoint(2.5, 1.0),
                new WeightedObservedPoint(3.0, 3.5),
                new WeightedObservedPoint(3.5, 12.5),
                new WeightedObservedPoint(4.0, 25.0),
                new WeightedObservedPoint(4.5, 40.5)};

        fitter.addObservedPoints(observations);
        double[] result = fitter.fit(initialGuess);

        ParametricUnivariateRealFunction gaussian = new Gaussian.Parametric();
        PointValuePair bestSolution = new LevenbergMarquardtOptimizer()
                .optimize(gaussian, initialGuess, observations, new MaxEval(100),
                        GoalType.MINIMIZE, new SimpleBounds(0.0, Double.POSITIVE_INFINITY, 3));
        double[] bestSolutionParams = bestSolution.getPoint();

        assertArrayEquals(bestSolutionParams, result, 0.001);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testFitWithUnboundedDomain() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[]{
                new WeightedObservedPoint(Double.NEGATIVE_INFINITY, 1.0),
                new WeightedObservedPoint(1.0, 3.5),
                new WeightedObservedPoint(Double.POSITIVE_INFINITY, 25.0)};

        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[]{-1.0, 2.5, 0.5};

        WeightedObservedPoint[] observations = new WeightedObservedPoint[]{
                new WeightedObservedPoint(2.5, 1.0),
                new WeightedObservedPoint(3.0, 3.5),
                new WeightedObservedPoint(3.5, 12.5),
                new WeightedObservedPoint(4.0, 25.0),
                new WeightedObservedPoint(4.5, 40.5)};

        fitter.addObservedPoints(observations);
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitWithEmptyObservationList() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        fitter.fit();
        assertNull(fitter.getParameters());
    }
}