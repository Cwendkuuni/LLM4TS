import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.LevenbergMarquardtOptimizer;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = {1.0, 0.0, 1.0};
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(0.0, 1.0);
        fitter.addObservedPoint(1.0, 2.0);
        fitter.addObservedPoint(2.0, 3.0);

        double[] parameters = fitter.fit(initialGuess);

        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more assertions based on expected values
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(0.0, 1.0);
        fitter.addObservedPoint(1.0, 2.0);
        fitter.addObservedPoint(2.0, 3.0);

        double[] parameters = fitter.fit();

        assertNotNull(parameters);
        assertEquals(3, parameters.length);
        // Add more assertions based on expected values
    }

    @Test(expected = NullArgumentException.class)
    public void testParameterGuesserWithNullObservations() {
        WeightedObservedPoint[] observations = null;
        GaussianFitter.ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
    }

    // Add more test cases to cover edge cases and exception handling scenarios

}