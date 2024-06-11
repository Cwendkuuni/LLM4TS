package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test cases for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData2DArray() {
        double[][] data = new double[0][];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testData2DArray() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{
                {1.0, 0.8660254037844385, 0.7343855532183873},
                {0.8660254037844385, 1.0, 0.8660254037844385},
                {0.7343855532183873, 0.8660254037844385, 1.0}},
                correlationMatrix.getColumnVector(0).toArray(),
                "First column of correlation matrix");
        assertEquals(3, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testData2DArrayInsufficientColumns() {
        double[][] data = {
                {1.0, 2.0},
                {4.0, 5.0},
                {7.0, 8.0},
                {10.0, 11.0}
        };
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testData2DArrayInsufficientRows() {
        double[][] data = {
                {1.0, 2.0},
                {4.0, 5.0}
        };
        new PearsonsCorrelation(data);
    }

    @Test
    public void testRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                                      {2.0, 3.0, 4.0},
                                                                      {3.0, 4.0, 5.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{
                {1.0, 0.8660254037844385, 0.816496580927726},
                {0.8660254037844385, 1.0, 0.7343855532183873},
                {0.816496580927726, 0.7343855532183873, 1.0}},
                correlationMatrix.getColumnVector(0).toArray(),
                "First column of correlation matrix");
        assertEquals(3, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixInsufficientColumns() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0},
                                                                      {2.0, 3.0},
                                                                      {3.0, 4.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixInsufficientRows() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0},
                                                                      {2.0, 3.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCovariance() {
        SimpleRegression regression = new SimpleRegression();
        double[][] data = {
                {1.0, 2.0, 1.0},
                {2.0, 3.0, 3.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 7.0}
        };
        regression.calculate(new Array2DRowRealMatrix(data));
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{
                {1.0, 0.8660254037844385, 0.7343855532183873},
                {0.8660254037844385, 1.0, 0.7343855532183873},
                {0.7343855532183873, 0.7343855532183873, 1.0}},
                correlationMatrix.getColumnVector(0).toArray(),
                "First column of correlation matrix");
        assertEquals(4, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceInsufficientColumns() {
        SimpleRegression regression = new SimpleRegression();
        double[][] data = {
                {1.0, 2.0, 1.0},
                {2.0, 3.0, 3.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0}
        };
        regression.calculate(new Array2DRowRealMatrix(data));
        Covariance covariance = new Covariance(data);
        new PearsonsCorrelation(covariance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceInsufficientRows() {
        SimpleRegression regression = new SimpleRegression();
        double[][] data = {
                {1.0, 2.0, 1.0},
                {2.0, 3.0, 3.0}
        };
        regression.calculate(new Array2DRowRealMatrix(data));
        Covariance covariance = new Covariance(data);
        new PearsonsCorrelation(covariance);
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {
                {1.0, 2.0, 1.0},
                {2.0, 3.0, 3.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 7.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix standardErrors = pc.getCorrelationStandardErrors();
        assertEquals(3, standardErrors.getRowDimension());
        assertEquals(3, standardErrors.getColumnDimension());
        assertArrayEquals(new double[][]{
                {0.5, 0.5, 0.5},
                {0.5, 0.5, 0.5},
                {0.5, 0.5, 0.5}},
                standardErrors.getColumnVector(0).toArray(),
                "First column of standard errors");
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValues() throws MathException {
        double[][] data = {
                {1.0, 2.0, 1.0},
                {2.0, 3.0, 3.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 7.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix pValues = pc.getCorrelationPValues();
        assertEquals(3, pValues.getRowDimension());
        assertEquals(3, pValues.getColumnDimension());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(pValues.getEntry(i, j) > 0);
            }
        }
    }

}