import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        
        // Add assertions here to test the functionality of fit method with initial guess
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        
        // Add assertions here to test the functionality of fit method without initial guess
    }

    @Test
    public void testParameterGuesserWithValidObservations() {
        WeightedObservedPoint[] observations = {new WeightedObservedPoint(1.0, 1.0, 1.0),
                                                new WeightedObservedPoint(2.0, 2.0, 2.0),
                                                new WeightedObservedPoint(3.0, 3.0, 3.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] params = guesser.guess();
        
        // Add assertions here to test the functionality of ParameterGuesser with valid observations
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullObservations() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
    }

    // Add more test cases for edge cases and exception handling scenarios as needed

}