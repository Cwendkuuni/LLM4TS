import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        double[] initialGuess = {100.0, 5.0, 1.0};
        double[] expected = {100.0, 5.0, 1.0};

        assertArrayEquals(expected, fitter.fit(initialGuess), 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        double[] expected = {100.0, 5.0, 1.0};

        assertArrayEquals(expected, fitter.fit(), 0.0001);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = {
                new WeightedObservedPoint(4.0, 0.0),
                new WeightedObservedPoint(4.1, 1.0),
                new WeightedObservedPoint(4.2, 2.0),
                new WeightedObservedPoint(4.3, 3.0),
                new WeightedObservedPoint(4.4, 4.0)
        };

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);

        double[] expected = {4.0, 4.2, 0.2236068};

        assertArrayEquals(expected, guesser.guess(), 0.0001);
    }
}