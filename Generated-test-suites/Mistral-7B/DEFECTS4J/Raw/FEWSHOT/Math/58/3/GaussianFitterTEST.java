package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.OptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.stochastic.MonteCarloOptimizer;
import org.apache.commons.math3.optim.nonlinear.stochastic.SimulatedAnnealingOptimizer;
import org.apache.commons.math3.optim.nonlinear.stochastic.SimpleStochasticOptimizer;
import org.apache.commons.math3.optim.nonlinear.stochastic.StochasticOptimizer;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.restart.RestartStrategy;
import org.apache.commons.math3.optim.restart.SimpleRestart;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class GaussianFitterTest {
    private static final double EPSILON = 1.0e-12;
    private static final double SMALL_VALUE = 1.0e-20;

    @Test
    public void testFitterGaussianFunction() {
        // Testing fit method with initial guess
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(2.0, 4.0, 2.0),
            new WeightedObservedPoint(3.0, 9.0, 3.0),
            new WeightedObservedPoint(4.0, 16.0, 4.0),
            new WeightedObservedPoint(5.0, 25.0, 5.0)
        };

        final GaussianFitter fitter = new GaussianFitter(new SimulatedAnnealingOptimizer(100.0));
        double[] initialGuess = new double[] { 2.0, 3.0, 1.0 };
        double[] parameters = fitter.fit(observations, initialGuess);

        assertEquals(5.0, parameters[0], EPSILON);
        assertEquals(3.0, parameters[1], EPSILON);
        assertEquals(1.5, parameters[2], EPSILON);

        // Testing fit method without initial guess
        double[] parametersWithoutInitialGuess = fitter.fit(observations);
        assertEquals(5.0, parametersWithoutInitialGuess[0], EPSILON);
        assertEquals(3.0, parametersWithoutInitialGuess[1], EPSILON);
        assertEquals(1.5, parametersWithoutInitialGuess[2], EPSILON);

        // Testing fit method with different optimizers
        StochasticOptimizer optimizer1 = new MonteCarloOptimizer(1000, new SimpleRestart(100));
        StochasticOptimizer optimizer2 = new SimpleStochasticOptimizer(new MaxEval(100));
        GaussianFitter fitter1 = new GaussianFitter(optimizer1);
        GaussianFitter fitter2 = new GaussianFitter(optimizer2);

        double[] parametersFromOptimizer1 = fitter1.fit(observations);
        double[] parametersFromOptimizer2 = fitter2.fit(observations);

        assertArrayEquals(parameters, parametersFromOptimizer1, EPSILON);
        assertArrayEquals(parameters, parametersFromOptimizer2, EPSILON);

        // Testing throw exception when no observations provided
        try {
            fitter.fit();
            fail("Expected exception when no observations are provided.");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Testing throw exception when observations array is empty
        try {
            GaussianFitter fitterEmpty = new GaussianFitter(optimizer1);
            fitterEmpty.fit(new WeightedObservedPoint[0]);
            fail("Expected exception when observations array is empty.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Testing throw exception when the optimizer is null
        try {
            GaussianFitter fitterNull = new GaussianFitter(null);
            fitterNull.fit(observations);
            fail("Expected exception when optimizer is null.");
        } catch (NullPointerException e) {
            // Expected
        }

        // Testing throw exception when observations array contains null elements
        WeightedObservedPoint[] observationsWithNull = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(2.0, null, 2.0),
            new WeightedObservedPoint(3.0, 9.0, 3.0)
        };

        try {
            fitter.fit(observationsWithNull);
            fail("Expected exception when observations array contains null elements.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFitterFunctionAndGradient() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(2.0, 4.0, 2.0),
            new WeightedObservedPoint(3.0, 9.0, 3.0),
            new WeightedObservedPoint(4.0, 16.0, 4.0),
            new WeightedObservedPoint(5.0, 25.0, 5.0)
        };

        final GaussianFitter fitter = new GaussianFitter(new SimulatedAnnealingOptimizer(100.0));
        double[] initialGuess = new double[] { 2.0, 3.0, 1.0 };
        double[] parameters = fitter.fit(observations, initialGuess);

        ParametricUnivariateRealFunction function = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                return new Gaussian.Parametric().value(x, p);
            }

            @Override
            public double[] gradient(double x, double[] p) {
                return new Gaussian.Parametric().gradient(x, p);
            }
        };

        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public double value(double[] p) {
                return fitter.objectiveFunction(function, observations, p)[0];
            }

            @Override
            public double[] gradient(double[] p) {
                double[] grad = new double[3];
                for (int i = 0; i < 3; i++) {
                    double[] pNew = p.clone();
                    pNew[i] += Precision.SAFE_EPSILON;
                    double[] temp = fitter.objectiveFunction(function, observations, pNew);
                    grad[i] = temp[0] - value(p);
                }
                return grad;
            }
        };

        PointValuePairList result = new PointValuePairList();
        StochasticOptimizer optimizer = new MonteCarloOptimizer(1000, new SimpleRestart(100));
        optimizer.optimize(problem, result);

        assertArrayEquals(parameters, result.getPoint(0), EPSILON);
    }
}