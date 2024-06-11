import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1, 0.0, 0.5),
            new WeightedObservedPoint(1, 1.0, 1.0),
            new WeightedObservedPoint(1, 2.0, 0.5)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParams = guesser.guess();
        assertNotNull(guessedParams);
        assertEquals(3, guessedParams.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullInput() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserWithInsufficientPoints() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1, 0.0, 0.5),
            new WeightedObservedPoint(1, 1.0, 1.0)
        };
        new GaussianFitter.ParameterGuesser(points);
    }

}