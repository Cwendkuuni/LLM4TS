import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.NumberIsTooSmallException;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] result = fitter.fit(initialGuess);

        assertNotNull(result);
        assertEquals(3, result.length);
        // Add more assertion logic here based on the expected output for this method
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] result = fitter.fit();

        assertNotNull(result);
        assertEquals(3, result.length);
        // Add more assertion logic here based on the expected output for this method
    }

    @Test
    public void testParameterGuesserBasicGuess() {
        WeightedObservedPoint[] points = {new WeightedObservedPoint(1.0, 2.0, 3.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] result = guesser.guess();

        assertNotNull(result);
        assertEquals(3, result.length);
        // Add more assertion logic here based on the expected output for this method
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserNullInput() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
    }
    
    // Add more test cases to cover edge cases and exception scenarios

}