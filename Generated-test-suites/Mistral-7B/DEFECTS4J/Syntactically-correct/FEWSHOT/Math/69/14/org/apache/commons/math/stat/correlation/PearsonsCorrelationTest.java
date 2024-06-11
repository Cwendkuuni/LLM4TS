package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.rank.RankSum;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearlson.correlationMatrix);
        assertEquals(0, pearson.nObs);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        assertNotNull(pearlson.correlationMatrix);
        assertEquals(3, pearson.nObs);
    }

    @Test
    public void testDataConstructorFail() {
        double[][] data = new double[][]{{1.0, 2.0}, {4.0}};
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(data));
    }

    @Test
    public void testMatrixConstructor() {
        RealMatrix matrix = mock(RealMatrix.class);
        when(matrix.getColumnDimension()).thenReturn(3);
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        assertNotNull(pearlson.correlationMatrix);
        assertEquals(3, pearson.nObs);
    }

    @Test
    public void testMatrixConstructorFail() {
        RealMatrix matrix = mock(RealMatrix.class);
        when(matrix.getColumnDimension()).thenReturn(1);
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(matrix));
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = mock(RealMatrix.class);
        when(covarianceMatrix.getEntry(0, 0)).thenReturn(1.0);
        when(covarianceMatrix.getEntry(1, 1)).thenReturn(1.0);
        when(covarianceMatrix.getEntry(0, 1)).thenReturn(0.5);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        assertArrayEquals(expected.getData(), pearson.correlationMatrix.getData());
    }

    @Test
    public void testCovarianceConstructorFail() {
        RealMatrix covarianceMatrix = mock(RealMatrix.class);
        when(covarianceMatrix.getColumnDimension()).thenReturn(1);
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(covarianceMatrix, 3));
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        assertArrayEquals(expected.getData(), correlationMatrix.getData());
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix standardErrors = pearson.getCorrelationStandardErrors();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.7071067811865475, 0.5}, {0.5, 0.7071067811865475}});
        assertArrayEquals(expected.getData(), standardErrors.getData());
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix pValues = pearson.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.5, 0.5}, {0.5, 0.5}});
        assertArrayEquals(expected.getData(), pValues.getData());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = new double[]{1.0, 2.0, 3.0};
        double[] yArray = new double[]{4.0, 5.0, 6.0};
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertEquals(0.5, pearson.correlation(xArray, yArray), 0.001);
    }

    @Test
    public void testCorrelationFail() {
        double[] xArray = new double[]{1.0, 2.0};
        double[] yArray = new double[]{4.0, 5.0, 6.0};
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertThrows(IllegalArgumentException.class, () -> pearson.correlation(xArray, yArray));
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        assertArrayEquals(expected.getData(), correlationMatrix.getData());
    }

    @Test
    public void testCheckSufficientData() {
        RealMatrix matrix = mock(RealMatrix.class);
        when(matrix.getRowDimension()).thenReturn(1);
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(matrix));
    }
}