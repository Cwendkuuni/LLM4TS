package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test case for the default constructor.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        Assert.assertNull(pc.getCorrelationMatrix());
        Assert.assertEquals(0, pc.nObs);
    }

    /**
     * Test case for the constructor with a 2x2 double array.
     */
    @Test
    public void test2x2DoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);

        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });

        RealMatrix actualCorrMatrix = pc.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrMatrix.getData(), actualCorrMatrix.getData(), 0.001);
        Assert.assertEquals(4, pc.nObs);
    }

    /**
     * Test case for the constructor with a 3x3 RealMatrix.
     */
    @Test
    public void test3x3RealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5555555555555556, 0.4444444444444444},
                {0.5555555555555556, 1.0, 0.6666666666666667},
                {0.4444444444444444, 0.6666666666666667, 1.0}
        });

        RealMatrix actualCorrMatrix = pc.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrMatrix.getData(), actualCorrMatrix.getData(), 0.001);
        Assert.assertEquals(9, pc.nObs);
    }

    /**
     * Test case for the constructor with a Covariance instance.
     */
    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);

        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });

        RealMatrix actualCorrMatrix = pc.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrMatrix.getData(), actualCorrMatrix.getData(), 0.001);
        Assert.assertEquals(4, pc.nObs);
    }

    /**
     * Test case for the constructor with a RealMatrix and number of observations.
     */
    @Test
    public void testRealMatrixAndNumberOfObservationsConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5},
                {0.5, 1.0}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 4);

        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });

        RealMatrix actualCorrMatrix = pc.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrMatrix.getData(), actualCorrMatrix.getData(), 0.001);
        Assert.assertEquals(4, pc.nObs);
    }

    /**
     * Test case for the getCorrelationStandardErrors() method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);

        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });

        RealMatrix actualCorrMatrix = pc.getCorrelationMatrix();
        RealMatrix actualCorrStandardErrors = pc.getCorrelationStandardErrors();

        Assert.assertArrayEquals(expectedCorrMatrix.getData(), actualCorrMatrix.getData(), 0.001);
        Assert.assertEquals(4, pc.nObs);

        RealMatrix expectedCorrStandardErrors = new Array2DRowRealMatrix(new double[][]{
                {0.4472136, 0.4472136},
                {0.4472136, 0.4472136}
        });

        Assert.assertArrayEquals(expectedCorrStandardErrors.getData(), actualCorrStandardErrors.getData(), 0.001);
    }

    /**
     * Test case for the getCorrelationPValues() method.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);

        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8},
                {0.8, 1.0}
        });

        RealMatrix actualCorrMatrix = pc.getCorrelationMatrix();
        RealMatrix actualCorrPValues = pc.getCorrelationPValues();

        Assert.assertArrayEquals(expectedCorrMatrix.getData(), actualCorrMatrix.getData(), 0.001);
        Assert.assertEquals(4, pc.nObs);

        double[][] expectedCorrPValuesData = new double[][]{
                {0.3965174, 0.3965174},
                {0.3965174, 0.3965174}
        };

        Assert.assertArrayEquals(expectedCorrPValuesData, actualCorrPValues.getData(), 0.001);
    }

    /**
     * Test case for the correlation method with two 1x1 RealVectors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWith1x1RealVectors() {
        RealVector xVector = new ArrayRealVector(new double[]{1.0});
        RealVector yVector = new ArrayRealVector(new double[]{1.0});
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xVector.toArray(), yVector.toArray());
    }

    /**
     * Test case for the correlation method with two arrays of different lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithArraysOfDifferentLengths() {
        double[] xArray = new double[]{1.0, 2.0, 3.0};
        double[] yArray = new double[]{1.0, 2.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    /**
     * Test case for the correlation method with two arrays of insufficient length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = new double[]{1.0};
        double[] yArray = new double[]{1.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(xArray, yArray);
    }

    /**
     * Test case for the correlation method with two arrays of sufficient length.
     */
    @Test
    public void testCorrelationWithSufficientData() throws IllegalArgumentException {
        double[] xArray = new double[]{1.0, 2.0, 3.0};
        double[] yArray = new double[]{1.1, 2.1, 3.1};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }
        double expectedCorrelation = regression.getR();
        Assert.assertEquals(expectedCorrelation, correlation, 0.001);
    }
}