import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.GaussianFunction;
import org.apache.commons.math3.optim.nonlinear.vector.WeightedObservedPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class GaussianFitterTest {

    private GaussianFitter fitter;
    private double[] testDataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
    private double[] testDataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

    @Before
    public void setUp() {
        UnivariateOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer();
        fitter = new GaussianFitter(optimizer);
    }

    @Test
    public void testFitWithValidData() {
        WeightedObservedPoint[] weightedObservedPointArray = new WeightedObservedPoint[testDataX.length];
        for (int i = 0; i < testDataX.length; i++) {
            weightedObservedPointArray[i] = new WeightedObservedPoint(testDataX[i], testDataY[i], 1.0);
            fitter.addObservedPoint(testDataX[i], testDataY[i]);
        }
        double[] parameters = fitter.fit();

        double[] expectedParameters = {1.0, 4.05326982, 0.12538598};
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidInitialGuess() {
        double[] initialGuess = {-1.0, 4.05326982, 0.12538598};
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitWithEmptyData() {
        WeightedObservedPoint[] weightedObservedPointArray = new WeightedObservedPoint[0];
        fitter.fit(weightedObservedPointArray);
        Assert.assertArrayEquals(new double[0], fitter.getFittedParameters(), 0.0);
    }

    @Test
    public void testBasicGuess() {
        WeightedObservedPoint[] weightedObservedPointArray = new WeightedObservedPoint[testDataX.length];
        for (int i = 0; i < testDataX.length; i++) {
            weightedObservedPointArray[i] = new WeightedObservedPoint(testDataX[i], testDataY[i], 1.0);
        }
        ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(weightedObservedPointArray);
        double[] parameters = parameterGuesser.guess();
        double[] expectedParameters = {1.0, 4.05326982, 0.12538598};
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);
    }

    @Test
    public void testInterpolateXAtY() {
        WeightedObservedPoint[] weightedObservedPointArray = new WeightedObservedPoint[testDataX.length];
        for (int i = 0; i < testDataX.length; i++) {
            weightedObservedPointArray[i] = new WeightedObservedPoint(testDataX[i], testDataY[i], 1.0);
        }

        double[] interpolatedXAtYArray = new double[testDataX.length];
        for (int i = 0; i < testDataX.length; i++) {
            interpolatedXAtYArray[i] = GaussianFitter.interpolateXAtY(weightedObservedPointArray, i, 1, testDataY[i]);
        }

        GaussianFunction gaussianFunction = new GaussianFunction();
        double[] gaussianFittedParameters = GaussianFitter.fit(testDataX, testDataY);
        double gaussianFittedValue = gaussianFunction.value(testDataX[0], gaussianFittedParameters);
        double expectedInterpolatedXAtY = gaussianFunction.inverse(gaussianFittedValue);

        for (int i = 0; i < testDataX.length; i++) {
            Assert.assertEquals(expectedInterpolatedXAtY, interpolatedXAtYArray[i], 0.001);
        }
    }

    @Test
    public void testFindMaxY() {
        WeightedObservedPoint[] weightedObservedPointArray = new WeightedObservedPoint[testDataX.length];
        for (int i = 0; i < testDataX.length; i++) {
            weightedObservedPointArray[i] = new WeightedObservedPoint(testDataX[i], testDataY[i], 1.0);
        }

        int maxYIndex = GaussianFitter.findMaxY(weightedObservedPointArray);
        double maxYValue = weightedObservedPointArray[maxYIndex].getY();

        int expectedMaxYIndex = testDataY.length - 1;
        double expectedMaxYValue = testDataY[testDataY.length - 1];
        Assert.assertEquals(expectedMaxYIndex, maxYIndex);
        Assert.assertEquals(expectedMaxYValue, maxYValue, 0.001);
    }

    @Test
    public void testInterpolationPointsForY() {
        WeightedObservedPoint[] weightedObservedPointArray = new WeightedObservedPoint[testDataX.length];
        for (int i = 0; i < testDataX.length; i++) {
            weightedObservedPointArray[i] = new WeightedObservedPoint(testDataX[i], testDataY[i], 1.0);
        }

        WeightedObservedPoint[] interpolationPointsForYArray = GaussianFitter.getInterpolationPointsForY(weightedObservedPointArray, 0, 2, testDataY[1]);

        double interpolationPointAx = interpolationPointsForYArray[0].getX();
        double interpolationPointBx = interpolationPointsForYArray[1].getX();
        double interpolationPointAy = interpolationPointsForYArray[0].getY();
        double interpolationPointBy = interpolationPointsForYArray[1].getY();

        double expectedInterpolationPointAx = testDataX[1];
        double expectedInterpolationPointBx = testDataX[2];
        double expectedInterpolationPointAy = testDataY[1];
        double expectedInterpolationPointBy = testDataY[2];

        Assert.assertEquals(expectedInterpolationPointAx, interpolationPointAx, 0.001);
        Assert.assertEquals(expectedInterpolationPointBx, interpolationPointBx, 0.001);
        Assert.assertEquals(expectedInterpolationPointAy, interpolationPointAy, 0.001);
        Assert.assertEquals(expectedInterpolationPointBy, interpolationPointBy, 0.001);
    }

    @Test
    public void testIsBetween() {
        Assert.assertTrue(GaussianFitter.isBetween(testDataY[1], testDataY[0], testDataY[2]));
        Assert.assertTrue(GaussianFitter.isBetween(testDataY[1], testDataY[2], testDataY[0]));

        Assert.assertFalse(GaussianFitter.isBetween(testDataY[0], testDataY[1], testDataY[2]));
        Assert.assertFalse(GaussianFitter.isBetween(testDataY[1], testDataY[2], testDataY[0]));
        Assert.assertFalse(GaussianFitter.isBetween(testDataY[2], testDataY[1], testDataY[0]));
        Assert.assertFalse(GaussianFitter.isBetween(testDataY[0], testDataY[1], testDataY[1]));
        Assert.assertFalse(GaussianFitter.isBetween(testDataY[1], testDataY[0], testDataY[1]));
        Assert.assertFalse(GaussianFitter.isBetween(testDataY[1], testDataY[1], testDataY[1]));

        double notInRangeValue = testDataY[0] + 10;
        Assert.assertFalse(GaussianFitter.isBetween(notInRangeValue, testDataY[0], testDataY[1]));
        Assert.assertFalse(GaussianFitter.isBetween(testDataY[0], testDataY[0], testDataY[1]));
        Assert.assertFalse(GaussianFitter.isBetween(notInRangeValue, testDataY[0], testDataY[1]));
    }

    @Test
    public void testCreateWeightedObservedPointComparator() {
        Comparator<WeightedObservedPoint> comparator = GaussianFitter.createWeightedObservedPointComparator();

        WeightedObservedPoint point1 = new WeightedObservedPoint(1.0, 10.0, 1.0);
        WeightedObservedPoint point2 = new WeightedObservedPoint(2.0, 20.0, 2.0);
        WeightedObservedPoint point3 = new WeightedObservedPoint(3.0, 30.0, 3.0);

        int result = comparator.compare(point1, point2);
        int expectedResult = -1;
        Assert.assertEquals(expectedResult, result);

        result = comparator.compare(point2, point3);
        expectedResult = 1;
        Assert.assertEquals(expectedResult, result);

        result = comparator.compare(point1, point1);
        expectedResult = 0;
        Assert.assertEquals(expectedResult, result);

        result = comparator.compare(point2, point1);
        expectedResult = 1;
        Assert.assertEquals(expectedResult, result);

        result = comparator.compare(point1, point3);
        expectedResult = -1;
        Assert.assertEquals(expectedResult, result);
    }
}