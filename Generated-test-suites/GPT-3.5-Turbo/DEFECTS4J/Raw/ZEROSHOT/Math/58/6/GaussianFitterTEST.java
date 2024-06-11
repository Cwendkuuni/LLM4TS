import org.junit.Test;

public class GaussianFitterTest {
    
    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] result = fitter.fit(initialGuess);
        // assert statements
        assertEquals(3, result.length);
        // More assertions go here
    }
    
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        double[] result = fitter.fit();
        // assert statements
        assertEquals(3, result.length);
        // More assertions go here
    }
    
    @Test
    public void testParameterGuess() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 0.0, 0.0);
        points[1] = new WeightedObservedPoint(2.0, 1.0, 0.0);
        points[2] = new WeightedObservedPoint(3.0, 0.0, 0.0);
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] result = guesser.guess();
        // assert statements
        assertEquals(3, result.length);
        // More assertions go here
    }
    
    @Test(expected = NullArgumentException.class)
    public void testParameterGuessNullArgument() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        double[] result = guesser.guess();
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuessTooFewPoints() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[2];
        points[0] = new WeightedObservedPoint(1.0, 0.0, 0.0);
        points[1] = new WeightedObservedPoint(2.0, 1.0, 0.0);
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] result = guesser.guess();
    }
    
    @Test
    public void testInterpolateXAtY() throws OutOfRangeException {
        WeightedObservedPoint[] points = new WeightedObservedPoint[3];
        points[0] = new WeightedObservedPoint(1.0, 0.0, 0.0);
        points[1] = new WeightedObservedPoint(2.0, 1.0, 0.0);
        points[2] = new WeightedObservedPoint(3.0, 0.0, 0.0);
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double result = guesser.interpolateXAtY(points, 0, 1, 0.5);
        // assert statements
        assertEquals(1.5, result, 0.0001);
    }
    
    // More test methods go here
    
}