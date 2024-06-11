import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

import static org.junit.Assert.*;

public class GaussianFitterTest {
    private GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
    }

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] result = fitter.fit(initialGuess);
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        double[] result = fitter.fit();
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserConstructorWithNullObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserConstructorWithTooFewObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    }

    @Test
    public void testParameterGuesserGuess() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[10];
        for (int i = 0; i < 10; i++) {
            observations[i] = new WeightedObservedPoint(1.0, i, i);
        }
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] guess = guesser.guess();
        assertNotNull(guess);
        assertEquals(3, guess.length);
    }

    @Test(expected = OutOfRangeException.class)
    public void testParameterGuesserInterpolateXAtYOutOfRange() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[10];
        for (int i = 0; i < 10; i++) {
            observations[i] = new WeightedObservedPoint(1.0, i, i);
        }
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        guesser.interpolateXAtY(observations, 0, 1, 11.0);
    }

    @Test(expected = ZeroException.class)
    public void testParameterGuesserInterpolateXAtYZero() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[10];
        for (int i = 0; i < 10; i++) {
            observations[i] = new WeightedObservedPoint(1.0, i, i);
        }
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        guesser.interpolateXAtY(observations, 0, 0, 5.0);
    }

    @Test
    public void testParameterGuesserIsBetweenWithinRange() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        assertTrue(guesser.isBetween(2.0, 1.0, 3.0));
    }

    @Test
    public void testParameterGuesserIsBetweenEqualToBoundary() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        assertTrue(guesser.isBetween(2.0, 2.0, 3.0));
    }

    @Test
    public void testParameterGuesserIsBetweenOutsideRange() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        assertFalse(guesser.isBetween(2.0, 3.0, 4.0));
    }
}