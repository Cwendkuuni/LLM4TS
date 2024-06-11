import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.analysis.function.Gaussian;

public class GaussianFitterTest {

    GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    }

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] expectedParameters = {1.0, 2.0, 3.0};
        double[] parameters = fitter.fit(initialGuess);
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        double[] expectedParameters = {1.0, 2.0, 3.0};
        double[] parameters = fitter.fit();
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = { 
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0),
            new WeightedObservedPoint(5.0, 6.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] expectedGuess = {6.0, 3.0, 2.0};
        double[] guess = guesser.guess();
        Assert.assertArrayEquals(expectedGuess, guess, 0.001);
    }
}