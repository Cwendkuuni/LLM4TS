package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test cases for PearsonsCorrelation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    /**
     * Test correlation matrix creation from double array
     */
    @Test
    public void testCorrelationMatrixFromDoubleArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertEquals(3, pc.getCorrelationMatrix().getRowDimension());
        assertEquals(3, pc.getCorrelationMatrix().getColumnDimension());

        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(0.75, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.866, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.75, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.866, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.75, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.866, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test correlation matrix creation from RealMatrix
     */
    @Test
    public void testCorrelationMatrixFromRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertEquals(3, pc.getCorrelationMatrix().getRowDimension());
        assertEquals(3, pc.getCorrelationMatrix().getColumnDimension());

        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(0.75, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.866, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.75, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.866, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.75, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.866, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test correlation matrix creation from Covariance
     */
    @Test
    public void testCorrelationMatrixFromCovariance() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 2}, {2, 3}});
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertEquals(2, pc.getCorrelationMatrix().getRowDimension());
        assertEquals(2, pc.getCorrelationMatrix().getColumnDimension());

        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(0.816, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.816, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.447, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test correlation standard errors creation
     */
    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());

        assertEquals(0.457, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.457, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.305, correlationStandardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.457, correlationStandardErrors.getEntry(2, 0), 0.001);
        assertEquals(0.305, correlationStandardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.305, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    /**
     * Test correlation p-values creation
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());

        assertEquals(0.157, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.157, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.277, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.157, correlationPValues.getEntry(2, 0), 0.001);
        assertEquals(0.277, correlationPValues.getEntry(2, 1), 0.001);
        assertEquals(0.277, correlationPValues.getEntry(2, 2), 0.001);
    }

    /**
     * Test correlation computation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationIllegalArgumentException() {
        double[] xArray = {1, 2};
        double[] yArray = {1, 2, 3};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {1, 2, 3};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(1, correlation, 0.001);

        xArray = new double[]{1, 2, 3, 4};
        yArray = new double[]{2, 3, 4, 5};
        correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.05);

        xArray = new double[]{1, 2, 3, 4};
        yArray = new double[]{2, 4, 6, 8};
        correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.05);

        xArray = new double[]{1, 2, 3, 4};
        yArray = new double[]{-2, -4, -6, -8};
        correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(-0.8, correlation, 0.05);
    }

    /**
     * Test correlation matrix creation from covariance matrix
     */
    @Test
    public void testCorrelationMatrixFromCovarianceMatrix() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 2}, {2, 3}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        assertEquals(2, pc.getCorrelationMatrix().getRowDimension());
        assertEquals(2, pc.getCorrelationMatrix().getColumnDimension());

        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(0.816, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.816, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.447, correlationMatrix.getEntry(1, 1), 0.001);
    }

}