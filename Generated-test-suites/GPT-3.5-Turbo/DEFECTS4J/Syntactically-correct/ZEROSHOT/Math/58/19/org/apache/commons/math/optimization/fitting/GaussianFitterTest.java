import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] result = fitter.fit(initialGuess);
        double[] expected = {1.0, 2.0, 3.0};
        assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] result = fitter.fit();
        double[] expected = {1.0, 2.0, 3.0};
        assertArrayEquals(expected, result, 0.001);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[3];
        observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 2.0, 2.0);
        observations[2] = new WeightedObservedPoint(3.0, 3.0, 3.0);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] result = guesser.guess();
        double[] expected = {3.0, 2.0, 1.0};
        assertArrayEquals(expected, result, 0.001);
    }
}