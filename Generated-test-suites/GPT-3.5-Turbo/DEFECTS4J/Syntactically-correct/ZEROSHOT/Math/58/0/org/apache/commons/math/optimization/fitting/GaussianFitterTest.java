import org.junit.Test;

import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFit_withInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] result = fitter.fit(initialGuess);
        double[] expected = {1.0, 0.0, 1.0};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testFit_withoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] result = fitter.fit();
        double[] expected = {1.0, 0.0, 1.0};
        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testParameterGuesser() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        double[] result = guesser.guess();
        assertNull(result);
    }
}