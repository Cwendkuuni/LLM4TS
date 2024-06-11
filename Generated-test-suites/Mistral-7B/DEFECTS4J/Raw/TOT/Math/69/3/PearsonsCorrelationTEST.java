package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    /**
     * Test correlation calculation for two arrays with known correlation value
     */
    @Test
    public void testCorrelationWithKnownValues() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {1.1, 2.2, 3.3, 4.4};
        double correlationExpected = 0.9;

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlationActual = pearsonsCorrelation.correlation(xArray, yArray);

        Assert.assertEquals("Calculated correlation does not match expected", correlationExpected, correlationActual, 0.001);
    }

    /**
     * Test correlation calculation for two matrices with known correlation matrix
     */
    @Test
    public void testCorrelationMatrixWithKnownValues() {
        double[][] xData = {{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}};
        double[][] yData = {{1.1, 1.2, 1.3}, {2.1, 2.2, 2.3}, {3.1, 3.2, 3.3}, {4.1, 4.2, 4.3}};
        double correlationExpected = new SimpleRegression().calculateRegressionCoefficients(xData, yData)[0];

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix xMatrix = new BlockRealMatrix(new Array2DRowRealMatrix(xData));
        RealMatrix yMatrix = new BlockRealMatrix(new Array2DRowRealMatrix(yData));
        RealMatrix correlationMatrixExpected = pearsonsCorrelation.computeCorrelationMatrix(xMatrix);
        RealMatrix correlationMatrixActual = pearsonsCorrelation.newInstance(xMatrix).getCorrelationMatrix();

        Assert.assertArrayEquals("Correlation matrices do not match", correlationMatrixExpected.toArray(), correlationMatrixActual.toArray(), 0.001);
        Assert.assertEquals("Calculated correlation coefficient does not match expected", correlationExpected, correlationMatrixActual.getEntry(0, 0), 0.001);
    }

    /**
     * Test correlation calculation for two arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengths() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {1.1, 2.2, 3.3, 4.4};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation calculation for two arrays with insufficient data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test standard errors calculation for correlation matrix
     */
    @Test
    public void testCorrelationStandardErrors() {
        double[][] xData = {{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}};
        double[][] yData = {{1.1, 1.2, 1.3}, {2.1, 2.2, 2.3}, {3.1, 3.2, 3.3}, {4.1, 4.2, 4.3}};
        double correlationExpected = new SimpleRegression().calculateRegressionCoefficients(xData, yData)[0];

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix xMatrix = new BlockRealMatrix(new Array2DRowRealMatrix(xData));
        RealMatrix yMatrix = new BlockRealMatrix(new Array2DRowRealMatrix(yData));
        RealMatrix correlationMatrixExpected = pearsonsCorrelation.computeCorrelationMatrix(xMatrix);
        RealMatrix correlationStandardErrorsExpected = pearsonsCorrelation.newInstance(xMatrix).getCorrelationStandardErrors();
        RealMatrix correlationStandardErrorsActual = pearsonsCorrelation.newInstance(xMatrix).getCorrelationStandardErrors();

        Assert.assertArrayEquals("Correlation standard errors matrices do not match", correlationStandardErrorsExpected.toArray(), correlationStandardErrorsActual.toArray(), 0.001);
        for (int i = 0; i < xData[0].length; i++) {
            Assert.assertEquals("Standard errors for i-th variable do not match", Math.sqrt((1 - correlationExpected * correlationExpected) / (xData.length - 2)), correlationStandardErrorsActual.getEntry(i, i), 0.001);
        }
    }

    /**
     * Test p-values calculation for correlation matrix
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] xData = {{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}};
        double[][] yData = {{1.1, 1.2, 1.3}, {2.1, 2.2, 2.3}, {3.1, 3.2, 3.3}, {4.1, 4.2, 4.3}};
        double correlationExpected = new SimpleRegression().calculateRegressionCoefficients(xData, yData)[0];

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix xMatrix = new BlockRealMatrix(new Array2DRowRealMatrix(xData));
        RealMatrix yMatrix = new BlockRealMatrix(new Array2DRowRealMatrix(yData));
        RealMatrix correlationMatrixExpected = pearsonsCorrelation.computeCorrelationMatrix(xMatrix);
        RealMatrix correlationPValuesExpected = pearsonsCorrelation.newInstance(xMatrix).getCorrelationPValues();
        RealMatrix correlationPValuesActual = pearsonsCorrelation.newInstance(xMatrix).getCorrelationPValues();

        Assert.assertArrayEquals("Correlation p-values matrices do not match", correlationPValuesExpected.toArray(), correlationPValuesActual.toArray(), 0.001);
        for (int i = 0; i < xData[0].length; i++) {
            for (int j = 0; j < xData[0].length; j++) {
                if (i != j) {
                    double t = Math.abs(correlationExpected * Math.sqrt((xData.length - 2) / (1 - correlationExpected * correlationExpected))) * Math.sqrt((xData.length - 2) / (1 - correlationExpected * correlationExpected));
                    Assert.assertEquals("P-values for i-th and j-th variables do not match", 2 * (1 - new TDistributionImpl(xData.length - 2).cumulativeProbability(t)), correlationPValuesActual.getEntry(i, j), 0.001);
                }
            }
        }
    }
}