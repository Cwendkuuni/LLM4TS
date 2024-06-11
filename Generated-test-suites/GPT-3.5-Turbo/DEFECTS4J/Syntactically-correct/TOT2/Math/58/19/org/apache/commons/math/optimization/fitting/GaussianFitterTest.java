import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertEquals(1.0, parameters[0], 0.0001);
        assertEquals(0.0, parameters[1], 0.0001);
        assertEquals(1.0, parameters[2], 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        assertEquals(1.0, parameters[0], 0.0001);
        assertEquals(0.0, parameters[1], 0.0001);
        assertEquals(1.0, parameters[2], 0.0001);
    }

    @Test
    public void testParameterGuesserGuess() {
        WeightedObservedPoint[] observedPoints = new WeightedObservedPoint[3];
        observedPoints[0] = new WeightedObservedPoint(0.0, 1.0, 1.0);
        observedPoints[1] = new WeightedObservedPoint(1.0, 2.0, 1.0);
        observedPoints[2] = new WeightedObservedPoint(2.0, 1.0, 1.0);

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] parameters = guesser.guess();
        assertEquals(2.0, parameters[0], 0.0001);
        assertEquals(1.0, parameters[1], 0.0001);
        assertEquals(1.0, parameters[2], 0.0001);
    }

    // Additional test cases for edge cases and exception handling should be added here

}