package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for PearsonsCorrelation
 */
public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation);
        assertEquals(0, pearsonsCorrelation.nObs);
        assertNull(pearsonsCorrelation.correlationMatrix);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}.
     */
    @Test
    public void testConstructorWith2DArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 3.2}, {3.1, 3.2, 3.3}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation);
        assertEquals(3, pearsonsCorrelation.nObs);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 3.2}, {3.1, 3.2, 3.3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonsCorrelation);
        assertEquals(3, pearsonsCorrelation.nObs);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 3.2}, {3.1, 3.2, 3.3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 3.2}, {3.1, 3.2, 3.3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertEquals(0.5646, correlationStandardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.5288, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.5288, correlationStandardErrors.getEntry(0, 2), 0.001);
        assertEquals(0.5288, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.5288, correlationStandardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.4321, correlationStandardErrors.getEntry(1, 2), 0.001);
        assertEquals(0.4321, correlationStandardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.4321, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}.
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 3.2}, {3.1, 3.2, 3.3}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertEquals(1.0, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(1.0, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(1.0, correlationPValues.getEntry(0, 2), 0.001);
        assertEquals(0.5033, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.5033, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(1, 2), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 2), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}.
     */
    @Test
    public void testCorrelationWith2DArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.1, 3.1, 3.2};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.7416, correlation, 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#covarianceToCorrelation(RealMatrix)}.
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 3.2}, {3.1, 3.2, 3.3}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data).transpose().multiply(new BlockRealMatrix(data));
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.7416, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.6667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 2), 0.001);
    }

}