package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        Assert.assertNull(pearson.getCorrelationMatrix());
    }

    @Test
    public void testIdentityMatrix() {
        double[][] data = {
                {1.0, 1.0},
                {1.0, 1.0}
        };
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        Assert.assertArrayEquals(expected.getData(), pearson.getCorrelationMatrix().getData(), 0.0);
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8, 0.84, 0.88}, {0.84, 0.88, 0.89}, {0.88, 0.89, 1.0}});
        Assert.assertArrayEquals(expected.getData(), pearson.getCorrelationMatrix().getData(), 0.001);
    }

    @Test
    public void testCovarianceMatrix() {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 3.0},
                {3.0, 4.0},
                {4.0, 5.0}
        };
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8, 0.74}, {0.74, 0.8}});
        Assert.assertArrayEquals(expected.getData(), pearson.getCorrelationMatrix().getData(), 0.001);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {1.1, 2.1, 3.1};
        double expected = 0.993;
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        double result = pearson.correlation(xArray, yArray);
        Assert.assertEquals(expected, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArraySize() {
        double[][] data = {1.0};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataMatrixSize() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}});
        new PearsonsCorrelation(matrix);
    }
}