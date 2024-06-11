import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Test fit method with initial guess
        double[] initialGuess = {1.0, 0.0, 1.0};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 1.0);
        fitter.addObservedPoint(2.0, 2.0);
        double[] result = fitter.fit(initialGuess);

        double[] expected = {2.0, 1.5, 0.5};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Test fit method without initial guess
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 1.0);
        fitter.addObservedPoint(2.0, 2.0);
        double[] result = fitter.fit();

        double[] expected = {2.0, 1.5, 0.5};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserNullObservations() {
        // Test ParameterGuesser constructor with null observations
        WeightedObservedPoint[] observations = null;
        new GaussianFitter.ParameterGuesser(observations);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserNotEnoughObservations() {
        // Test ParameterGuesser constructor with less than 3 observations
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 1.0),
            new WeightedObservedPoint(2.0, 2.0)
        };
        new GaussianFitter.ParameterGuesser(observations);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}