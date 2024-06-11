package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.nObs);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullInputData() {
        double[][] data = null;
        new PearsonsCorrelation(data);
    }

    @Test
    public void testDataWithOneRow() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
        assertEquals(3, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(3, correlation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(0, 2), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(1, 0), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(1, 1), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(1, 2), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(2, 0), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(2, 1), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(2, 2), 0.001);
    }

    @Test
    public void testDataWithTwoColumns() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
        assertEquals(2, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(2, correlation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0.5, correlation.getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals(0.5, correlation.getCorrelationMatrix().getEntry(1, 0), 0.001);
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(1, 1), 0.001);
    }

    @Test
    public void testDataWithTwoColumnsAndOneRow() {
        double[][] data = new double[][]{{1.0, 2.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
        assertEquals(1, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(2, correlation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(1, 0), 0.001);
        assertEquals(0.0, correlation.getCorrelationMatrix().getEntry(1, 1), 0.001);
    }

    @Test
    public void testDataWithTwoColumnsAndThreeRows() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
        assertEquals(3, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(2, correlation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0.5, correlation.getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals(0.5, correlation.getCorrelationMatrix().getEntry(1, 0), 0.001);
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(1, 1), 0.001);
        assertEquals(0.4472, correlation.getCorrelationMatrix().getEntry(1, 2), 0.001);
        assertEquals(0.4472, correlation.getCorrelationMatrix().getEntry(2, 1), 0.001);
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(2, 2), 0.001);
    }

    @Test
    public void testDataWithTwoColumnsAndThreeRowsSymmetric() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
        assertEquals(3, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(2, correlation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0.5, correlation.getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals(0.5, correlation.getCorrelationMatrix().getEntry(1, 0), 0.001);
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(1, 1), 0.001);
        assertEquals(0.4472, correlation.getCorrelationMatrix().getEntry(1, 2), 0.001);
        assertEquals(0.4472, correlation.getCorrelationMatrix().getEntry(2, 1), 0.001);
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(2, 2), 0.001);
    }

    @Test
    public void testCovariance() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, data.length);
        assertNotNull(correlation.getCorrelationMatrix());
        assertEquals(3, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(3, correlation.getCorrelationMatrix().getColumnDimension());
        assertEquals(1.0, correlation.getCorrelationMatrix().getEntry(0, 0), 0.001);
        assertEquals(0.6428, correlation.getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals(0.6428, correlation.getCorrelationMatrix().getEntry(1, 0), 0.001);
        assertEquals(0.6, correlation.getCorrelationMatrix().getEntry(1, 1), 0.001);
        assertEquals(0.5714, correlation.getCorrelationMatrix().getEntry(1, 2), 0.001);
        assertEquals(0.5714, correlation.getCorrelationMatrix().getEntry(2, 1), 0.001);
        assertEquals(0.8, correlation.getCorrelationMatrix().getEntry(2, 2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataWithDifferentLengths() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.0, 2.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataWithInsufficientData() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = new double[][]{{1.0, 1.5, 2.5}, {2.0, 2.0, 3.0}, {3.0, 3.5, 4.5}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, standardErrors.getRowDimension());
        assertEquals(3, standardErrors.getColumnDimension());
        assertEquals(0.6666666666666666, standardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.5, standardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.5, standardErrors.getEntry(0, 2), 0.001);
        assertEquals(0.5, standardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.3333333333333333, standardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.3333333333333333, standardErrors.getEntry(1, 2), 0.001);
        assertEquals(0.3333333333333333, standardErrors.getEntry(2, 0), 0.001);
        assertEquals(0.3333333333333333, standardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.3333333333333333, standardErrors.getEntry(2, 2), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 1.5, 2.5}, {2.0, 2.0, 3.0}, {3.0, 3.5, 4.5}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix pValues = correlation.getCorrelationPValues();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, pValues.getRowDimension());
        assertEquals(3, pValues.getColumnDimension());
        assertEquals(0.5, pValues.getEntry(0, 0), 0.001);
        assertEquals(0.5, pValues.getEntry(0, 1), 0.001);
        assertEquals(0.5, pValues.getEntry(0, 2), 0.001);
        assertEquals(0.5, pValues.getEntry(1, 0), 0.001);
        assertEquals(0.1282129331232239, pValues.getEntry(1, 1), 0.001);
        assertEquals(0.1282129331232239, pValues.getEntry(1, 2), 0.001);
        assertEquals(0.1282129331232239, pValues.getEntry(2, 0), 0.001);
        assertEquals(0.1282129331232239, pValues.getEntry(2, 1), 0.001);
        assertEquals(0.1282129331232239, pValues.getEntry(2, 2), 0.001);
    }
}