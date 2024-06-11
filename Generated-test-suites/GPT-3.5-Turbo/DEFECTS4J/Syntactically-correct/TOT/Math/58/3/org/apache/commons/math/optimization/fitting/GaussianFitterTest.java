import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 2.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Test typical use case
        // Test that the norm value is within a certain range
        assertTrue(parameters[0] >= 0.0 && parameters[0] <= Double.MAX_VALUE);
        // Test that the mean value is within a certain range
        assertTrue(parameters[1] >= Double.MIN_VALUE && parameters[1] <= Double.MAX_VALUE);
        // Test that the sigma value is within a certain range
        assertTrue(parameters[2] >= Double.MIN_VALUE && parameters[2] <= Double.MAX_VALUE);
        
        // Test edge case
        // Test that the norm value is positive
        assertTrue(parameters[0] > 0.0);
    }
    
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Test typical use case
        // Test that the norm value is within a certain range
        assertTrue(parameters[0] >= 0.0 && parameters[0] <= Double.MAX_VALUE);
        // Test that the mean value is within a certain range
        assertTrue(parameters[1] >= Double.MIN_VALUE && parameters[1] <= Double.MAX_VALUE);
        // Test that the sigma value is within a certain range
        assertTrue(parameters[2] >= Double.MIN_VALUE && parameters[2] <= Double.MAX_VALUE);
        
        // Test edge case
        // Test that the norm value is positive
        assertTrue(parameters[0] > 0.0);
    }

    @Test
    public void testGuess() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0),
            new WeightedObservedPoint(4.03128248, 984167.0),
            new WeightedObservedPoint(4.03839603, 1887233.0),
            new WeightedObservedPoint(4.04421621, 2687152.0),
            new WeightedObservedPoint(4.05132976, 3461228.0),
            new WeightedObservedPoint(4.05326982, 3580526.0),
            new WeightedObservedPoint(4.05779662, 3439750.0),
            new WeightedObservedPoint(4.0636168,  2877648.0),
            new WeightedObservedPoint(4.06943698, 2175960.0),
            new WeightedObservedPoint(4.07525716, 1447024.0),
            new WeightedObservedPoint(4.08237071, 717104.0),
            new WeightedObservedPoint(4.08366408, 620014.0)
        };
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] guessedParams = guesser.guess();
        assertNotNull(guessedParams);
        assertEquals(3, guessedParams.length);
        // Test typical use case
        // Test that the norm value is within a certain range
        assertTrue(guessedParams[0] >= 0.0 && guessedParams[0] <= Double.MAX_VALUE);
        // Test that the mean value is within a certain range
        assertTrue(guessedParams[1] >= Double.MIN_VALUE && guessedParams[1] <= Double.MAX_VALUE);
        // Test that the sigma value is within a certain range
        assertTrue(guessedParams[2] >= Double.MIN_VALUE && guessedParams[2] <= Double.MAX_VALUE);
        
        // Test edge case
        // Test that the norm value is positive
        assertTrue(guessedParams[0] > 0.0);
    }
}