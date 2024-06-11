package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testCreateInstance() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.nObs);
    }

    @Test
    public void testCreateFromDataArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.843, 0.678}, {0.843, 1, 0.966}, {0.678, 0.966, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(4, correlation.nObs);
    }

    @Test
    public void testCreateFromRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}});
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.843, 0.678}, {0.843, 1, 0.966}, {0.678, 0.966, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(4, correlation.nObs);
    }

    @Test
    public void testCreateFromCovariance() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{3, 2.5, 2}, {2.5, 4.5, 2.5}, {2, 2.5, 5}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(new Covariance(data));
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.843, 0.678}, {0.843, 1, 0.966}, {0.678, 0.966, 1}});
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(4, correlation.nObs);
    }

    @Test
    public void testCreateFromCovarianceMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{3, 2.5, 2}, {2.5, 4.5, 2.5}, {2, 2.5, 5}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 4);
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.843, 0.678}, {0.843, 1, 0.966}, {0.678, 0.966, 1}});
        assertEquals(correlationMatrix, correlation.getCorrelationMatrix());
        assertEquals(4, correlation.nObs);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCreateFromNullCovariance() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(null, 4);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCreateFromInsufficientDataCovariance() {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{3, 2.5}, {2.5, 4.5}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromDataArrayInvalidDimension() {
        double[][] data = {{1}, {2, 3}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromRealMatrixInvalidDimension() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.843, 0.678}, {0.843, 1, 0.966}, {0.678, 0.966, 1}});
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.631, 0.631, 0.631}, {0.631, 0.547, 0.722}, {0.631, 0.722, 0.425}});
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.843, 0.678}, {0.843, 1, 0.966}, {0.678, 0.966, 1}});
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        double[][] expectedCorrelationPValues = new double[][]{{0.145, 0.373, 0.018}, {0.373, 0.145, 0.004}, {0.018, 0.004, 0.999}};
        for (int i = 0; i < correlationPValues.getRowDimension(); i++) {
            for (int j = 0; j < correlationPValues.getColumnDimension(); j++) {
                assertEquals(expectedCorrelationPValues[i][j], correlationPValues.getEntry(i, j), 0.001);
            }
        }
    }
}