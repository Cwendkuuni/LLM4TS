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
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] result = fitter.fit(initialGuess);

        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 0.0001);
        assertEquals(0.0, result[1], 0.0001);
        assertEquals(1.0, result[2], 0.0001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        double[] result = fitter.fit();

        assertEquals(3, result.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observedPoints = {
            new WeightedObservedPoint(4.0254623, 531026.0),
            new WeightedObservedPoint(4.03128248, 984167.0),
            new WeightedObservedPoint(4.03839603, 1887233.0),
            new WeightedObservedPoint(4.04421621, 2687152.0),
            new WeightedObservedPoint(4.05132976, 3461228.0),
            new WeightedObservedPoint(4.05326982, 3580526.0),
            new WeightedObservedPoint(4.05779662, 3439750.0),
            new WeightedObservedPoint(4.0636168,  2877648.0),
            new WeightedObservedPoint(4.06943698, 2175960.0),
            new WeightedObservedPoint(4.07525716, 1447024.0),
            new WeightedObservedPoint(4.08237071, 717104.0),
            new WeightedObservedPoint(4.08366408, 620014.0)
        };

        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] result = guesser.guess();

        assertEquals(3, result.length);
        assertEquals(3580526.0, result[0], 0.0001);
        assertEquals(4.05326982, result[1], 0.0001);
        assertEquals(0.03587403, result[2], 0.0001);
    }

}