import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {new WeightedObservedPoint(1.0, 2.0, 1.0),
                                          new WeightedObservedPoint(2.0, 3.0, 1.0),
                                          new WeightedObservedPoint(3.0, 4.0, 1.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParams = guesser.guess();
        assertNotNull(guessedParams);
        assertEquals(3, guessedParams.length);
    }

}