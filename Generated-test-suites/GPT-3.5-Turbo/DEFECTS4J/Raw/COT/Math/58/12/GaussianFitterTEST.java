
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.exception.OutOfRangeException;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] result = fitter.fit(initialGuess);

        assertNotNull(result);
        assertEquals(initialGuess.length, result.length);
        // Additional assertions can be added to check the accuracy of the fit
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] result = fitter.fit();

        assertNotNull(result);
        assertEquals(3, result.length);
        // Additional assertions can be added to check the accuracy of the fit
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0, 3.0),
            new WeightedObservedPoint(4.0, 5.0, 6.0),
            new WeightedObservedPoint(7.0, 8.0, 9.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] result = guesser.guess();

        assertNotNull(result);
        assertEquals(3, result.length);
        // Additional assertions can be added to check the accuracy of the guessed parameters
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserWithInsufficientObservations() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0, 3.0),
            new WeightedObservedPoint(4.0, 5.0, 6.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInterpolateXAtYWithOutOfRangeY() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0, 3.0),
            new WeightedObservedPoint(4.0, 5.0, 6.0),
            new WeightedObservedPoint(7.0, 8.0, 9.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        guesser.interpolateXAtY(observations, 0, 1, 10.0);
    }
}
