package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Tests for org.apache.commons.math.stat.correlation.PearsonsCorrelation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}
     */
    @Test
    public void testConstructorWithNoData() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonCorrelation);
        assertEquals(0, pearsonCorrelation.getNObs());
        assertNull(pearsonCorrelation.getCorrelationMatrix());
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}
     */
    @Test
    public void testConstructorWith2DArray() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonCorrelation);
        assertEquals(3, pearsonCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}
     */
    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonCorrelation);
        assertEquals(3, pearsonCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertEquals(0.5, correlationStandardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.365, correlationStandardErrors.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.421, correlationStandardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.365, correlationStandardErrors.getEntry(1, 2), 0.001);
        assertEquals(0.365, correlationStandardErrors.getEntry(2, 0), 0.001);
        assertEquals(0.365, correlationStandardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}
     *
     * @throws MathException
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertEquals(0.101, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(0.05, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.015, correlationPValues.getEntry(0, 2), 0.001);
        assertEquals(0.05, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.005, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.005, correlationPValues.getEntry(1, 2), 0.001);
        assertEquals(0.015, correlationPValues.getEntry(2, 0), 0.001);
        assertEquals(0.01, correlationPValues.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(RealMatrix)}
     */
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation().computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(double[][])}
     */
    @Test
    public void testComputeCorrelationMatrixWith2DArray() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        RealMatrix correlationMatrix = new PearsonsCorrelation().computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}
     */
    @Test
    public void testCorrelationMethod() {
        double[] xArray = { 1.0, 2.0, 3.0 };
        double[] yArray = { 2.0, 3.0, 4.0 };
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.5, correlation, 0.001);
    }

    /**
     * Test method for
     * {@link PearsonsCorrelation#covarianceToCorrelation(RealMatrix)}
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        RealMatrix covarianceMatrix = new SimpleRegression().covariance(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.366, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 0.001);
    }
}