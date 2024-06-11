import static org.junit.Assert.*;

import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Test fitting with initial guess
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);

        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Test fitting without initial guess
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();

        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesser() {
        // Test ParameterGuesser
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 1.0, 0.1);
        points[1] = new WeightedObservedPoint(2.0, 2.0, 0.2);
        points[2] = new WeightedObservedPoint(3.0, 3.0, 0.3);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParameters = guesser.guess();

        assertNotNull(guessedParameters);
        assertEquals(3, guessedParameters.length);
    }
}