import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623, 531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        fitter.addObservedPoint(4.05779662, 3439750.0);
        fitter.addObservedPoint(4.0636168, 2877648.0);
        fitter.addObservedPoint(4.06943698, 2175960.0);
        fitter.addObservedPoint(4.07525716, 1447024.0);
        fitter.addObservedPoint(4.08237071, 717104.0);
        fitter.addObservedPoint(4.08366408, 620014.0);
        double[] parameters = fitter.fit();
        
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 3.0, 0.5};
        double[] parameters = fitter.fit(initialGuess);
        
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[3];
        observations[0] = new WeightedObservedPoint(1.0, 2.0, 0.1);
        observations[1] = new WeightedObservedPoint(1.2, 2.5, 0.2);
        observations[2] = new WeightedObservedPoint(1.4, 3.0, 0.3);
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] guessedParams = guesser.guess();

        assertNotNull(guessedParams);
        assertEquals(3, guessedParams.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullObservations() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserWithLessThanThreeObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        observations[0] = new WeightedObservedPoint(1.0, 2.0, 0.1);
        observations[1] = new WeightedObservedPoint(1.2, 2.5, 0.2);

        new GaussianFitter.ParameterGuesser(observations);
    }

    @Test
    public void testParameterGuesserInterpolateXAtY() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 2.0, 0.1);
        points[1] = new WeightedObservedPoint(1.2, 2.5, 0.2);
        points[2] = new WeightedObservedPoint(1.4, 3.0, 0.3);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double interpolatedX = guesser.interpolateXAtY(points, 0, 1, 2.2);

        assertEquals(1.16, interpolatedX, 0.01);
    }

    @Test
    public void testParameterGuesserIsBetween() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        boolean result = guesser.isBetween(1.5, 1.0, 2.0);

        assertTrue(result);
    }
}