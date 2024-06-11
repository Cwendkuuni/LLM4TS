import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    private GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    }

    @Test
    public void testFit_WithInitialGuess() {
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] parameters = fitter.fit(initialGuess);

        assertEquals(3, parameters.length);
        // TODO: Add more assertions
    }

    @Test
    public void testFit_WithoutInitialGuess() {
        double[] parameters = fitter.fit();

        assertEquals(3, parameters.length);
        // TODO: Add more assertions
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesser_Constructor_WithNullObservations() {
        WeightedObservedPoint[] observations = null;
        new GaussianFitter.ParameterGuesser(observations);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesser_Constructor_WithLessThan3Observations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        new GaussianFitter.ParameterGuesser(observations);
    }

    // TODO: Add more tests for GaussianFitter.ParameterGuesser

}