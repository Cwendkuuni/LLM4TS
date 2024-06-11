import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 2.0, 3.0};
        GaussianFitter fitter = new GaussianFitter(null);
        double[] result = fitter.fit(initialGuess);
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] result = fitter.fit();
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 2.0, 0.5);
        points[1] = new WeightedObservedPoint(2.0, 3.0, 0.6);
        points[2] = new WeightedObservedPoint(3.0, 4.0, 0.7);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] result = guesser.guess();
        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test
    public void testInterpolateXAtY() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 2.0, 0.5);
        points[1] = new WeightedObservedPoint(2.0, 3.0, 0.6);
        points[2] = new WeightedObservedPoint(3.0, 4.0, 0.7);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double result = guesser.interpolateXAtY(points, 0, 1, 2.5);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testIsBetween() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        boolean result = guesser.isBetween(2.5, 2.0, 3.0);
        assertTrue(result);

        result = guesser.isBetween(3.5, 2.0, 3.0);
        assertFalse(result);
    }

}