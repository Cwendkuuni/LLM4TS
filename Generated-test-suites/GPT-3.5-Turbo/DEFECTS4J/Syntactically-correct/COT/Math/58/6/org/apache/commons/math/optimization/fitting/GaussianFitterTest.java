import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        // Create instances of observed points
        WeightedObservedPoint p1 = new WeightedObservedPoint(1.0, 2.0, 3.0);
        WeightedObservedPoint p2 = new WeightedObservedPoint(2.0, 4.0, 6.0);
        WeightedObservedPoint p3 = new WeightedObservedPoint(3.0, 6.0, 9.0);

        // Add observed points to fitter
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(p1);
        fitter.addObservedPoint(p2);
        fitter.addObservedPoint(p3);

        // Test the fit method
        double[] parameters = fitter.fit();

        // Assert that the parameters are as expected
        assertEquals(1.0, parameters[0], 0.0001);
        assertEquals(2.0, parameters[1], 0.0001);
        assertEquals(3.0, parameters[2], 0.0001);
    }

    @Test
    public void testFitWithInitialGuess() {
        // Create instances of observed points
        WeightedObservedPoint p1 = new WeightedObservedPoint(1.0, 2.0, 3.0);
        WeightedObservedPoint p2 = new WeightedObservedPoint(2.0, 4.0, 6.0);
        WeightedObservedPoint p3 = new WeightedObservedPoint(3.0, 6.0, 9.0);

        // Add observed points to fitter
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(p1);
        fitter.addObservedPoint(p2);
        fitter.addObservedPoint(p3);

        // Define an initial guess for the parameters
        double[] initialGuess = {0.5, 1.0, 1.5};

        // Test the fit method with initial guess
        double[] parameters = fitter.fit(initialGuess);

        // Assert that the parameters are as expected
        assertEquals(1.0, parameters[0], 0.0001);
        assertEquals(2.0, parameters[1], 0.0001);
        assertEquals(3.0, parameters[2], 0.0001);
    }

    @Test
    public void testParameterGuesser() {
        // Create instances of observed points
        WeightedObservedPoint p1 = new WeightedObservedPoint(1.0, 2.0, 3.0);
        WeightedObservedPoint p2 = new WeightedObservedPoint(2.0, 4.0, 6.0);
        WeightedObservedPoint p3 = new WeightedObservedPoint(3.0, 6.0, 9.0);

        // Create an array of observed points
        WeightedObservedPoint[] observedPoints = {p1, p2, p3};

        // Create a parameter guesser with the observed points
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);

        // Test the guess method
        double[] parameters = guesser.guess();

        // Assert that the parameters are as expected
        assertEquals(6.0, parameters[0], 0.0001);
        assertEquals(2.0, parameters[1], 0.0001);
        assertEquals(0.5, parameters[2], 0.0001);
    }
}