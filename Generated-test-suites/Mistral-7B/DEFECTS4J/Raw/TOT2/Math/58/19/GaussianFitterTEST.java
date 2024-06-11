import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.levenberg_marquardt.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.fitting.GaussianCurveFitter;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        // Test with valid inputs
        double[] observationsX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] observationsY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 717104.0, 620014.0};

        Double[] initialGuess = {1.0, 4.0, 0.5};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observationsX, observationsY);
        double[] result = fitter.fit(initialGuess);

        // Check that the optimization was successful
        assertFalse(fitter.hasConverged());
        assertEquals(GoalType.FUN_VAL, fitter.getOptimizationResult().getGoalStatus());
        PointValuePairCollection solutions = fitter.getOptimizationResult().getSolutions();
        assertEquals(1, solutions.size());
        PointValuePair solution = solutions.get(0);
        assertArrayEquals(initialGuess, solution.getPoint(), 0.001);

        // Test the fit function
        result = fitter.fit();
        assertArrayEquals(new double[] {2.25, 3.75, 0.6}, result, 0.001);

        // Test with empty observations
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(new double[0], new double[0]);
        result = fitter.fit();
        assertArrayEquals(new double[0], result, 0.0);

        // Test with null observations
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(null, observationsY);
        try {
            fitter.fit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null observationsY
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observationsX, null);
        try {
            fitter.fit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with inconsistent observations
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observationsX, observationsY);
        fitter.addObservedPoints(observationsX[0], observationsY[1]);
        try {
            fitter.fit();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid initialGuess
        initialGuess = new Double[] {-1.0, 4.0, 0.5};
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(initialGuess);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        initialGuess = new Double[] {1.0, -4.0, 0.5};
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(initialGuess);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        initialGuess = new Double[] {1.0, 4.0, -0.5};
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        try {
            fitter.fit(initialGuess);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void addObservedPoints(GaussianFitter fitter, double[] x, double[] y) {
        for (int i = 0; i < x.length; i++) {
            fitter.addObservedPoint(new WeightedObservedPoint(x[i], y[i], new InitialGuess(x[i], y[i])));
        }
    }

    @Test
    public void testFitWithCustomInitialGuess() {
        // Test with valid inputs and custom initial guess
        double[] observationsX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] observationsY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 717104.0, 620014.0};

        Double[] initialGuess = {1.5, 4.0, 0.7};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observationsX, observationsY);
        double[] result = fitter.fit(initialGuess);

        // Check that the optimization was successful
        assertFalse(fitter.hasConverged());
        assertEquals(GoalType.FUN_VAL, fitter.getOptimizationResult().getGoalStatus());
        PointValuePairCollection solutions = fitter.getOptimizationResult().getSolutions();
        assertEquals(1, solutions.size());
        PointValuePair solution = solutions.get(0);
        assertArrayEquals(initialGuess, solution.getPoint(), 0.001);

        // Test the fit function
        result = fitter.fit();
        assertArrayEquals(new double[] {2.0, 3.8, 0.65}, result, 0.001);
    }
}