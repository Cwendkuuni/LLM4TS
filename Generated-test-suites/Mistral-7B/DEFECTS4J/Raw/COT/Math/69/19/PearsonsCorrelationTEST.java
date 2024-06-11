package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * JUnit 4 test cases for PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelationEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.getnObs());
    }

    @Test
    public void testPearsonsCorrelationDoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0, 1.0}, {2.0, 1.0, 2.0}, {1.0, 2.0, 1.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        double[][] expectedCorrelationMatrix = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        assertArraysEqual(expectedCorrelationMatrix, correlationMatrix.getData());
        assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test
    public void testPearsonsCorrelationRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        double[][] expectedCorrelationMatrix = {{1.0, 0.5}, {0.5, 1.0}};
        assertArraysEqual(expectedCorrelationMatrix, correlationMatrix.getData());
        assertEquals(2, pearsonCorrelation.getnObs());
    }

    @Test
    public void testPearsonsCorrelationCovarianceConstructor() {
        Covariance covariance = new Covariance(new Array2DRowRealMatrix(new double[][]{{1.0, 1.5}, {1.5, 3.0}}));
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        double[][] expectedCorrelationMatrix = {{1.0, 0.6}, {0.6, 1.0}};
        assertArraysEqual(expectedCorrelationMatrix, correlationMatrix.getData());
        assertEquals(2, pearsonCorrelation.getnObs());
    }

    @Test
    public void testPearsonsCorrelationRealMatrixConstructorWithNumberOfObservations() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.5}, {1.5, 3.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        double[][] expectedCorrelationMatrix = {{1.0, 0.6}, {0.6, 1.0}};
        assertArraysEqual(expectedCorrelationMatrix, correlationMatrix.getData());
        assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 1.0}, {2.0, 1.0, 2.0}, {1.0, 2.0, 1.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        double[][] expectedCorrelationMatrix = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        assertArraysEqual(expectedCorrelationMatrix, correlationMatrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 1.0}, {2.0, 1.0, 2.0}, {1.0, 2.0, 1.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        double[][] expectedCorrelationStandardErrors = {{-0.332, 0.332, 0.332}, {0.332, -0.332, 0.332}, {0.332, 0.332, -0.332}};
        assertArraysEqual(expectedCorrelationStandardErrors, correlationStandardErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 1.0}, {2.0, 1.0, 2.0}, {1.0, 2.0, 1.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        double[][] expectedCorrelationPValues = {{0.0, 0.976, 0.976}, {0.976, 0.0, 0.976}, {0.976, 0.976, 0.0}};
        assertArraysEqual(expectedCorrelationPValues, correlationPValues.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationNegativeData() {
        double[] xArray = {-1.0, 2.0, -1.0};
        double[] yArray = {2.0, -1.0, 2.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationArraysNotSameLength() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {1.0, 2.0, 3.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {1.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.5}, {1.5, 3.0}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        double[][] expectedCorrelationMatrix = {{1.0, 0.6}, {0.6, 1.0}};
        assertArraysEqual(expectedCorrelationMatrix, correlationMatrix.getData());
    }

    private static void assertArraysEqual(Object actual, Object expected) {
        assertArrayEquals(actual.getClass().getComponentType(), expected.getClass().getComponentType(), actual, expected);
    }
}