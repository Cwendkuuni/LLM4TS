import org.junit.Test;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    @Test
    public void testFit_withInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] initialGuess = {1.0, 2.0, 3.0};
        double[] parameters = fitter.fit(initialGuess);

        // Assertion statements
        // ...
    }

    @Test
    public void testFit_withoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new DifferentiableMultivariateVectorialOptimizer());
        double[] parameters = fitter.fit();

        // Assertion statements
        // ...
    }

    @Test
    public void testParameterGuesser_guess() {
        WeightedObservedPoint[] observedPoints = {new WeightedObservedPoint(4.0254623, 531026.0), 
                                                new WeightedObservedPoint(4.03128248, 984167.0),
                                                new WeightedObservedPoint(4.03839603, 1887233.0),
                                                new WeightedObservedPoint(4.04421621, 2687152.0)};
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observedPoints);
        double[] parameters = parameterGuesser.guess();

        // Assertion statements
        // ...
    }
}