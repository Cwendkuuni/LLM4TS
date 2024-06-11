import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 2.0, 3.0};
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
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0, 3.0),
            new WeightedObservedPoint(4.0, 5.0, 6.0),
            new WeightedObservedPoint(7.0, 8.0, 9.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] params = guesser.guess();
        assertNotNull(params);
        assertEquals(3, params.length);
    }
}