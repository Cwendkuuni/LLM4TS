import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math.exception.ManyViolationsException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.ParameterGuesser;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.junit.Before;
import org.junit.Test;

public class GaussianFitterTest {

    // Test data
    private WeightedObservedPoint[] observations;

    @Before
    public void setUp() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        observations = new WeightedObservedPoint[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            observations[i] = new WeightedObservedPoint(1.0, xValues[i], yValues[i]);
        }
    }

    @Test
    public void testConstructor() {
        DifferentiableMultivariateVectorialOptimizer optimizer = null;
        try {
            new GaussianFitter(optimizer);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
    }

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 3.0, 2.0};
        GaussianFitter fitter = new GaussianFitter(null);
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
    }

    @Test
    public void testFitWithInvalidInitialGuess() {
        double[] initialGuess = {1.0, 2.0};
        GaussianFitter fitter = new GaussianFitter(null);
        try {
            fitter.fit(initialGuess);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testParameterGuesser() {
        ParameterGuesser guesser = new ParameterGuesser(observations);
        double[] parameters = guesser.guess();
        assertNotNull(parameters);
    }

    @Test
    public void testParameterGuesserWithInvalidObservations() {
        WeightedObservedPoint[] invalidObservations = {new WeightedObservedPoint(1.0, 1.0, 1.0)};
        try {
            new ParameterGuesser(invalidObservations);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testParameterGuesserWithInvalidObservationsNull() {
        try {
            new ParameterGuesser(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}