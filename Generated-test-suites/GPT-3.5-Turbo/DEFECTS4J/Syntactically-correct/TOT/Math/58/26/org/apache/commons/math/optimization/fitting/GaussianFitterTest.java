import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {
    
    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        
        WeightedObservedPoint[] observedPoints = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(2.0, 1.0, 0.0),
            new WeightedObservedPoint(3.0, 0.0, 0.0),
            new WeightedObservedPoint(4.0, 1.0, 0.0),
            new WeightedObservedPoint(5.0, 0.0, 0.0)
        };
        
        fitter.addObservedPoints(observedPoints);
        
        double[] initialGuess = {1.0, 3.0, 0.5};
        double[] parameters = fitter.fit(initialGuess);
        
        double[] expectedParameters = {0.99999999, 2.99999998, 0.50000008};
        
        assertArrayEquals(expectedParameters, parameters, 0.00000001);
    }
    
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        
        WeightedObservedPoint[] observedPoints = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(2.0, 1.0, 0.0),
            new WeightedObservedPoint(3.0, 0.0, 0.0),
            new WeightedObservedPoint(4.0, 1.0, 0.0),
            new WeightedObservedPoint(5.0, 0.0, 0.0)
        };
        
        fitter.addObservedPoints(observedPoints);
        
        double[] parameters = fitter.fit();
        
        double[] expectedParameters = {1.0, 2.0, 0.5};
        
        assertArrayEquals(expectedParameters, parameters, 0.00000001);
    }
    
    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observedPoints = {
            new WeightedObservedPoint(1.0, 0.0, 0.0),
            new WeightedObservedPoint(2.0, 1.0, 0.0),
            new WeightedObservedPoint(3.0, 0.0, 0.0),
            new WeightedObservedPoint(4.0, 1.0, 0.0),
            new WeightedObservedPoint(5.0, 0.0, 0.0)
        };
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] parameters = guesser.guess();
        
        double[] expectedParameters = {1.0, 2.0, 0.5};
        
        assertArrayEquals(expectedParameters, parameters, 0.00000001);
    }
}