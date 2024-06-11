import org.apache.commons.math.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.ParameterGuesser;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new MockOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = {1.0, 0.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new MockOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] parameters = fitter.fit();
        assertNotNull(parameters);
        assertEquals(3, parameters.length);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(0.0, 1.0, 0.0),
            new WeightedObservedPoint(1.0, 2.0, 0.0)
        };
        ParameterGuesser guesser = new ParameterGuesser(points);
        double[] params = guesser.guess();
        assertNotNull(params);
        assertEquals(3, params.length);
    }

    // Mock optimizer for testing purposes
    private static class MockOptimizer implements DifferentiableMultivariateVectorialOptimizer {
        public double[] optimize() {
            return new double[] {1.0, 0.0, 1.0};
        }
    }
}