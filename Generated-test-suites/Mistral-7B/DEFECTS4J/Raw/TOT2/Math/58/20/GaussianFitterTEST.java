import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.ArrayRealVector;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorFunction;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Test class for {@code GaussianFitter}.
 */
public class GaussianFitterTest {

    /** Optimizer to use for the fitting. */
    private UnivariateOptimizer optimizer;
    /** Observed points to test {@code GaussianFitter} with. */
    private WeightedObservedPoint[] observations;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new org.apache.commons.math3.optim.nonlinear.GaussNewtonFinder();
        observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
                new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
                new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
                new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
                new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
                new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
                new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
                new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
                new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
                new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0),
        };
    }

    /**
     * Tests {@code GaussianFitter#fit(double[], double[])}.
     */
    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] parameters = fitter.fit(observations);
        double[] expectedParameters = { 1.0, 4.054527066926709, 0.24885621282188954 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    /**
     * Tests {@code GaussianFitter#fit()} with default initial guess.
     */
    @Test
    public void testFitDefaultGuess() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] parameters = fitter.fit();
        double[] expectedParameters = { 1.0, 4.054527066926709, 0.24885621282188954 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    /**
     * Tests {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     */
    @Test
    public void testFitFunction() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] parameters = fitter.fit(function, observations);
        double[] expectedParameters = { 1.0, 4.054527066926709, 0.24885621282188954 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    /**
     * Tests {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}
     * with default initial guess.
     */
    @Test
    public void testFitFunctionDefaultGuess() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] parameters = fitter.fit(function);
        double[] expectedParameters = { 1.0, 4.054527066926709, 0.24885621282188954 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    /**
     * Tests {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[])}
     * with custom initial guess.
     */
    @Test
    public void testFitFunctionCustomGuess() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = { 0.5, 4.0, 0.3 };
        double[] parameters = fitter.fit(function, initialGuess);
        double[] expectedParameters = { 1.0, 4.054527066926709, 0.24885621282188954 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    /**
     * Tests {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[], SimpleBounds)}
     * with custom initial guess and bounds.
     */
    @Test
    public void testFitFunctionCustomGuessAndBounds() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = { 0.5, 4.0, 0.3 };
        SimpleBounds bounds = new SimpleBounds(new ArrayRealVector(new double[] { 0.1, 1.0, 0.01 }),
                                                new ArrayRealVector(new double[] { 2.0, 10.0, 1.0 }));
        double[] parameters = fitter.fit(function, initialGuess, bounds);
        double[] expectedParameters = { 1.0, 4.054527066926709, 0.24885621282188954 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}
     * with non-strictly positive initial guess.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitFunctionNonStrictlyPositiveInitialGuess() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = { -1.0, 4.0, 0.3 };
        fitter.fit(function, initialGuess);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[])}
     * with non-strictly positive initial guess.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitFunctionNonStrictlyPositiveInitialGuess2() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = { 0.0, 4.0, -1.0 };
        fitter.fit(function, initialGuess);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[], SimpleBounds)}
     * with non-strictly positive initial guess.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitFunctionCustomGuessAndBoundsNonStrictlyPositive() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = { 0.0, 4.0, -1.0 };
        SimpleBounds bounds = new SimpleBounds(new ArrayRealVector(new double[] { 0.1, 1.0, 0.01 }),
                                                new ArrayRealVector(new double[] { 2.0, 10.0, 1.0 }));
        fitter.fit(function, initialGuess, bounds);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     */
    @Test(expected = NullPointerException.class)
    public void testFitNullFunction() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null, observations);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[])}
     * with null function.
     */
    @Test(expected = NullPointerException.class)
    public void testFitFunctionNull() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new Gaussian.Parametric(), null);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[])}.
     */
    @Test(expected = NullPointerException.class)
    public void testFitFunctionAndPointsNull() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null, observations);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[], SimpleBounds)}
     * with null points.
     */
    @Test(expected = NullPointerException.class)
    public void testFitFunctionCustomGuessAndBoundsNull() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new Gaussian.Parametric(), null, observations);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitFunctionEmptyPoints() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(function, new WeightedObservedPoint[] {});
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitFunctionNullPoints() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(function, null);
    }

    /**
     * Tests exception thrown by {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[], SimpleBounds)}
     * with null bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitFunctionCustomGuessAndBoundsNullBounds() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(function, observations, null);
    }

    /**
     * Tests {@code GaussianFitter#fit(ParametricUnivariateRealFunction, double[], double[], SimpleBounds)}
     * with invalid goal type.
     */
    @Test
    public void testFitFunctionCustomGoalType() {
        final ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        SimpleBounds bounds = new SimpleBounds(new ArrayRealVector(new double[] { 0.1, 1.0, 0.01 }),
                                                new ArrayRealVector(new double[] { 2.0, 10.0, 1.0 }));
        fitter.setGoalType(GoalType.MILP_SENSE);
        fitter.fit(function, observations, bounds);
    }
}