import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.GaussianInitialGuess;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.adapter.NonlinearPointValuePair;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.optim.points.impl.DefaultPointValues;
import org.apache.commons.math3.optim.points.impl.DenseDoubleMatrix2D;
import org.junit.Before;
import org.junit.Test;

public class GaussianFitterTest {

    private GaussianFitter fitter;
    private double[] initialGuess;
    private WeightedObservedPoint[] points;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        initialGuess = new double[]{1.0, 1.0, 1.0};
        points = new WeightedObservedPoint[]{
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
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0)};
    }

    @Test
    public void testFitMethodWithInitialGuess() {
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(new double[]{1.612323432953844, 4.039736284280269, 0.224963234257604}, result, 0.001);
    }

    @Test
    public void testFitMethodWithoutInitialGuess() {
        double[] result = fitter.fit();
        assertArrayEquals(new double[]{1.612323432953844, 4.039736284280269, 0.224963234257604}, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitMethodNullObservations() {
        try {
            fitter.fit();
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception type", e);
        }
        fitter.addObservedPoint(null, 0.0);
        try {
            fitter.fit();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitMethodWithTooFewObservations() {
        fitter.addObservedPoint(4.0, 1.0);
        try {
            fitter.fit();
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception type", e);
        }
        fitter.addObservedPoint(5.0, 2.0);
        try {
            fitter.fit();
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObservedPointWithNullX() {
        try {
            fitter.addObservedPoint(null, 1.0);
        } catch (NullArgumentException e) {
            throw new AssertionError("Unexpected exception type", e);
        }
        try {
            fitter.addObservedPoint(new WeightedObservedPoint(null, 1.0));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObservedPointWithNullY() {
        WeightedObservedPoint point = new WeightedObservedPoint(4.0, null);
        try {
            fitter.addObservedPoint(point);
        } catch (NullArgumentException e) {
            throw new AssertionError("Unexpected exception type", e);
        }
        try {
            fitter.addObservedPoint(new WeightedObservedPoint(4.0, Double.NaN));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddObservedPoint() {
        fitter.addObservedPoint(4.0254623, 531026.0);
        assertEquals(11, fitter.getNumberOfObservedPoints());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithInvalidInitialGuess() {
        final double[] invalidInitialGuess = {-1.0, 1.0, 1.0};
        try {
            fitter.fit(invalidInitialGuess);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithNegativeInitialGuessNorm() {
        final double[] negativeInitialGuess = {-1.0, 1.0, 1.0};
        try {
            fitter.fit(negativeInitialGuess);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithZeroInitialGuessNorm() {
        final double[] zeroInitialGuess = {0.0, 1.0, 1.0};
        try {
            fitter.fit(zeroInitialGuess);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithNegativeInitialGuessSigma() {
        final double[] negativeInitialGuess = {1.0, 1.0, -1.0};
        try {
            fitter.fit(negativeInitialGuess);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithZeroInitialGuessSigma() {
        final double[] zeroInitialGuess = {1.0, 1.0, 0.0};
        try {
            fitter.fit(zeroInitialGuess);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitMethodWithNegativeInitialGuessMean() {
        final double[] negativeInitialGuess = {1.0, -1.0, 1.0};
        try {
            fitter.fit(negativeInitialGuess);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception type", e);
        }
        try {
            fitter.fit(new double[]{1.0, -1.0, 1.0});
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }
}