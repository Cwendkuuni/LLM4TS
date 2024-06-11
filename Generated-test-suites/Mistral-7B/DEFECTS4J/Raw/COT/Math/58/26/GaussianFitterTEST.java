package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.initialization.Guess;
import org.apache.commons.math3.optim.initialization.ParameterGuesser;
import org.apache.commons.math3.optim.initialization.SimpleBounds;
import org.apache.commons.math3.optim.initialization.ZeroInitialGuess;
import org.apache.commons.math3.optim.nonlinear.ScaledVectorFunction;
import org.apache.commons.math3.optim.nonlinear.VectorFunction;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.points.RealVectorValuePair;
import org.apache.commons.math3.optim.initialization.InitialGuess;
import org.apache.commons.math3.optim.initialization.WeightedObservedPoint;
import org.apache.commons.math3.optim.nonlinear.GradVectorFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleVectorFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialValueConvergenceException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    private static ParametricUnivariateRealFunction function;
    private static WeightedObservedPoint[] observations;

    @BeforeClass
    public static void setUpBeforeClass() {
        function = new Gaussian.Parametric();
        observations = new WeightedObservedPoint[]{
                new WeightedObservedPoint(4.0254623, 531026.0),
                new WeightedObservedPoint(4.03128248, 984167.0),
                new WeightedObservedPoint(4.03839603, 1887233.0),
                new WeightedObservedPoint(4.04421621, 2687152.0),
                new WeightedObservedPoint(4.05132976, 3461228.0),
                new WeightedObservedPoint(4.05326982, 3580526.0),
                new WeightedObservedPoint(4.05779662, 3439750.0),
                new WeightedObservedPoint(4.0636168, 2877648.0),
                new WeightedObservedPoint(4.06943698, 2175960.0),
                new WeightedObservedPoint(4.07525716, 1447024.0),
                new WeightedObservedPoint(4.08237071, 717104.0),
                new WeightedObservedPoint(4.08366408, 620014.0)};
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(observations);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test(expected = NullPointerException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitZeroLengthObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test
    public void testFitInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 1.0, 1.0};
        double[] parameters = fitter.fit(initialGuess, observations);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test(expected = NullPointerException.class)
    public void testFitInitialGuessNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 1.0, 1.0};
        fitter.fit(initialGuess, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitInitialGuessZeroLengthObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 1.0, 1.0};
        fitter.fit(initialGuess, new WeightedObservedPoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitInitialGuessInvalidInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {0.0, 1.0, 1.0};
        fitter.fit(initialGuess, observations);
    }

    @Test
    public void testFitParameterGuesser() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(observations);
        double[] initialGuess = ((ParameterGuesser) fitter.getParameterGuesser()).guess();
        assertNotNull(initialGuess);
        assertEquals(3, initialGuess.length);
        assertArrayEquals(parameters, initialGuess, 0.001);
    }

    @Test(expected = NullPointerException.class)
    public void testFitParameterGuesserNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(null);
        try {
            ((ParameterGuesser) fitter.getParameterGuesser()).guess();
        } catch (NullPointerException e) {
            // Expected
        }
        fail("Parameter guess should not be executed with null observations.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitParameterGuesserZeroLengthObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(new WeightedObservedPoint[0]);
        try {
            ((ParameterGuesser) fitter.getParameterGuesser()).guess();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        fail("Parameter guess should not be executed with zero-length observations.");
    }

    @Test
    public void testFitFwhmApprox() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(observations);
        double fwhmApprox = parameters[2];
        double[] initialGuess = {parameters[0], parameters[1], fwhmApprox};
        double[] parametersFromGuess = fitter.fit(initialGuess, observations);
        assertNotNull(parametersFromGuess);
        assertEquals(3, parametersFromGuess.length);
        assertArrayEquals(parameters, parametersFromGuess, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNotStrictlyPositiveException() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(observations);
        double[] initialGuess = {-1.0, parameters[1], parameters[2]};
        try {
            fitter.fit(initialGuess, observations);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}