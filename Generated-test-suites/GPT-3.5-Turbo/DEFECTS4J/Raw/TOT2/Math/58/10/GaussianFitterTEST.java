import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] initialGuess = {1.0, 0.0, 0.5};
        double[] result = fitter.fit(initialGuess);
        assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] result = fitter.fit();
        assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 2.0, 0.1);
        points[1] = new WeightedObservedPoint(2.0, 3.0, 0.2);
        points[2] = new WeightedObservedPoint(3.0, 4.0, 0.3);
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParams = guesser.guess();
        assertNotNull(guessedParams);
        // Add more assertions as needed
    }

    // Add more test cases for other methods and edge cases

}