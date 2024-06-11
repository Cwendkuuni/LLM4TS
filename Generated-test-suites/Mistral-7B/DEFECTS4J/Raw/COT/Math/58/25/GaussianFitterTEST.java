package org.apache.commons.math.optimization.fitting; // adjust the package if needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Gradient2PointSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleGradientDescent;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.PointValuesAdapter;
import org.apache.commons.math3.optim.restart.Restart;
import org.apache.commons.math3.optim.restart.SimpleRestart;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private static final double EPSILON = 1e-10;

    private static final double[] X_DATA = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
    private static final double[] Y_DATA = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

    private static final double[] INITIAL_GUESS = {1.0, 0.0, 1.0};
    private static final double[] EXACT_SOLUTION = {Math.sqrt(Math.PI), 0.0, Math.sqrt(Math.PI) / 2.0};

    private static final ObjectiveFunction FITTING_FUNCTION = new ObjectiveFunction() {
        @Override
        public double value(double[] x) throws UnboundedDomainException {
            double norm = x[0];
            double mean = x[1];
            double sigma = x[2];

            ParametricUnivariateRealFunction gaussianFunction = new Gaussian.Parametric();
            double[] parameters = {norm, mean, sigma};
            double error = 0.0;

            for (int i = 0; i < X_DATA.length; i++) {
                double xValue = X_DATA[i];
                double expectedYValue = Y_DATA[i];
                double actualYValue = gaussianFunction.value(xValue, parameters);
                error += Math.pow(expectedYValue - actualYValue, 2.0);
            }
            return error;
        }

        @Override
        public int getNumberOfVariables() {
            return 3;
        }
    };

    private static final DifferentiableMultivariateVectorialOptimizer OPTIMIZER =
            new Gradient2PointSearch(new SimpleGradientDescent(0.1), new SimpleRestart(MaxEval.RESTART_AFTER, 10));

    private WeightedObservedPoint[] observations;

    @Parameterized.Parameters(name = "{index}: observations = {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                new Object[]{X_DATA.clone(), Y_DATA.clone()}
        });
    }

    @Parameterized.Constructor
    public GaussianFitterTest(double[] xData, double[] yData) {
        this.observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();

        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test
    public void testFitInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit(INITIAL_GUESS);

        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitTooFewObservations() {
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        fitter.addObservedPoint(4.0);
        fitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitInvalidInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        double[] invalidInitialGuess = {0.0, 0.0, -1.0};
        fitter.fit(invalidInitialGuess);
    }

    @Test
    public void testFitWrongNumberOfObservedPoints() {
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        WeightedObservedPoint[] wrongNumberOfObservedPoints = observations.clone();
        wrongNumberOfObservedPoints[0] = null;
        fitter.addObservedPoints(wrongNumberOfObservedPoints);
        assertArrayEquals(new double[3], fitter.fit(), EPSILON);
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitOutOfRangeObservedPoint() {
        WeightedObservedPoint[] observationsWithOutOfRangePoint = observations.clone();
        observationsWithOutOfRangePoint[0] = new WeightedObservedPoint(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0);
        GaussianFitter fitter = new GaussianFitter(OPTIMIZER);
        fitter.addObservedPoints(observationsWithOutOfRangePoint);
        try {
            fitter.fit();
            fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testFitBoundaryCases() {
        // Minimum number of observations
        GaussianFitter fitterMin = new GaussianFitter(OPTIMIZER);
        fitterMin.addObservedPoints(new WeightedObservedPoint[] {new WeightedObservedPoint(0.0, 0.0, 1.0)});
        double[] resultMin = fitterMin.fit();
        assertArrayEquals(new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0}, resultMin, EPSILON);

        // Mean equals maximum observed point
        WeightedObservedPoint[] observationsWithMaxMean = observations.clone();
        double maxX = Arrays.stream(X_DATA).max().getAsDouble();
        observationsWithMaxMean[0] = new WeightedObservedPoint(maxX, Double.MAX_VALUE, 1.0);
        GaussianFitter fitterMaxMean = new GaussianFitter(OPTIMIZER);
        fitterMaxMean.addObservedPoints(observationsWithMaxMean);
        double[] resultMaxMean = fitterMaxMean.fit();
        assertArrayEquals(new double[] {Double.POSITIVE_INFINITY, maxX, Double.POSITIVE_INFINITY}, resultMaxMean, EPSILON);

        // Mean equals minimum observed point
        WeightedObservedPoint[] observationsWithMinMean = observations.clone();
        double minX = Arrays.stream(X_DATA).min().getAsDouble();
        observationsWithMinMean[0] = new WeightedObservedPoint(minX, Double.MIN_VALUE, 1.0);
        GaussianFitter fitterMinMean = new GaussianFitter(OPTIMIZER);
        fitterMinMean.addObservedPoints(observationsWithMinMean);
        double[] resultMinMean = fitterMinMean.fit();
        assertArrayEquals(new double[] {Double.POSITIVE_INFINITY, minX, Double.POSITIVE_INFINITY}, resultMinMean, EPSILON);

        // Sigma equals zero
        WeightedObservedPoint[] observationsWithZeroSigma = observations.clone();
        observationsWithZeroSigma[0] = new WeightedObservedPoint(X_DATA[0], Y_DATA[0], 1.0);
        GaussianFitter fitterZeroSigma = new GaussianFitter(OPTIMIZER);
        fitterZeroSigma.addObservedPoints(observationsWithZeroSigma);
        double[] resultZeroSigma = fitterZeroSigma.fit();
        assertArrayEquals(new double[] {Double.POSITIVE_INFINITY, X_DATA[0], 0.0}, resultZeroSigma, EPSILON);

        // Sigma negative
        WeightedObservedPoint[] observationsWithNegativeSigma = observations.clone();
        observationsWithNegativeSigma[0] = new WeightedObservedPoint(X_DATA[0], Y_DATA[0], 1.0);
        GaussianFitter fitterNegativeSigma = new GaussianFitter(OPTIMIZER);
        fitterNegativeSigma.addObservedPoints(observationsWithNegativeSigma);
        try {
            double[] resultNegativeSigma = fitterNegativeSigma.fit();
            fail("NegativeSigmaException expected");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}