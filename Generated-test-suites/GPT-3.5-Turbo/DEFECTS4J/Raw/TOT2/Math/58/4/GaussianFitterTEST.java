import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 0.0, 1.0};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] result = fitter.fit(initialGuess);
        assertNotNull(result);
        assertEquals(3, result.length);
        // Add more specific assertions based on expected values
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] result = fitter.fit();
        assertNotNull(result);
        assertEquals(3, result.length);
        // Add more specific assertions based on expected values
    }

    @Test
    public void testParameterGuesserConstruction() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 1.0, 0.0),
            new WeightedObservedPoint(2.0, 2.0, 1.0),
            new WeightedObservedPoint(3.0, 3.0, 2.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        assertNotNull(guesser);
        // Add more specific assertions based on expected values
    }
    
    // Add more test cases for other methods in GaussianFitter class

}