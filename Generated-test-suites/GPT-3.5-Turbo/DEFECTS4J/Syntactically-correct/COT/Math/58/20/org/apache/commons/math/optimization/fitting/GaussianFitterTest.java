import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 0.0, 1.0};
        GaussianFitter fitter = new GaussianFitter(null);
        double[] result = fitter.fit(initialGuess);

        // Compare result with expected values
        // ...
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] result = fitter.fit();

        // Compare result with expected values
        // ...
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        double[] result = guesser.guess();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithInsufficientObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[2]);
        double[] result = guesser.guess();
    }

    // Add more edge case and exception handling test cases...


}