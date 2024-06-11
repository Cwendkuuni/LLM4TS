import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more assertions based on expected results
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more assertions based on expected results
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 2.0, 3.0),
            new WeightedObservedPoint(4.0, 5.0, 6.0),
            new WeightedObservedPoint(7.0, 8.0, 9.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParams = guesser.guess();
        assertNotNull(guessedParams);
        assertEquals(3, guessedParams.length);
        // Add more assertions based on expected results
    }

}