import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GaussianFitterTest {

    private GaussianFitter fitter;
    private double[] initialGuess;
    private WeightedObservedPoint[] observedPoints;

    @Before
    public void setUp() {
        fitter = new GaussianFitter();
        initialGuess = new double[]{1.0, 0.0, 1.0};
        observedPoints = new WeightedObservedPoint[]{
                new WeightedObservedPoint(1.0, 2.0, 1.0),
                new WeightedObservedPoint(3.0, 4.0, 1.0),
                new WeightedObservedPoint(5.0, 6.0, 1.0)
        };
    }

    @Test
    public void testFitWithInitialGuess() {
        double[] result = fitter.fit(initialGuess);
        assertEquals(3, result.length);
        // additional assertions on the result

    }

    @Test
    public void testFitWithoutInitialGuess() {
        double[] result = fitter.fit();
        assertEquals(3, result.length);
        // additional assertions on the result

    }

    @Test
    public void testParameterGuesser() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] result = guesser.guess();
        assertEquals(3, result.length);
        // additional assertions on the result

    }

}