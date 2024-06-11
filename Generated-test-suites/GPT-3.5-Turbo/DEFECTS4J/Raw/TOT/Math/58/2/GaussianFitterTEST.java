import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFit_InitialGuess() {
        double[] initialGuess = {1, 0, 1};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0, 0.0);
        double[] parameters = fitter.fit(initialGuess);
        assertArrayEquals(parameters, new double[] {1.0, 0.0, 1.0}, 0.0001);
    }
    
    @Test
    public void testFit_WithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0, 1.0);
        double[] parameters = fitter.fit();
        assertArrayEquals(parameters, new double[] {1.0, 4.0, 0.6931}, 0.0001);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testFit_NullArgument() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0, 1.0);
        double[] parameters = fitter.fit(null);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testFit_TooFewObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0, 1.0);
        fitter.addObservedPoint(5.0, 2.0);
        double[] parameters = fitter.fit();
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testFit_OutOfRange() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0, 1.0);
        double[] parameters = fitter.fit();
    }
    
    @Test
    public void testParameterGuesser_BasicGuess() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[4];
        points[0] = new WeightedObservedPoint(0.5, 1, 4);
        points[1] = new WeightedObservedPoint(0.5, 2, 6);
        points[2] = new WeightedObservedPoint(0.5, 3, 8);
        points[3] = new WeightedObservedPoint(0.5, 4, 10);
        
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(points);
        double[] parameters = parameterGuesser.guess();
        assertArrayEquals(parameters, new double[] {2.5, 3.0, 1.4142}, 0.0001);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testParameterGuesser_NullArgument() {
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(null);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesser_TooFewObservations() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[2];
        points[0] = new WeightedObservedPoint(0.5, 1, 4);
        points[1] = new WeightedObservedPoint(0.5, 2, 6);
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(points);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testParameterGuesser_OutOfRange() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[4];
        points[0] = new WeightedObservedPoint(0.5, 1, 4);
        points[1] = new WeightedObservedPoint(0.5, 2, 6);
        points[2] = new WeightedObservedPoint(0.5, 3, 8);
        points[3] = new WeightedObservedPoint(0.5, 4, 2);
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(points);
    }
}