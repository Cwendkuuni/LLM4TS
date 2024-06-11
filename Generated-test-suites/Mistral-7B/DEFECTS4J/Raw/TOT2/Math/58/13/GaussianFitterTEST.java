import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Assert.assertTrue;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;

public class GaussianFitterTest {

    @Test
    public void testFittingWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        WeightedObservedPoint[] observations = createObservedPoints();
        double[] parameters = fitter.fit(createInitialGuess());

        UnivariateFunction function = new Gaussian.Parametric();
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = function.value(observations[0].getX(), parameters);
            assertTrue("Parameter " + i + " should be positive", parameters[i] > 0);
        }

        assertArrayEquals(createExpectedResults(), result, 0.0001);
    }

    @Test
    public void testFittingWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        WeightedObservedPoint[] observations = createObservedPoints();
        double[] parameters = fitter.fit();

        UnivariateFunction function = new Gaussian.Parametric();
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = function.value(observations[0].getX(), parameters);
            assertTrue("Parameter " + i + " should be positive", parameters[i] > 0);
        }

        assertArrayEquals(createExpectedResults(), result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        WeightedObservedPoint[] observations = null;
        fitter.fit(observations);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        WeightedObservedPoint[] observations = new WeightedObservedPoint[0];
        fitter.fit(observations);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeY() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        WeightedObservedPoint[] observations = createObservedPoints();
        WeightedObservedPoint wrongPoint = new WeightedObservedPoint(Double.NaN, Double.NaN, 0);
        observations = new WeightedObservedPoint[] { wrongPoint, observations[0] };
        try {
            fitter.fit(observations);
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeX() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        WeightedObservedPoint[] observations = createObservedPoints();
        WeightedObservedPoint wrongPoint = new WeightedObservedPoint(observations[0].getX(), Double.NaN, 0);
        observations = new WeightedObservedPoint[] { wrongPoint, observations[0] };
        try {
            fitter.fit(observations);
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci(10));
        double[] initialGuess = { -1, 1, 2 };
        try {
            fitter.fit(initialGuess);
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    private WeightedObservedPoint[] createObservedPoints() {
        return new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0),
                new WeightedObservedPoint(4.03128248, 984167.0),
                new WeightedObservedPoint(4.03839603, 1887233.0),
                new WeightedObservedPoint(4.04421621, 2687152.0),
                new WeightedObservedPoint(4.05132976, 3461228.0),
                new WeightedObservedPoint(4.05326982, 3580526.0),
                new WeightedObservedPoint(4.05779662, 3439750.0),
                new WeightedObservedPoint(4.0636168, 2877648.0),
                new WeightedObservedPoint(4.06943698, 2175960.0),
                new WeightedObservedPoint(4.07525716, 1447024.0),
                new WeightedObservedPoint(4.08237071, 717104.0),
                new WeightedObservedPoint(4.08366408, 620014.0)
        };
    }

    private double[] createInitialGuess() {
        return new double[] { 1., 4., 1. };
    }

    private double[] createExpectedResults() {
        return new double[] { 534318.89346157, 4.04052973, 0.19480763 };
    }

    @Test
    public void testFitWithInvalidOptimizer() {
        GaussianFitter gaussianFitter = new GaussianFitter(null);
        WeightedObservedPoint[] observations = createObservedPoints();
        try {
            gaussianFitter.fit(observations);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidGoalType() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new Fibonacci(10);
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = createObservedPoints();
        PointValuePair result = gaussianFitter.fit(observations, GoalType.MINVAL);
        assertTrue(Double.isNaN(result.getPoint()[0]));
        assertTrue(Double.isNaN(result.getPoint()[1]));
        assertTrue(Double.isNaN(result.getPoint()[2]));
    }
}