package org.apache.commons.cli.test;

import static org.apache.commons.math3.util.Precision.isClose;
import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.Covariance;

import java.util.Random;

public class PearsonsCorrelationTest {

    /**
     * Test for constructor without data
     */
    @Test
    public void testEmptyPearsonsCorrelation() {
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation();
        assertNull(pearsonCorr.getCorrelationMatrix());
        assertEquals(0, pearsonCorr.getNObs());
    }

    /**
     * Test constructor from a 2x2 double array
     */
    @Test
    public void testPearsonsCorrelationFrom2x2Matrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(data);
        assertEquals(2, pearsonCorr.getNObs());
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test constructor from a 3x3 RealMatrix
     */
    @Test
    public void testPearsonsCorrelationFrom3x3Matrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(matrix);
        assertEquals(3, pearsonCorr.getNObs());
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        assertEquals(0.741, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.741, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.811, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.433, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.811, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(0.498, correlationMatrix.getEntry(2, 1), 0.001);
    }

    /**
     * Test constructor from a Covariance
     */
    @Test
    public void testPearsonsCorrelationFromCovariance() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(covariance);
        assertEquals(2, pearsonCorr.getNObs());
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test constructor from a RealMatrix and number of observations
     */
    @Test
    public void testPearsonsCorrelationFromRealMatrixAndNumberOfObservations() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(matrix, 2);
        assertEquals(2, pearsonCorr.getNObs());
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test getCorrelationStandardErrors() method
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorr.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertEquals(0.672, correlationStandardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.672, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.723, correlationStandardErrors.getEntry(2, 0), 0.001);
        assertEquals(0.672, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.672, correlationStandardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.723, correlationStandardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.672, correlationStandardErrors.getEntry(0, 2), 0.001);
        assertEquals(0.672, correlationStandardErrors.getEntry(1, 2), 0.001);
        assertEquals(0.723, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    /**
     * Test getCorrelationPValues() method
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorr = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorr.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorr.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertEquals(0.5, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 0), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(1, 2), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 2), 0.001);
    }

    /**
     * Test correlation method for double arrays
     */
    @Test
    public void testCorrelationForDoubleArrays() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0, 6.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    /**
     * Test correlation method for empty double arrays
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationForEmptyDoubleArrays() {
        double[] xArray = {};
        double[] yArray = {};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method for double arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationForDoubleArraysWithDifferentLengths() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method for double arrays with insufficient data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationForDoubleArraysWithInsufficientData() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test covarianceToCorrelation method
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(0.741, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.741, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.811, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.433, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.811, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(0.498, correlationMatrix.getEntry(2, 1), 0.001);
    }
}