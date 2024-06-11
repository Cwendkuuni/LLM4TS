package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrixFromArrays() {
        double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yData = {2.0, 3.0, 4.0, 4.5, 5.5};
        PearsonsCorrelation correlation = new PearsonsCorrelation(xData, yData);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        Assert.assertEquals("Correlation matrix is not as expected.", expectedMatrix, correlation.getCorrelationMatrix());
    }

    @Test
    public void testCorrelationMatrixFromCovariance() {
        double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yData = {2.0, 3.0, 4.0, 4.5, 5.5};
        Covariance covariance = new Covariance(xData, yData);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        Assert.assertEquals("Correlation matrix is not as expected.", expectedMatrix, correlation.getCorrelationMatrix());
    }

    @Test
    public void testCorrelationMatrixFromRealMatrix() {
        double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yData = {2.0, 3.0, 4.0, 4.5, 5.5};
        RealMatrix dataMatrix = new Array2DRowRealMatrix(new double[][]{{xData}, {yData}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(dataMatrix);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        Assert.assertEquals("Correlation matrix is not as expected.", expectedMatrix, correlation.getCorrelationMatrix());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeData() {
        double[] xData = {-1.0, 2.0, -3.0, 4.0, 5.0};
        double[] yData = {2.0, 3.0, 4.0, 4.5, 5.5};
        new PearsonsCorrelation(xData, yData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareData() {
        double[] xData = {1.0, 2.0};
        double[] yData = {2.0, 3.0, 4.0};
        new PearsonsCorrelation(xData, yData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData() {
        new PearsonsCorrelation();
    }

    @Test
    public void testCorrelationFromRegression() {
        double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yData = {2.0, 3.0, 4.0, 4.5, 5.5};
        SimpleRegression regression = new SimpleRegression();
        for (double x : xData) {
            for (double y : yData) {
                regression.addData(x, y);
            }
        }
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xData, yData);
        Assert.assertEquals("Correlation is not as expected.", regression.getR(), correlation.correlation(xData, yData), 0.001);
    }
}