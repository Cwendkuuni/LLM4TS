import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;

public class GaussianFitterTest {
    
    private GaussianFitter fitter;
    
    @Before
    public void setUp() {
        fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
    }
    
    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] expected = {1.0, 2.0, 3.0};
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(expected, result, 0.001);
    }
    
    @Test
    public void testFitWithoutInitialGuess() {
        double[] expected = {1.0, 2.0, 3.0};
        double[] result = fitter.fit();
        assertArrayEquals(expected, result, 0.001);
    }
    
    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = {new WeightedObservedPoint(1.0, 2.0), new WeightedObservedPoint(3.0, 4.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] expected = {1.0, 2.0, 3.0};
        double[] result = guesser.guess();
        assertArrayEquals(expected, result, 0.001);
    }
}