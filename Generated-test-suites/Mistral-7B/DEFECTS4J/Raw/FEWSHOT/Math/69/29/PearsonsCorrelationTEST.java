package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    @Test(expected = IllegalArgumentException.class)
    public void testDataNull() {
        new PearsonsCorrelation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataEmpty() {
        double[][] data = new double[0][];
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataNonSquare() {
        double[][] data = { { 1, 2, 3 }, { 4, 5 } };
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataSingleColumn() {
        double[][] data = { { 1 } };
        new PearsonsCorrelation(data);
    }

    @Test
    public void testDataOneVariable() {
        double[][] data = { { 1 }, { 2 }, { 3 }, { 4 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(4, correlationMatrix.getRowDimension());
        assertEquals(1, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1.0 } }, correlationMatrix.getData());
        assertEquals(4, pc.nObs);
    }

    @Test
    public void testDataTwoVariables() {
        double[][] data = { { 1, 2 }, { 3, 2 }, { 3, 4 }, { 5, 4 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1.0, 0.8 }, { 0.8, 1.0 } }, correlationMatrix.getData());
        assertEquals(4, pc.nObs);
    }

    @Test
    public void testDataThreeVariables() {
        double[][] data = { { 1, 2, 3 }, { 3, 2, 4 }, { 3, 4, 5 }, { 5, 4, 6 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1.0, 0.7, 0.6 }, { 0.7, 1.0, 0.8 }, { 0.6, 0.8, 1.0 } }, correlationMatrix.getData());
        assertEquals(4, pc.nObs);
    }

    @Test
    public void testDataDifferentOrders() {
        double[][] data = { { 1, 3 }, { 2, 4 }, { 3, 5 }, { 4, 6 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 0.6, 0.8 }, { 0.8, 1.0 } }, correlationMatrix.getData());
        assertEquals(4, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceNull() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testCreateFromCovariance() {
        double[][] data = { { 1, 2 }, { 3, 2 }, { 3, 4 }, { 5, 4 } };
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.5 },
            { 1.5, 2.0 }
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1.0, 0.8 }, { 0.8, 1.0 } }, correlationMatrix.getData());
        assertEquals(4, pc.nObs);
    }

    @Test
    public void testCreateFromCovarianceMatrix() {
        double[][] data = { { 1, 2 }, { 3, 2 }, { 3, 4 }, { 5, 4 } };
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {
            { 2.0, 1.5 },
            { 1.5, 2.0 }
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 4);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][] { { 1.0, 0.8 }, { 0.8, 1.0 } }, correlationMatrix.getData());
        assertEquals(4, pc.nObs);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = { { 1, 2 }, { 3, 2 }, { 3, 4 }, { 5, 4 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix standardErrors = pc.getCorrelationStandardErrors();
        assertNotNull(standardErrors);
        assertEquals(2, standardErrors.getRowDimension());
        assertEquals(2, standardErrors.getColumnDimension());
        assertArrayEquals(new double[][] { { 0.4828406, 0.4472136 }, { 0.4472136, 0.4828406 } }, standardErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = { { 1, 2 }, { 3, 2 }, { 3, 4 }, { 5, 4 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix pValues = pc.getCorrelationPValues();
        assertNotNull(pValues);
        assertEquals(2, pValues.getRowDimension());
        assertEquals(2, pValues.getColumnDimension());
        assertArrayEquals(new double[][] { { 0.163845648173214, 0.163845648173214 }, { 0.163845648173214, 0.163845648173214 } }, pValues.getData());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = { { 1, 2, 3 }, { 3, 2, 4 }, { 3, 4, 5 }, { 5, 4, 6 } };
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][] {
            { 0.8, 0.6, 0.5 },
            { 0.6, 1.0, 0.8 },
            { 0.5, 0.8, 1.0 }
        });
        RealMatrix computedCorrelation = new PearsonsCorrelation(data).computeCorrelationMatrix();
        assertArrayEquals(correlationMatrix.getData(), computedCorrelation.getData());
    }

    @Test
    public void testComputeCorrelationMatrixFromData() {
        double[][] data = { { 1, 2, 3 }, { 3, 2, 4 }, { 3, 4, 5 }, { 5, 4, 6 } };
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][] {
            { 0.8, 0.6, 0.5 },
            { 0.6, 1.0, 0.8 },
            { 0.5, 0.8, 1.0 }
        });
        RealMatrix computedCorrelation = new PearsonsCorrelation().computeCorrelationMatrix(data);
        assertArrayEquals(correlationMatrix.getData(), computedCorrelation.getData());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = { 1, 2, 3, 4 };
        double[] yArray = { 2, 3, 3, 4 };
        assertEquals(0.8, new PearsonsCorrelation().correlation(xArray, yArray), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrays() {
        double[] xArray = { 1 };
        double[] yArray = { 1, 2 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}