import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        fitter.addObservedPoint(4.0, 10.0);
        fitter.addObservedPoint(5.0, 20.0);

        double[] params = fitter.fit();
        assertEquals(2, params.length);
        assertEquals(20.0, params[0], 0.0001);
        assertEquals(4.5, params[1], 0.0001);
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        double[] initialGuess = {15.0, 4.0};
        fitter.addObservedPoint(3.0, 5.0);
        fitter.addObservedPoint(4.0, 10.0);

        double[] params = fitter.fit(initialGuess);
        assertEquals(2, params.length);
        assertEquals(10.0, params[0], 0.0001);
        assertEquals(3.5, params[1], 0.0001);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
                new WeightedObservedPoint(3.0, 5.0),
                new WeightedObservedPoint(4.0, 10.0),
                new WeightedObservedPoint(5.0, 15.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParams = guesser.guess();

        assertEquals(3, guessedParams.length);
        assertEquals(15.0, guessedParams[0], 0.0001);
        assertEquals(4.0, guessedParams[1], 0.0001);
        assertEquals(0.5, guessedParams[2], 0.0001);
    }

    // Mock optimizer for testing purposes
    private class MockOptimizer extends DifferentiableMultivariateVectorialOptimizer {
        // Mock implementation
    }

}