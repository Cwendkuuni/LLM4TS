package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.GaussLegendreP;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GaussianFitterTest {

    private static final double DELTA = 1e-5;
    private static final double EPSILON = 1e-10;

    private WeightedObservedPoint[] testData1;
    private WeightedObservedPoint[] testData2;
    private WeightedObservedPoint[] testData3;
    private GaussianFitter fitter;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    }

    @Test
    public void testFitGaussianFunction_Basic() {
        testData1 = new WeightedObservedPoint[]{
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

        double[] result = fitter.fit(testData1);
        double[] expected = new double[]{1500000.0, 4.06728663, 0.18713918};

        Assert.assertArrayEquals(expected, result, DELTA);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitGaussianFunction_NullObservations() {
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitGaussianFunction_EmptyObservations() {
        WeightedObservedPoint[] emptyData = {};
        fitter.fit(emptyData);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitGaussianFunction_InvalidInitialGuess() {
        double[] invalidGuess = {-1.0, 1.0, 1.0};
        fitter.fit(invalidGuess);
    }

    @Test
    public void testFitGaussianFunction_GaussianFunction() {
        UnivariateFunction gaussian = new Gaussian(MathConstants.PI * Math.sqrt(2.0) / 2.0, 1.0, 0.0);
        List<PointValuePair<Double, Double>> points = GaussLegendreP.generatePoints(10, 0.0, 1.0);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.size()];
        for (int i = 0; i < points.size(); i++) {
            observations[i] = new WeightedObservedPoint(points.get(i).getX(), gaussian.value(points.get(i).getX()));
        }
        double[] result = fitter.fit(observations);

        double[] expected = new double[]{Math.exp(Math.PI * Math.sqrt(2.0) / 2.0),
                observations[0].getX(), Math.sqrt(2.0) / Math.sqrt(Math.PI)};

        Assert.assertArrayEquals(expected, result, DELTA);
    }

    @Test
    public void testFitGaussianFunction_LargeData() {
        testData2 = new WeightedObservedPoint[10000];
        for (int i = 0; i < testData2.length; i++) {
            testData2[i] = new WeightedObservedPoint(i / 100.0, Erf.erf(i / 10.0));
        }
        double[] result = fitter.fit(testData2);
        double[] expected = new double[]{Math.exp(Math.PI / 4.0), 0.0, Math.sqrt(Math.PI / 2.0)};

        Assert.assertArrayEquals(expected, result, DELTA);
    }

    @Test
    public void testFitGaussianFunction_LargeData_LargeSigma() {
        testData3 = new WeightedObservedPoint[10000];
        for (int i = 0; i < testData3.length; i++) {
            testData3[i] = new WeightedObservedPoint(i / 100.0, Erf.erf(i / 10.0 + 2.0));
        }
        double[] result = fitter.fit(testData3);
        double[] expected = new double[]{Math.exp(Math.PI / 4.0), 0.0, Math.sqrt(Math.PI / 2.0) * Math.sqrt(5.0)};

        Assert.assertArrayEquals(expected, result, DELTA);
    }
}