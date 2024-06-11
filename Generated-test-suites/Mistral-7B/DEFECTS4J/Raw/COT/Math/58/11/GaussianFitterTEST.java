import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private static final double EPSILON = 1e-6;
    private static final double[] DATA = { 4.0254623, 531026.0, 4.03128248, 984167.0,
            4.03839603, 1887233.0, 4.04421621, 2687152.0, 4.05132976, 3461228.0,
            4.05326982, 3580526.0, 4.05779662, 3439750.0, 4.0636168, 2877648.0,
            4.06943698, 2175960.0, 4.07525716, 1447024.0, 4.08237071, 717104.0,
            4.08366408, 620014.0 };

    private static final double[] INITIAL_GUESS = { 1.0, 4.0, 1.0 };

    private static final UnivariateFunction FUNCTION = new Gaussian.Parametric();

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new LevenbergMarquardtOptimizer() },
                { new GaussNewtonOptimizer() }
        });
    }

    private final UnivariateOptimizer optimizer;

    public GaussianFitterTest(UnivariateOptimizer optimizer) {
        this.optimizer = optimizer;
    }

    @Test
    public void testFit() throws Exception {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[DATA.length];
        for (int i = 0; i < DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(DATA[i], 1.0);
        }
        CurveFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();
        double[] expected = { FunctionUtils.getValue(FUNCTION, INITIAL_GUESS[0], INITIAL_GUESS[1]),
                INITIAL_GUESS[1], INITIAL_GUESS[2] };
        Assert.assertArrayEquals(expected, result, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullData() {
        WeightedObservedPoint[] observations = null;
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullOptimizer() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[DATA.length];
        UnivariateOptimizer optimizer = null;
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithTooFewData() {
        WeightedObservedPoint observations[] = new WeightedObservedPoint[2];
        observations[0] = new WeightedObservedPoint(1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 2.0);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        fitter.fit();
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitWithInvalidData() throws Exception {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[DATA.length + 1];
        System.arraycopy(DATA, 0, observations, 0, DATA.length);
        observations[DATA.length] = new WeightedObservedPoint(Double.NEGATIVE_INFINITY, 1.0);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        try {
            fitter.fit();
        } catch (OutOfRangeException e) {
            Assert.assertEquals("Weighted observed point y value is out of range: " + e.getMessage(),
                              Double.NEGATIVE_INFINITY, e.getValue());
            throw e;
        }
    }

    @Test(expected = ZeroException.class)
    public void testGuesserWithEmptyData() {
        WeightedObservedPoint[] observations = {};
        ParameterGuesser guesser = new ParameterGuesser(observations);
        try {
            guesser.guess();
        } catch (ZeroException e) {
            Assert.assertEquals("The size of the observations array must be greater than zero",
                              "The size of the observations array must be greater than zero", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ZeroException.class)
    public void testGuesserWithOneDataPoint() {
        WeightedObservedPoint[] observations = { new WeightedObservedPoint(1.0, 1.0) };
        ParameterGuesser guesser = new ParameterGuesser(observations);
        try {
            guesser.guess();
        } catch (ZeroException e) {
            Assert.assertEquals("The size of the observations array must be greater than or equal to 3",
                              "The size of the observations array must be greater than or equal to 3", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGuesser() throws Exception {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[DATA.length];
        System.arraycopy(DATA, 0, observations, 0, DATA.length);
        ParameterGuesser guesser = new ParameterGuesser(observations);
        double[] result = guesser.guess();
        double[] expected = { FunctionUtils.getValue(FUNCTION, INITIAL_GUESS[0], INITIAL_GUESS[1]),
                INITIAL_GUESS[1], INITIAL_GUESS[2] };
        Assert.assertArrayEquals(expected, result, EPSILON);
    }

    @Test
    public void testFitAndCheckGuessedParameters() throws Exception {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[DATA.length];
        System.arraycopy(DATA, 0, observations, 0, DATA.length);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();
        ParameterGuesser guesser = new ParameterGuesser(observations);
        double[] guessedParameters = guesser.guess();
        Assert.assertArrayEquals(expectedValues(guessedParameters), result, EPSILON);
    }

    private double[] expectedValues(double[] guessedParameters) {
        double mean = guessedParameters[1];
        double norm = guessedParameters[0];
        double sigma = guessedParameters[2];
        UnivariateFunction function = new ParametricUnivariateRealFunction() {
            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = FUNCTION.value(x, new double[] { norm, mean, sigma });
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };
        PointValuePairList pvs = new PointValuePairList();
        LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer();
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            PointValuePair pvp = lmo.optimize(function, new SimpleBounds(-10.0, 10.0), INITIAL_GUESS);
            pvs.add(new PointValuePair(x, pvp.getValue()));
        }
        double[] expected = new double[3];
        expected[0] = FunctionUtils.getValue(function, INITIAL_GUESS[0], INITIAL_GUESS[1]);
        expected[1] = mean;
        expected[2] = sigma;
        for (int i = 0; i < 3; i++) {
            double tolerance = 1e-5 * Math.max(Math.abs(expected[i]), 1.0);
            Assert.assertTrue("Expected parameter " + i + " to be within " + tolerance + " of the guessed value",
                             Math.abs(guessedParameters[i] - expected[i]) <= tolerance);
        }
        return expected;
    }
}