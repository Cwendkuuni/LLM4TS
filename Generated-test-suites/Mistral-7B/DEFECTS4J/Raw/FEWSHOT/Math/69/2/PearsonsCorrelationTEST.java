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
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test
    public void testDataArrayConstructor() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        int nObs = pc.nObs;

        assertEquals(3, nObs);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { {1.0, 0.643, 0.577}, {0.643, 1.0, 0.665}, {0.577, 0.665, 1.0} }, correlationMatrix.getData());
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0} });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        assertEquals(3, pc.nObs);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { {1.0, 0.643, 0.577}, {0.643, 1.0, 0.665}, {0.577, 0.665, 1.0} }, correlationMatrix.getData());
    }

    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance(new double[][] { {1.0, 1.5}, {1.5, 2.5} });
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        assertEquals(2, pc.nObs);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { {1.0, 0.8}, {0.8, 1.0} }, correlationMatrix.getData());
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { {1.0, 1.5}, {1.5, 2.5} });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        assertEquals(2, pc.nObs);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { {1.0, 0.8}, {0.8, 1.0} }, correlationMatrix.getData());
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double correlation = correlationMatrix.getEntry(i, j);
                double stdError = correlationStandardErrors.getEntry(i, j);
                double nObs = pc.nObs;

                assertTrue(stdError > 0);
                assertEquals(Math.sqrt((1 - Math.pow(correlation, 2)) / (nObs - 2)), stdError, 0.01);
            }
        }
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double correlation = correlationMatrix.getEntry(i, j);
                double pValue = correlationPValues.getEntry(i, j);
                double tDistribution = new TDistributionImpl(pc.nObs - 2);

                assertTrue(pValue > 0);
                assertEquals(1.0 - 2 * tDistribution.cumulativeProbability(Math.abs(correlation * Math.sqrt((pc.nObs - 2) / (1 - Math.pow(correlation, 2))))), pValue, 0.01);
            }
        }
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(data);

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { {1.0, 0.643, 0.577}, {0.643, 1.0, 0.665}, {0.577, 0.665, 1.0} }, correlationMatrix.getData());
    }

    @Test
    public void testComputeCorrelationMatrixWithRealMatrix() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(matrix);

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { {1.0, 0.643, 0.577}, {0.643, 1.0, 0.665}, {0.577, 0.665, 1.0} }, correlationMatrix.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionCorrelation() {
        double[] xArray = { 1.0 };
        double[] yArray = { 2.0 };
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionCorrelationMatrix() {
        double[][] data = { { 1.0 } };
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.computeCorrelationMatrix(data);
    }
}