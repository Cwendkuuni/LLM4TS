package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for PearsonsCorrelation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test construction from double[][] data
     */
    @Test
    public void testConstructionFrom2DArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D[] {new Array2DRowRealMatrix(new double[][] {{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}}), new Array2DRowRealMatrix(new double[][] {{0.8, 0.6, 0.6}, {0.6, 0.8, 0.6}, {0.6, 0.6, 1.0}})});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.001);
    }

    /**
     * Test construction from RealMatrix
     */
    @Test
    public void testConstructionFromRealMatrix() throws MathException {
        RealMatrix data = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D[] {new Array2DRowRealMatrix(new double[][] {{1.0, 0.8}, {0.8, 1.0}}), new Array2DRowRealMatrix(new double[][] {{0.8, 0.8}, {0.8, 1.0}})});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.001);
    }

    /**
     * Test construction from Covariance
     */
    @Test
    public void testConstructionFromCovariance() throws MathException {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {{3.61, 2.31}, {2.31, 4.93}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, covarianceMatrix.getRowDimension());
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D[] {new Array2DRowRealMatrix(new double[][] {{1.0, 0.76}, {0.76, 1.0}}), new Array2DRowRealMatrix(new double[][] {{0.76, 0.76}, {0.76, 1.0}})});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), actualCorrelationMatrix.getData(), 0.001);
    }

    /**
     * Test getCorrelationStandardErrors() method
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        RealMatrix data = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][] {{0.632, 0.512}, {0.512, 0.632}});
        RealMatrix actualCorrelationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertArrayEquals(expectedCorrelationStandardErrors.getData(), actualCorrelationStandardErrors.getData(), 0.001);
    }

    /**
     * Test getCorrelationPValues() method
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix data = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][] {{0.466, 0.043}, {0.043, 0.466}});
        RealMatrix actualCorrelationPValues = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertArrayEquals(expectedCorrelationPValues.getData(), actualCorrelationPValues.getData(), 0.001);
    }

    /**
     * Test correlation() method with double[][] data
     */
    @Test
    public void testCorrelationWith2DArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        double correlation = PearsonsCorrelation.correlation(data[0], data[1]);
        double expectedCorrelation = 0.8;
        Assert.assertEquals(expectedCorrelation, correlation, 0.001);
    }

    /**
     * Test correlation() method with RealMatrix
     */
    @Test
    public void testCorrelationWithRealMatrix() {
        RealMatrix data = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        double correlation = PearsonsCorrelation.correlation(data.getColumn(0), data.getColumn(1));
        double expectedCorrelation = 0.8;
        Assert.assertEquals(expectedCorrelation, correlation, 0.001);
    }

    /**
     * Test checkSufficientData() method
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData() {
        RealMatrix data = new Array2DRowRealMatrix(new double[][] {{1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.checkSufficientData(data);
    }
}