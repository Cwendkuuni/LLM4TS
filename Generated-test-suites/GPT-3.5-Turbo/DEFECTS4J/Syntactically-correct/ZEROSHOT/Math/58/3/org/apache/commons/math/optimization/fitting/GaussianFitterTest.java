import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesserGuess() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(1.0, 1.0, 1.0),
            new WeightedObservedPoint(1.0, 2.0, 0.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserNullArguments() {
        WeightedObservedPoint[] points = null;
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserInsufficientPoints() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(1.0, 1.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = guesser.guess();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesserFindMaxY() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(3.0, 1.0, 1.0),
            new WeightedObservedPoint(2.0, 2.0, 0.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        int maxYIdx = guesser.findMaxY(points);
        assertEquals(1, maxYIdx);
    }

    @Test
    public void testParameterGuesserInterpolateXAtY() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(2.0, 1.0, 1.0),
            new WeightedObservedPoint(3.0, 2.0, 0.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        try {
            double x = guesser.interpolateXAtY(points, 0, 1, 0.5);
            assertEquals(0.5, x, 0.001);
        } catch (Exception e) {
            fail("An exception occurred.");
        }
    }
}