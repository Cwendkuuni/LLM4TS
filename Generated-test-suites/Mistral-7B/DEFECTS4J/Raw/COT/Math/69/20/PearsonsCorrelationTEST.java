package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testConstructorWith2x2Data() {
        double[][] data = {{1.0, 1.5}, {2.0, 2.7}, {3.0, 3.5}, {4.0, 4.3}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 0.86602540378444},
                {0.86602540378444, 1.0}
        });

        Assert.assertEquals(expectedCorrelationMatrix, pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(4, pearsonCorrelation.nObs);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 0.86602540378444, 0.76601892218395},
                {0.86602540378444, 1.0, 0.73438555580435},
                {0.76601892218395, 0.73438555580435, 1.0}
        });

        Assert.assertEquals(expectedCorrelationMatrix, pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testConstructorWithCovariance() {
        RealMatrix covarianceMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5},
                {0.5, 1.0}
        });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 4);
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 0.86602540378444},
                {0.86602540378444, 1.0}
        });

        Assert.assertEquals(expectedCorrelationMatrix, pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(4, pearsonCorrelation.nObs);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 1.5}, {2.0, 2.7}, {3.0, 3.5}, {4.0, 4.3}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {0.422580645161297, 0.422580645161297},
                {0.422580645161297, 0.422580645161297}
        });
        RealMatrix actualCorrelationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        Assert.assertEquals(expectedCorrelationStandardErrors, actualCorrelationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 1.5}, {2.0, 2.7}, {3.0, 3.5}, {4.0, 4.3}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {0.436138639958398, 0.436138639958398},
                {0.436138639958398, 0.436138639958398}
        });
        RealMatrix actualCorrelationPValues = pearsonCorrelation.getCorrelationPValues();
        Assert.assertEquals(expectedCorrelationPValues, actualCorrelationPValues);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInvalidDataConstruction() {
        double[][] data = {{1.0, 2.0}, {2.0, 2.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrixWithEmptyDataMatrix() {
        RealMatrix emptyRealMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[0][]);
        new PearsonsCorrelation(emptyRealMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {1.5, 2.7, 3.5, 4.3};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.86602540378444, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataForCorrelation() {
        double[] xArray = {1.0};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 4.0}
        });
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {0.8, 0.64},
                {0.64, 0.8}
        });
        RealMatrix actualCorrelationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        Assert.assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    @Test
    public void testCheckSufficientData() {
        RealMatrix emptyRealMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[0][]);
        try {
            new PearsonsCorrelation().checkSufficientData(emptyRealMatrix);
            Assert.fail();
        } catch (MathRuntimeException e) {
            // expected
        }
        RealMatrix sufficientRealMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        new PearsonsCorrelation().checkSufficientData(sufficientRealMatrix);
    }
}