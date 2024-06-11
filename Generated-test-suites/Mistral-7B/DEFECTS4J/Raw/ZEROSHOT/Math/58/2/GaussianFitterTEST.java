package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.GammaFunction;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.statistic.RankSum;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test data.
     */
    private static final double[] TEST_DATA = { 4.0254623, 531026.0,
        4.03128248, 984167.0, 4.03839603, 1887233.0, 4.04421621, 2687152.0,
        4.05132976, 3461228.0, 4.05326982, 3580526.0, 4.05779662, 3439750.0,
        4.06361680, 2877648.0, 4.06943698, 2175960.0, 4.07525716, 1447024.0,
        4.08237071, 717104.0, 4.08366408, 620014.0 };

    /**
     * Test data with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitterWithNullObservedPoints() {
        new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
    }

    /**
     * Test fitting a Gaussian function to the given observed points.
     */
    @Test
    public void testFit() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        fitter.addObservedPoint(observations);
        double[] expected = { 129262.4343652006, 4.04784109403934, 0.3782604534409799 };
        double[] result = fitter.fit();
        assertArrayEquals(expected, result, 1.0E-8);
    }

    /**
     * Test fitting a Gaussian function to the given observed points with an initial
     * guess.
     */
    @Test
    public void testFitWithInitialGuess() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        double[] initialGuess = { 100000.0, 4.0, 0.5 };
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(new double[] { 129262.4343652006, 4.04784109403934, 0.3782604534409799 }, result, 1.0E-8);
    }

    /**
     * Test exception when fitting a Gaussian function with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuess() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        double[] initialGuess = { -1.0, 4.0, 0.5 };
        fitter.fit(initialGuess);
    }

    /**
     * Test exception when fitting a Gaussian function with no observed points.
     */
    @Test(expected = IllegalStateException.class)
    public void testFitWithEmptyObservedPoints() {
        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        fitter.fit(new WeightedObservedPoint[0]);
    }

    /**
     * Test exception when fitting a Gaussian function with a non-differentiable
     * optimizer.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFitWithNonDifferentiableOptimizer() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }

        DifferentiableMultivariateVectorialOptimizer optimizer = mock(DifferentiableMultivariateVectorialOptimizer.class);
        when(optimizer.minimize(any(ObjectiveFunction.class), any(InitialGuess.class), any(MaxEval.class), any(GoalType.class),
                any(SimpleBounds.class))).thenThrow(new UnsupportedOperationException());

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(observations);
        fitter.fit();
    }

    /**
     * Test calculating the mean and standard deviation.
     */
    @Test
    public void testMeanAndStandardDeviation() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        fitter.addObservedPoint(observations);
        double[] parameters = fitter.fit();

        UnivariateFunction function = new Gaussian.Parametric();
        double mean = FunctionUtils.value(function, parameters[1]);
        double stdDev = Math.sqrt(parameters[2]);

        double[] dataCopy = Arrays.copyOfRange(TEST_DATA, 0, TEST_DATA.length);
        double calculatedMean = new Mean(dataCopy).getValue();
        Double calculatedStdDev = new StandardDeviation(dataCopy).getValue();

        assertEquals(calculatedMean, mean, 1.0E-8);
        assertEquals(calculatedStdDev.doubleValue(), stdDev, 1.0E-8);
    }

    /**
     * Test rank-sum test.
     */
    @Test
    public void testRankSumTest() {
        WeightedObservedPoint[] observationsA = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observationsA[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }

        WeightedObservedPoint[] observationsB = Arrays.copyOfRange(observationsA, 0, TEST_DATA.length / 2);

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        fitter.addObservedPoint(observationsA);
        double[] parameters = fitter.fit();

        double[] dataA = Arrays.copyOfRange(TEST_DATA, 0, TEST_DATA.length);
        RankSum rankSumTest = new RankSum(dataA);
        double rankSumTestResult = rankSumTest.getStatistic();

        GaussianFitter fitterB = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        fitterB.addObservedPoint(observationsB);
        double[] parametersB = fitterB.fit();
        UnivariateFunction functionB = new Gaussian.Parametric();
        double meanB = FunctionUtils.value(functionB, parametersB[1]);

        double rankSumExpected = Erf.erf((TEST_DATA.length - (TEST_DATA.length / 2)) / (2.0 * Math.sqrt(Math.PI) * stdDev));
        assertEquals(rankSumExpected, rankSumTestResult, 1.0E-8);
        assertEquals(Mean.VALUE, meanB, 1.0E-8);
    }

    /**
     * Test fitting a Gaussian function to a small dataset.
     */
    @Test
    public void testFitWithSmallDataset() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[2];
        observations[0] = new WeightedObservedPoint(1.0, 1.0);
        observations[1] = new WeightedObservedPoint(2.0, 1.0);

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer(1.0E-8, 1.0E-8));
        fitter.addObservedPoint(observations);
        double[] parameters = fitter.fit();

        UnivariateFunction function = new Gaussian.Parametric();
        assertEquals(1.5, FunctionUtils.value(function, parameters[1]), 1.0E-6);
        assertEquals(0.4825989, parameters[2], 1.0E-6);
    }
}