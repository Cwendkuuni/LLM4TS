import org.junit.Test;
import org.junit.Assert;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] parameters = fitter.fit(initialGuess);

        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new SomeOptimizer());
        double[] parameters = fitter.fit();

        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0),
            new WeightedObservedPoint(5.0, 6.0)
        };
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] guessedParams = guesser.guess();

        Assert.assertNotNull(guessedParams);
        Assert.assertEquals(3, guessedParams.length);
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullArray() {
        new GaussianFitter.ParameterGuesser(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testParameterGuesserWithLessThanThreePoints() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1.0, 2.0),
            new WeightedObservedPoint(3.0, 4.0)
        };
        new GaussianFitter.ParameterGuesser(points);
    }

    // Add more test cases for edge cases and exceptions

}