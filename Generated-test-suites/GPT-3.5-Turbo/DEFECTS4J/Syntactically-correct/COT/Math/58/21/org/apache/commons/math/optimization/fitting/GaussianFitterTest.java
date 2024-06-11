import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {
    
    @Test
    public void testConstructor() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        assertNotNull(fitter);
    }
    
    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = { 1.0, 0.0, 1.0 };
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }
    
    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }
    
    @Test
    public void testParameterGuesserConstructor() {
        WeightedObservedPoint[] observations = { 
            new WeightedObservedPoint(1.0, 0.0, 1.0),
            new WeightedObservedPoint(2.0, 2.0, 1.0),
            new WeightedObservedPoint(3.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        assertNotNull(guesser);
    }
    
    @Test
    public void testParameterGuesserGuess() {
        WeightedObservedPoint[] observations = { 
            new WeightedObservedPoint(1.0, 0.0, 1.0),
            new WeightedObservedPoint(2.0, 2.0, 1.0),
            new WeightedObservedPoint(3.0, 3.0, 1.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] parameters = guesser.guess();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }
}