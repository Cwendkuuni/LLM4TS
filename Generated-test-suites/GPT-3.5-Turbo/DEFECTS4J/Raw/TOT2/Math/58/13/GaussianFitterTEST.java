import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {
    
    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] initialGuess = {1, 2, 3}; // Sample initial guess values
        double[] parameters = fitter.fit(initialGuess);
        
        // Assert that parameters are not null and have correct length
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        
        // Add more specific assertions based on the expected outcome
    }
    
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] parameters = fitter.fit();
        
        // Assert that parameters are not null and have correct length
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        
        // Add more specific assertions based on the expected outcome
    }
    
    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1, 2, 1),
            new WeightedObservedPoint(2, 4, 1),
            new WeightedObservedPoint(3, 6, 1)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        
        double[] guessedParams = guesser.guess();
        
        // Add assertions based on the expected outcome of the guesser
    }

    // Add more test cases for other methods in GaussianFitter class
    
}