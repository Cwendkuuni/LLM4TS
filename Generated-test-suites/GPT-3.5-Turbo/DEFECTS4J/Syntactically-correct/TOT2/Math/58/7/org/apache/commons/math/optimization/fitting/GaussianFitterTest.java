import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Test fitting with initial guess
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        
        double[] parameters = fitter.fit(initialGuess);
        
        // Assert the parameters
        assertEquals(1.0, parameters[0], 0.01);
        assertEquals(0.0, parameters[1], 0.01);
        assertEquals(1.0, parameters[2], 0.01);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Test fitting without initial guess
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        
        double[] parameters = fitter.fit();
        
        // Assert the parameters
        assertNotNull(parameters);
    }

    @Test
    public void testParameterGuesser() {
        // Test ParameterGuesser class
        WeightedObservedPoint[] points = {new WeightedObservedPoint(1.0, 0.0, 0.1),
                                          new WeightedObservedPoint(2.0, 1.0, 0.2),
                                          new WeightedObservedPoint(3.0, 2.0, 0.3)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        
        double[] guessedParams = guesser.guess();
        
        // Assert the guessed parameters
        assertNotNull(guessedParams);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserNullArgument() {
        // Test handling NullArgumentException in ParameterGuesser
        WeightedObservedPoint[] points = null;
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        
        double[] guessedParams = guesser.guess();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserTooFewPoints() {
        // Test handling NumberIsTooSmallException in ParameterGuesser
        WeightedObservedPoint[] points = {new WeightedObservedPoint(1.0, 0.0, 0.1)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        
        double[] guessedParams = guesser.guess();
    }
}