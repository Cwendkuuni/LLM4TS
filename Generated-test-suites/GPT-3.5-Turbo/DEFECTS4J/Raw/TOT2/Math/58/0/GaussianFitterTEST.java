import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 10.0);
        fitter.addObservedPoint(2.0, 20.0);
        fitter.addObservedPoint(3.0, 30.0);
        double[] initialGuess = { 15.0, 2.0, 1.0 };
        double[] parameters = fitter.fit(initialGuess);
        assertEquals(15.0, parameters[0], 0.001);
        assertEquals(2.0, parameters[1], 0.001);
        assertEquals(1.0, parameters[2], 0.001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 10.0);
        fitter.addObservedPoint(2.0, 20.0);
        fitter.addObservedPoint(3.0, 30.0);
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
    }

    @Test
    public void testGuessWithValidObservations() {
        WeightedObservedPoint point1 = new WeightedObservedPoint(1.0, 10.0, 1.0);
        WeightedObservedPoint point2 = new WeightedObservedPoint(2.0, 20.0, 1.0);
        WeightedObservedPoint point3 = new WeightedObservedPoint(3.0, 30.0, 1.0);
        WeightedObservedPoint[] observations = { point1, point2, point3 };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] guessedParams = guesser.guess();
        assertNotNull(guessedParams);
    }

    @Test(expected = NullArgumentException.class)
    public void testGuessWithNullObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        guesser.guess();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testGuessWithLessThanThreeObservations() {
        WeightedObservedPoint point1 = new WeightedObservedPoint(1.0, 10.0, 1.0);
        WeightedObservedPoint point2 = new WeightedObservedPoint(2.0, 20.0, 1.0);
        WeightedObservedPoint[] observations = { point1, point2 };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        guesser.guess();
    }

}