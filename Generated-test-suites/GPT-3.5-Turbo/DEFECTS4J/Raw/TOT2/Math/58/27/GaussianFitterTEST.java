import static org.junit.Assert.*;
import org.junit.Test;

public class GaussianFitterTest {
    
    // Test fitting with initial guess
    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1, 0, 1}; // norm, mean, sigma
        double[] parameters = fitter.fit(initialGuess);
        
        assertEquals(1.0, parameters[0], 0.001);
        assertEquals(0.0, parameters[1], 0.001);
        assertEquals(1.0, parameters[2], 0.001);
    }
    
    // Test fitting without initial guess
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(1.0, 1.0);
        fitter.addObservedPoint(2.0, 2.0);
        fitter.addObservedPoint(3.0, 3.0);
        
        double[] parameters = fitter.fit();
        
        assertEquals(3.0, parameters[0], 0.001);
        assertEquals(2.0, parameters[1], 0.001);
        assertEquals(1.0, parameters[2], 0.001);
    }
    
    // Test ParameterGuesser class
    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observedPoints = {new WeightedObservedPoint(1.0, 1.0, 1.0),
                                                  new WeightedObservedPoint(2.0, 2.0, 1.0),
                                                  new WeightedObservedPoint(3.0, 3.0, 1.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        
        double[] parameters = guesser.guess();
        
        assertEquals(3.0, parameters[0], 0.001);
        assertEquals(2.0, parameters[1], 0.001);
        assertEquals(1.0, parameters[2], 0.001);
    }
    
    // Test edge case for finding max Y
    @Test
    public void testFindMaxYEdgeCase() {
        WeightedObservedPoint[] observedPoints = {new WeightedObservedPoint(1.0, 2.0, 1.0),
                                                  new WeightedObservedPoint(2.0, 1.0, 1.0),
                                                  new WeightedObservedPoint(3.0, 3.0, 1.0)};
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        
        int maxYIdx = guesser.findMaxY(observedPoints);
        
        assertEquals(2, maxYIdx);
    }
}