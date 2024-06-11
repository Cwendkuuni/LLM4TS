import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class GaussianFitterTest {

    @Test
    public void testConstructor() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        // Assert something about the fitter object
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        double[] expectedParameters = {1.0, 0.0, 1.0};
        assertArrayEquals(parameters, expectedParameters, 0.001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] parameters = fitter.fit();
        double[] expectedParameters = {1.0, 0.0, 1.0};
        assertArrayEquals(parameters, expectedParameters, 0.001);
    }

    @Test
    public void testParameterGuesser() {
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[]{
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
        });
        double[] parameters = guesser.guess();
        double[] expectedParameters = {3580526.0, 4.05326982, 0.210909};
        assertArrayEquals(parameters, expectedParameters, 0.001);
    }
}