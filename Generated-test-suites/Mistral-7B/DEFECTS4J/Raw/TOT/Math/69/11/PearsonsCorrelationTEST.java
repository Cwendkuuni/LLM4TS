package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrixFromDataArray() {
        double[][] data = {{1.0, 2.0, 2.0}, {3.0, 4.0, 4.0}, {5.0, 6.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.8},
                {0.8, 1.0, 0.8},
                {0.8, 0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelationMatrixFromRectangularArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelationMatrixFromMatrix() throws MathException {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.8},
                {0.8, 1.0, 0.8},
                {0.8, 0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientData() {
        double[][] data = {{1.0, 2.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        new PearsonsCorrelation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareData() {
        double[][] data = {{1.0}, {2.0, 3.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testDivisionByZero() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, -1.0}
        });
        new PearsonsCorrelation(covarianceMatrix, 3);
    }
}