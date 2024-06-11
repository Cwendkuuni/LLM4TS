import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.RealVectorValueFunction;
import org.apache.commons.math3.optim.points.RealVectorComparisonTools;
import org.apache.commons.math3.optim.points.RealVectorNumericalDerivative;
import org.apache.commons.math3.optim.points.ZerosChecker;
import org.apache.commons.math3.optim.restart.RestartingMethod;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.math3.optim.Points.*;
import static org.apache.commons.math3.optim.initializer.InitialGuess.IDENTITY;
import static org.apache.commons.math3.optim.restart.RestartingMethod.COARSE;
import static org.apache.commons.math3.optim.restart.RestartingMethod.FINE;

public class GaussianFitterTest {

    @Test
    public void testFitGaussian() {
        // Given
        Double[][] observations = {
                {4.0254623, 531026.0},
                {4.03128248, 984167.0},
                {4.03839603, 1887233.0},
                {4.04421621, 2687152.0},
                {4.05132976, 3461228.0},
                {4.05326982, 3580526.0},
                {4.05779662, 3439750.0},
                {4.0636168, 2877648.0},
                {4.06943698, 2175960.0},
                {4.07525716, 1447024.0},
                {4.08237071, 717104.0},
                {4.08366408, 620014.0}
        };
        WeightedObservedPoint[] weightedObservations = new WeightedObservedPoint[observations.length];
        for (int i = 0; i < observations.length; i++) {
            weightedObservations[i] = new WeightedObservedPoint(observations[i][0], observations[i][1], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // When
        double[] fitParameters = fitter.fit(weightedObservations);

        // Then
        RealVectorValueFunction gaussianFunction = new ParametricUnivariateRealFunction() {
            private final Gaussian.Parametric g = new Gaussian.Parametric();

            public double value(double x, double[] p) {
                return g.value(x, p);
            }

            public double[] gradient(double x, double[] p) {
                return g.gradient(x, p);
            }
        };

        double[] expectedParameters = {327280.8, 4.0577966, 0.135381};
        RealVectorNumericalDerivative nd = new RealVectorNumericalDerivative(expectedParameters);
        RealVectorValueFunction ndGaussianFunction = new RealVectorValueFunction(nd, gaussianFunction);
        PointValuePair pvp = new LevenbergMarquardtOptimizer().optimize(ndGaussianFunction, IDENTITY, GoalType.MINIMIZE, 1e-10, 100, new SimpleValueChecker(1e-10));
        double[] optimizedParameters = pvp.getPoint();

        // Test if fitParameters are close enough to optimizedParameters
        double tolerance = 1e-6;
        assertTrue("Fit parameters are too far from optimized parameters",
                RealVectorComparisonTools.areClose(fitParameters, optimizedParameters, tolerance));

        // Test if fit function values are close enough to observed values
        double maxDifference = 1e-4;
        for (WeightedObservedPoint wop : weightedObservations) {
            double expected = gaussianFunction.value(wop.getX(), expectedParameters);
            double actual = gaussianFunction.value(wop.getX(), fitParameters);
            assertTrue("Fit function value is too far from observed value",
                    Math.abs(expected - actual) <= maxDifference);
        }

        // Test edge cases: empty data set
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        WeightedObservedPoint[] emptyWeightedObservations = new WeightedObservedPoint[0];
        try {
            fitter.fit(emptyWeightedObservations);
            fail("Expected exception when passing empty dataset to fitter");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test edge cases: null data set
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit((WeightedObservedPoint[]) null);
            fail("Expected exception when passing null dataset to fitter");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test edge cases: dataset with no observable points
        WeightedObservedPoint[] noObservablePoints = {new WeightedObservedPoint(-1.0, 0.0, 1.0)};
        try {
            fitter.fit(noObservablePoints);
            fail("Expected exception when dataset has no observable points");
        } catch (OutOfRangeException e) {
            // Expected
        }

        // Test error scenarios: negative initial guess
        double[] negativeInitialGuess = {-1.0, 4.0577966, 0.135381};
        try {
            fitter.fit(negativeInitialGuess);
            fail("Expected exception when initial guess is negative");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test error scenarios: non-differentiable optimizer
        DifferentiableMultivariateVectorialOptimizer nonDifferentiableOptimizer = new NonDifferentiableOptimizer();
        GaussianFitter nonDifferentiableFitter = new GaussianFitter(nonDifferentiableOptimizer);
        try {
            nonDifferentiableFitter.fit(weightedObservations);
            fail("Expected exception when optimizer is not differentiable");
        } catch (RuntimeException e) {
            // Expected
        }

        // Test error scenarios: non-optimizable function
        RealVectorValueFunction nonOptimizableFunction = new RealVectorValueFunction() {
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };
        GaussianFitter nonOptimizableFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            nonOptimizableFitter.fit(weightedObservations, nonOptimizableFunction);
            fail("Expected exception when function is not optimizable");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test fit method with custom guess
        double[] customGuess = {300000.0, 4.0577966, 0.2};
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] fitParametersCustom = fitter.fit(weightedObservations, customGuess);
        double toleranceCustom = 1e-4;
        assertTrue("Fit parameters are too far from optimized parameters",
                RealVectorComparisonTools.areClose(fitParametersCustom, optimizedParameters, toleranceCustom));

        // Test fit method with custom optimizer
        DifferentiableMultivariateVectorialOptimizer customOptimizer = new CustomOptimizer();
        GaussianFitter customOptimizerFitter = new GaussianFitter(customOptimizer);
        double[] fitParametersCustomOptimizer = customOptimizerFitter.fit(weightedObservations);
        double toleranceCustomOptimizer = 1e-4;
        assertTrue("Fit parameters are too far from optimized parameters",
                RealVectorComparisonTools.areClose(fitParametersCustomOptimizer, optimizedParameters, toleranceCustomCustomOptimizer));

        // Test fit method with custom restart method
        RestartingMethod customRestartMethod = new RestartingMethod(FINE, COARSE);
        GaussianFitter customRestartMethodFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] fitParametersCustomRestartMethod = customRestartMethodFitter.fit(weightedObservations, customRestartMethod);
        double toleranceCustomRestartMethod = 1e-4;
        assertTrue("Fit parameters are too far from optimized parameters",
                RealVectorComparisonTools.areClose(fitParametersCustomRestartMethod, optimizedParameters, toleranceCustomRestartMethod));
    }
}

class CustomOptimizer implements DifferentiableMultivariateVectorialOptimizer {
    @Override
    public PointValuePair optimize(RealVectorFunction function, RealVectorInitialGuess initialGuess, GoalType goalType, double tolerance, int iterations, ValueChecker valueChecker) {
        throw new UnsupportedOperationException();
    }
}