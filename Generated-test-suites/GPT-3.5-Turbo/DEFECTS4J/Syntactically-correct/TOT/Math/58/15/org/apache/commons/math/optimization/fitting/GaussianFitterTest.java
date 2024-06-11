import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        // Create an instance of GaussianFitter with an initial guess
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        
        // Add observed points to the fitter
        fitter.addObservedPoint(1.0, 0.5);
        fitter.addObservedPoint(2.0, 0.7);
        fitter.addObservedPoint(3.0, 0.9);
        
        // Call fit method with initial guess and get the parameters
        double[] parameters = fitter.fit(initialGuess);
        
        // Assert that the parameters are correct
        assertEquals(0.849486, parameters[0], 1e-6);
        assertEquals(2.051223, parameters[1], 1e-6);
        assertEquals(0.325702, parameters[2], 1e-6);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        // Create an instance of GaussianFitter without an initial guess
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        
        // Add observed points to the fitter
        fitter.addObservedPoint(1.0, 0.5);
        fitter.addObservedPoint(2.0, 0.7);
        fitter.addObservedPoint(3.0, 0.9);
        
        // Call fit method without initial guess and get the parameters
        double[] parameters = fitter.fit();
        
        // Assert that the parameters are correct
        assertEquals(0.849486, parameters[0], 1e-6);
        assertEquals(2.051223, parameters[1], 1e-6);
        assertEquals(0.325702, parameters[2], 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservations() {
        // Create an instance of GaussianFitter
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        
        // Call fit method with null observations
        fitter.fit(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithInsufficientObservations() {
        // Create an instance of GaussianFitter
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        
        // Add only two observed points to the fitter
        fitter.addObservedPoint(1.0, 0.5);
        fitter.addObservedPoint(2.0, 0.7);
        
        // Call fit method with insufficient observations
        fitter.fit();
    }
    
    @Test
    public void testParameterGuesserGuess() {
        // Create an instance of ParameterGuesser
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser();
        
        // Create observed points
        WeightedObservedPoint[] observations = new WeightedObservedPoint[5];
        observations[0] = new WeightedObservedPoint(1.0, 0.5, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 0.7, 1.0);
        observations[2] = new WeightedObservedPoint(3.0, 0.9, 1.0);
        observations[3] = new WeightedObservedPoint(4.0, 0.6, 1.0);
        observations[4] = new WeightedObservedPoint(5.0, 0.4, 1.0);
        
        // Set the observations in the guesser
        guesser.setObservations(observations);
        
        // Call guess method and get the guessed parameters
        double[] parameters = guesser.guess();
        
        // Assert that the parameters are correct
        assertEquals(0.849486, parameters[0], 1e-6);
        assertEquals(2.051223, parameters[1], 1e-6);
        assertEquals(0.325702, parameters[2], 1e-6);
    }

}