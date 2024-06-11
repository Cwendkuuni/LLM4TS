import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Test fitting a Gaussian function with initial guess values
        // and assert the returned parameters
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);

        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more specific asserts for parameters here
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Test fitting a Gaussian function without initial guess
        // and assert the returned parameters
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();

        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more specific asserts for parameters here
    }

    @Test
    public void testParameterGuesser() {
        // Test ParameterGuesser class by providing observed points
        // and asserting the guessed parameters
        WeightedObservedPoint[] observedPoints = {new WeightedObservedPoint(1.0, 2.0, 0.1)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] guessedParameters = guesser.guess();

        assertNotNull(guessedParameters);
        assertEquals(3, guessedParameters.length);
        // Add more specific asserts for guessedParameters here
    }

    // Add more test cases for other methods and edge cases here

}