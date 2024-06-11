import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.fitting.GaussianFitter;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] result = fitter.fit(initialGuess);
        double[] expected = {1.0, 2.0, 3.0};

        assertArrayEquals(expected, result, 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        double[] result = fitter.fit();
        assertNotNull(result);
    }

    @Test
    public void testParameterGuesser() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser();
        double[] result = guesser.guess();
        assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserWithSmallObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[] {});
    }

    @Test
    public void testParameterGuesserWithObservations() {
        WeightedObservedPoint[] observations = { new WeightedObservedPoint(1.0, 2.0), new WeightedObservedPoint(3.0, 4.0) };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] result = guesser.guess();
        assertNotNull(result);
    }

    @Test
    public void testParameterGuesserWithObservationsOutOfRange() {
        WeightedObservedPoint[] observations = { new WeightedObservedPoint(1.0, 2.0), new WeightedObservedPoint(3.0, 4.0) };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);

        try {
            guesser.interpolateXAtY(observations, 0, 1, 5.0);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }
}