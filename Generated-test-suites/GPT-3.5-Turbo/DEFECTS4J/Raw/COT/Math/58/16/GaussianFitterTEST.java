
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussianFitterTest {
    private GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    }

    @Test
    public void testFit_WithInitialGuess_ReturnsParameters() {
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] expected = {1.0, 2.0, 3.0};
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(expected, result, 0.00001);
    }

    @Test
    public void testFit_WithoutInitialGuess_ReturnsParameters() {
        double[] expected = {1.0, 2.0, 3.0};
        double[] result = fitter.fit();
        assertArrayEquals(expected, result, 0.00001);
    }

    @Test
    public void testParameterGuesser_Guess_ReturnsParameters() {
        WeightedObservedPoint[] observedPoints = {
                new WeightedObservedPoint(1, 4, 2),
                new WeightedObservedPoint(2, 8, 4),
                new WeightedObservedPoint(3, 12, 6)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] expected = {12, 3, 2};
        double[] result = guesser.guess();
        assertArrayEquals(expected, result, 0.00001);
    }
}
