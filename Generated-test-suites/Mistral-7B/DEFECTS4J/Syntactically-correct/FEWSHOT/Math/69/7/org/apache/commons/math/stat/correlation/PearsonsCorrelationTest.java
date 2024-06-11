package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmpty() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testRectangularArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        double[] entries = {0.741, 0.813, 0.813, 0.741, 1.0, 1.0, 1.0, 1.0, 1.0};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(entries[2 * i + j], correlationMatrix.getEntry(i, j), 0.001);
            }
        }
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        double[] entries = {0.8, 0.5555555555555556, 0.5555555555555556, 0.8};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(entries[2 * i + j], correlationMatrix.getEntry(i, j), 0.001);
            }
        }
        assertEquals(2, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonRectangularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 0.5}, {0.5, 2.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        double[] entries = {0.8, 0.5555555555555556, 0.5555555555555556, 0.8};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(entries[2 * i + j], correlationMatrix.getEntry(i, j), 0.001);
            }
        }
        assertEquals(3, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCovariance() {
        new PearsonsCorrelation(null, 3);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        double[][] expected = {{Math.sqrt(0.26), 0.0, 0.0, Math.sqrt(0.26), 0.0, 0.0},
                {0.0, Math.sqrt(0.26), 0.0, 0.0, Math.sqrt(0.26), 0.0},
                {0.0, 0.0, Math.sqrt(0.26), 0.0, 0.0, Math.sqrt(0.26)}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected[i][j], correlationStandardErrors.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        double[][] expected = {{0.045, 0.955, 0.955, 0.045, 1.0, 1.0, 1.0, 1.0, 1.0},
                {0.955, 0.045, 0.955, 0.955, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 0.045, 0.955, 1.0, 1.0, 1.0, 1.0, 1.0}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(correlationPValues.getEntry(i, j) >= 0);
                assertEquals(expected[i][j], correlationPValues.getEntry(i, j), 0.001);
            }
        }
    }
}