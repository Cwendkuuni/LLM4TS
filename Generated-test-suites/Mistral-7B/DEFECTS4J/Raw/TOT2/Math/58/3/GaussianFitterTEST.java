import org.apache.commons.math3.optim.Points2D;
import org.apache.commons.math3.optim.precise.WeightedObservedPoint;
import org.apache.commons.math3.optim.nonlinear.functions.Gaussian;
import org.apache.commons.math3.optim.nonlinear.functions.ParametricUnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.functions.ParameterizedFunction;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        // Test fitting a Gaussian function to some sample data
        double[] xData = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] yData = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);

        double[] parameters = fitter.fit();
        ParametricUnivariateFunction gaussianFunction = new Gaussian.Parametric();

        // Check that the fitted function approximates the data
        double errorSum = 0.0;
        for (WeightedObservedPoint point : observations) {
            double expected = gaussianFunction.value(point.getX(), parameters);
            double actual = point.getY();
            errorSum += Math.pow(expected - actual, 2.0);
        }
        double error = Math.sqrt(errorSum / observations.length);
        System.out.println("Error: " + error);
        Assert.assertEquals(0.0, error, 1e-6);

        // Check that the fitted function has the expected parameters
        double[] expectedParameters = {1.0, 4.05326982, 0.11171062};
        assertArrayEquals("Unexpected parameters: ", expectedParameters, parameters, 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((double[]) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {-1.0, 4.0, 0.1};
        fitter.fit(initialGuess);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testZeroInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {0.0, 4.0, 0.0};
        fitter.fit(initialGuess);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFewerThanThreeInitialGuesses() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = {1.0};
        fitter.fit(initialGuess);
    }

    // Add other test cases as needed for edge cases and exception handling

}