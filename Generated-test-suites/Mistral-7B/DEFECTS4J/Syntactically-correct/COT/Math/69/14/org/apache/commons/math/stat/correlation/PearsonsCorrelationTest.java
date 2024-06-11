package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorWithoutData() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testConstructorWith2x2DoubleArray() {
        double[][] data = {{1.0d, 2.0d}, {3.0d, 4.0d}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1d, correlationMatrix.getEntry(0, 0), 0.001d);
        assertEquals(0.5d, correlationMatrix.getEntry(0, 1), 0.001d);
        assertEquals(0.5d, correlationMatrix.getEntry(1, 0), 0.001d);
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001d);
        assertEquals(2, pearsonCorrelation.nObs);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0d, 2.0d}, {3.0d, 4.0d}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1d, correlationMatrix.getEntry(0, 0), 0.001d);
        assertEquals(0.5d, correlationMatrix.getEntry(0, 1), 0.001d);
        assertEquals(0.5d, correlationMatrix.getEntry(1, 0), 0.001d);
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001d);
        assertEquals(2, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInsufficientDataRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0d, 2.0d}});
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInsufficientDataDoubleArray() {
        double[][] data = {{1.0d, 2.0d}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0d, 2.0d}, {3.0d, 4.0d}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1d, correlationMatrix.getEntry(0, 0), 0.001d);
        assertEquals(0.5d, correlationMatrix.getEntry(0, 1), 0.001d);
        assertEquals(0.5d, correlationMatrix.getEntry(1, 0), 0.001d);
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001d);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0d, 2.0d}, {3.0d, 4.0d}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        assertEquals(2, correlationStandardErrors.getRowDimension());
        assertEquals(2, correlationStandardErrors.getColumnDimension());
        assertEquals(0.482840755424138, correlationStandardErrors.getEntry(0, 0), 0.001d);
        assertEquals(0.482840755424138, correlationStandardErrors.getEntry(0, 1), 0.001d);
        assertEquals(0.482840755424138, correlationStandardErrors.getEntry(1, 0), 0.001d);
        assertEquals(0.333333333333333, correlationStandardErrors.getEntry(1, 1), 0.001d);
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValuesThrowsMathException() throws MathException {
        double[][] data = {{1.0d, 2.0d}, {3.0d, 4.0d}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        // MathException should be thrown due to hardcoded TDistribution with nObs-2=1 instead of nObs-2=2
        assertEquals(1, correlationPValues.getRowDimension());
        assertEquals(1, correlationPValues.getColumnDimension());
        assertEquals(1.0, correlationPValues.getEntry(0, 0), 0.001d);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0d, 2.0d}, {3.0d, 4.0d}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        assertEquals(2, correlationPValues.getRowDimension());
        assertEquals(2, correlationPValues.getColumnDimension());
        assertEquals(0.101489274442363, correlationPValues.getEntry(0, 0), 0.001d);
        assertEquals(0.050742136988182, correlationPValues.getEntry(0, 1), 0.001d);
        assertEquals(0.050742136988182, correlationPValues.getEntry(1, 0), 0.001d);
        assertEquals(0.014948323235386, correlationPValues.getEntry(1, 1), 0.001d);
    }

    @Test(expected = MathException.class)
    public void testCovarianceToCorrelationThrowsMathException() throws MathException {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0d, 2.0d}, {2.0d, 3.0d}});
        new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0d, 2.0d}, {2.0d, 3.0d}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(0.965925834559845, correlationMatrix.getEntry(0, 0), 0.001d);
        assertEquals(0.741325922740401, correlationMatrix.getEntry(0, 1), 0.001d);
        assertEquals(0.741325922740401, correlationMatrix.getEntry(1, 0), 0.001d);
        assertEquals(0.965925834559845, correlationMatrix.getEntry(1, 1), 0.001d);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationThrowsIllegalArgumentException() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationThrowsIllegalArgumentExceptionInvalidDataLength() {
        double[] xArray = {1.0};
        double[] yArray = {1.0, 2.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}