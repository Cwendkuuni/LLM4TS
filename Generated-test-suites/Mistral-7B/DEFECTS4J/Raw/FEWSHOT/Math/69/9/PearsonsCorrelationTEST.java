package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealArrayRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData2DArrayConstructor() {
        double[][] data = {};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testData2DArrayConstructor() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1, 0.8, 0.6 }, { 0.8, 1, 0.8 }, { 0.6, 0.8, 1 } }, correlationMatrix.getData());
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1, 0.8, 0.6 }, { 0.8, 1, 0.8 }, { 0.6, 0.8, 1 } }, correlationMatrix.getData());
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        RealMatrix covarianceMatrix = new Covariance(data).getCovarianceMatrix();
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1, 0.8, 0.6 }, { 0.8, 1, 0.8 }, { 0.6, 0.8, 1 } }, correlationMatrix.getData());
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertArrayEquals(new double[][] { { 0.7746, 0.6124, 0.4899 }, { 0.6124, 0.7746, 0.7344 }, { 0.4899, 0.7344, 0.7746 } }, correlationStandardErrors.getData());
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertArrayEquals(new double[][] { { 0.0119, 0.9881, 0.9968 }, { 0.9881, 0.0119, 0.0039 }, { 0.9968, 0.0039, 0.0119 } }, correlationPValues.getData());
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValuesWithNullMatrix() throws MathException {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.getCorrelationPValues();
    }
}