import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FisherGaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.stat.function.Gaussian;
import org.apache.commons.math3.stat.function.ParametricUnivariateFunction;
import org.apache.commons.math3.test.AbstractTests;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class GaussianFitterTest extends AbstractTests {

    private static final double EPSILON = 1e-6;
    private static final double[] X_DATA = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408, 4.08720235 };
    private static final double[] Y_DATA = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0, 576218.0 };

    @BeforeClass
    public static void setup() {
        UnivariateOptimizer optimizerLM = new LevenbergMarquardtOptimizer();
        UnivariateOptimizer optimizerFG = new FisherGaussNewtonOptimizer();
        for (int i = 0; i < X_DATA.length; i++) {
            addObservation(X_DATA[i], Y_DATA[i]);
        }
        fitter = new GaussianFitter(optimizerLM);
        fitter.fit(new double[]{1.0, 4.0, 0.5});
    }

    private static GaussianFitter fitter;
    private static List<WeightedObservedPoint> observations;

    @Test
    public void testGaussianFitter() {
        assertNotNull(fitter);
    }

    @Test
    public void testFitNormalParameters() {
        double[] expected = new double[]{2.0, 4.0, 1.0};
        double[] actual = fitter.getParameters();
        assertArrayEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFitUsingFisherGaussNewton() {
        GaussianFitter fitterFG = new GaussianFitter(optimizerFG);
        double[] parametersFG = fitterFG.fit();
        assertArrayEquals(expectedGaussianFitParameters(), parametersFG, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidNumberOfParameters() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        fitter.fit(new double[]{1.0, 4.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidParameterValues() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        fitter.fit(new double[]{-1.0, 4.0, 0.5});
    }

    @Test
    public void testAddObservationNullX() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        try {
            fitter.addObservation(null, Y_DATA[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddObservationNullY() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        try {
            fitter.addObservation(X_DATA[0], null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddObservationInvalidX() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        try {
            fitter.addObservation(-1.0, Y_DATA[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddObservationInvalidY() {
        GaussianFitter fitter = new GaussianFitter(optimizerLM);
        try {
            fitter.addObservation(X_DATA[0], -1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetObservations() {
        WeightedObservedPoint[] expected = observations.toArray(new WeightedObservedPoint[0]);
        WeightedObservedPoint[] actual = fitter.getObservations();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetFunction() {
        ParametricUnivariateFunction expected = new Gaussian.Parametric();
        ParametricUnivariateFunction actual = fitter.getFunction();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetInitialGuess() {
        double[] expected = new double[]{1.0, 4.0, 0.5};
        double[] actual = fitter.getInitialGuess();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetMinimum() {
        double expectedMin = Double.NEGATIVE_INFINITY;
        double actualMin = fitter.getMinimum();
        assertEquals(expectedMin, actualMin, EPSILON);
    }

    @Test
    public void testGetMaximum() {
        double expectedMax = Double.POSITIVE_INFINITY;
        double actualMax = fitter.getMaximum();
        assertEquals(expectedMax, actualMax, EPSILON);
    }

    @Test
    public void testGetBounds() {
        double[] expectedLower = new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        double[] expectedUpper = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        SimpleBounds expectedBounds = new SimpleBounds(expectedLower, expectedUpper);
        SimpleBounds actualBounds = fitter.getBounds();
        assertEquals(expectedBounds, actualBounds);
    }

    @Test
    public void testGetGoalType() {
        GoalType expected = GoalType.MINIMIZE;
        GoalType actual = fitter.getGoalType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetObjectiveFunction() {
        ObjectiveFunction expected = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                double p1 = x[0];
                double p2 = x[1];
                double p3 = x[2];
                ParametricUnivariateFunction g = new Gaussian.Parametric();
                double y = g.value(X_DATA[0], new double[]{p1, p2, p3});
                return y;
            }

            @Override
            public double[] gradient(double[] x) {
                double p1 = x[0];
                double p2 = x[1];
                double p3 = x[2];
                ParametricUnivariateFunction g = new Gaussian.Parametric();
                double[] gradient = g.gradient(X_DATA[0], new double[]{p1, p2, p3});
                return gradient;
            }
        };
        ObjectiveFunction actual = fitter.getObjectiveFunction();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptimizer() {
        LevenbergMarquardtOptimizer expectedOptimizer = (LevenbergMarquardtOptimizer) fitter.getOptimizer();
        assertEquals(expectedOptimizer.getClass(), fitter.getOptimizer().getClass());
        assertEquals(expectedOptimizer.getName(), fitter.getOptimizer().getName());
    }

    @Test
    public void testGetInitialGuessUsingAveragedInitialGuess() {
        GaussianFitter fitterWithAIG = new GaussianFitter(new AveragedInitialGuess(X_DATA, Y_DATA));
        double[] actual = fitterWithAIG.getInitialGuess();
        double[] expected = new double[]{1.0, 4.0, 1.0};
        assertArrayEquals(expected, actual);
    }

    private void addObservation(double x, double y) {
        WeightedObservedPoint obs = new WeightedObservedPoint(x, y, 1.0);
        fitter.addObservation(obs);
        observations.add(obs);
    }

    private double[] expectedGaussianFitParameters() {
        double mean = (X_DATA[X_DATA.length - 1] + X_DATA[0]) / 2.0;
        double variance = 0.5 * ((X_DATA[X_DATA.length - 1] - X_DATA[0]) / 2.0) * Math.log(2.0);
        return new double[]{Math.sqrt(variance), mean, Math.sqrt(variance)};
    }
}