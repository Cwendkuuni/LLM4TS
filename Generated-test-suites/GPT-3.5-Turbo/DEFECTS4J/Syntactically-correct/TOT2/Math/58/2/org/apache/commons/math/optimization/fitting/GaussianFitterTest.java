import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Test case for fit method with initial guess
        // Test scenario with typical use case
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] result = fitter.fit(initialGuess);
        // Add assertions here to validate the result
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Test case for fit method without initial guess
        // Test scenario with typical use case
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] result = fitter.fit();
        // Add assertions here to validate the result
    }

    @Test
    public void testParameterGuesser() {
        // Test case for ParameterGuesser class
        // Test scenario with edge cases
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(0.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(2.0, 3.0)
        };
        GaussianFitter.ParameterGuesser guesser = new ParameterGuesser(observations);
        double[] result = guesser.guess();
        // Add assertions here to validate the result
    }

}