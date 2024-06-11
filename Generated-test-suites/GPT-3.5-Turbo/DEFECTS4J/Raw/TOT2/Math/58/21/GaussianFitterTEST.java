import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more assertions as needed
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more assertions as needed
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(2.0, 2.0, 1.0),
            new WeightedObservedPoint(3.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParameters = guesser.guess();
        assertNotNull(guessedParameters);
        assertEquals(3, guessedParameters.length);
        // Add more assertions as needed
    }

    // Add more test cases for edge cases and exception handling scenarios

}