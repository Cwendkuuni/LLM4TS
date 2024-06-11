
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Create a GaussianFitter instance
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Add some observed points
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);

        // Set initial guess
        double[] initialGuess = {1000, 4.03, 0.2};

        // Fit the Gaussian function
        double[] parameters = fitter.fit(initialGuess);

        // Check the parameters
        assertEquals(3, parameters.length);
        assertEquals(531026.0, parameters[0], 0.0001);
        assertEquals(4.03, parameters[1], 0.0001);
        assertEquals(0.2, parameters[2], 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Create a GaussianFitter instance
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Add some observed points
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);

        // Fit the Gaussian function
        double[] parameters = fitter.fit();

        // Check the parameters
        assertEquals(3, parameters.length);
        assertTrue(parameters[0] > 0);
        assertTrue(parameters[1] > 0);
        assertTrue(parameters[2] > 0);
    }

    @Test
    public void testParameterGuesser() {
        // Create an array of observed points
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(4.0254623, 531026.0),
            new WeightedObservedPoint(4.03128248, 984167.0),
            new WeightedObservedPoint(4.03839603, 1887233.0)
        };

        // Create a ParameterGuesser instance
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);

        // Guess the parameters
        double[] parameters = guesser.guess();

        // Check the parameters
        assertEquals(3, parameters.length);
        assertTrue(parameters[0] > 0);
        assertTrue(parameters[1] > 0);
        assertTrue(parameters[2] > 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserNullObservations() {
        // Create a ParameterGuesser instance with null observations
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserTooFewObservations() {
        // Create an empty array of observations
        WeightedObservedPoint[] observations = {};

        // Create a ParameterGuesser instance with too few observations
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    }
}

