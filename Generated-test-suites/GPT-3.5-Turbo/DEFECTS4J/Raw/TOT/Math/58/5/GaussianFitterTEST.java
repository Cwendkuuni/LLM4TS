import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {
    
    // Test addObservedPoint method
    @Test
    public void testAddObservedPoint() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        
        assertEquals(6, fitter.getObservations().length);
    }
    
    // Test fit(double[] initialGuess) method
    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        
        double[] initialGuess = {1500000.0, 4.05, 0.1};
        double[] parameters = fitter.fit(initialGuess);
        
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }
    
    // Test fit() method
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623,  531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        fitter.addObservedPoint(4.03839603, 1887233.0);
        fitter.addObservedPoint(4.04421621, 2687152.0);
        fitter.addObservedPoint(4.05132976, 3461228.0);
        fitter.addObservedPoint(4.05326982, 3580526.0);
        
        double[] parameters = fitter.fit();
        
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }
    
    // Test ParameterGuesser class
    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[6];
        observations[0] = new WeightedObservedPoint(1.0, 1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 2.0, 2.0);
        observations[2] = new WeightedObservedPoint(3.0, 3.0, 3.0);
        observations[3] = new WeightedObservedPoint(4.0, 4.0, 4.0);
        observations[4] = new WeightedObservedPoint(5.0, 5.0, 5.0);
        observations[5] = new WeightedObservedPoint(6.0, 6.0, 6.0);
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] parameters = guesser.guess();
        
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }
}