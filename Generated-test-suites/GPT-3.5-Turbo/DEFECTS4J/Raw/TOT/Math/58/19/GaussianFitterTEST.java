import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit_singleValueInitialGuess() {
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] expectedParameters = {1.0, 2.0, 3.0};
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        double[] parameters = fitter.fit(initialGuess);
        
        assertArrayEquals(expectedParameters, parameters);
    }

    @Test
    public void testFit_zeroValueInitialGuess() {
        double[] initialGuess = {0.0, 0.0, 0.0};
        double[] expectedParameters = {0.0, 0.0, 0.0};
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        double[] parameters = fitter.fit(initialGuess);
        
        assertArrayEquals(expectedParameters, parameters);
    }

    @Test
    public void testFit_noInitialGuess() {
        double[] initialGuess = null;
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        double[] parameters = fitter.fit(initialGuess);
        
        assertNotNull(parameters);
        assertNotEquals(0, parameters.length);
    }

    @Test
    public void testFit_emptyInitialGuess() {
        double[] initialGuess = {};
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        double[] parameters = fitter.fit(initialGuess);
        
        assertNotNull(parameters);
        assertNotEquals(0, parameters.length);
    }

    @Test
    public void testFit_validObservations() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0),
            new WeightedObservedPoint(5.0, 6.0)
        };
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        fitter.setObservations(observations);
        double[] parameters = fitter.fit();
        
        assertNotNull(parameters);
        assertNotEquals(0, parameters.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testFit_nullObservations() {
        WeightedObservedPoint[] observations = null;
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        fitter.setObservations(observations);
        
        fitter.fit();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFit_lessThanThreeObservations() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0)
        };
        
        GaussianFitter fitter = new GaussianFitter(new MockOptimizer());
        fitter.setObservations(observations);
        
        fitter.fit();
    }

    @Test
    public void testGuess_validObservations() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0),
            new WeightedObservedPoint(5.0, 6.0)
        };
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] parameters = guesser.guess();
        
        assertNotNull(parameters);
        assertNotEquals(0, parameters.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testGuess_nullObservations() {
        WeightedObservedPoint[] observations = null;
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        
        guesser.guess();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testGuess_lessThanThreeObservations() {
        WeightedObservedPoint[] observations = {
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0)
        };
        
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        
        guesser.guess();
    }

    private class MockOptimizer extends DifferentiableMultivariateVectorialOptimizer {
        // Mock implementation for testing purposes
    }

}