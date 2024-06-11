package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDataArrayConstructor() {
        new PearsonsCorrelation(new double[0][0]);
    }

    @Test
    public void testDataArrayConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.846, 0.819}, {0.846, 1, 0.819}, {0.819, 0.819, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix actual = correlation.getCorrelationMatrix();
        assertEquals(expected, actual);
        assertEquals(3, correlation.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix actual = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.846, 0.819}, {0.846, 1, 0.819}, {0.819, 0.819, 1}});
        assertEquals(expected, actual);
        assertEquals(3, correlation.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix actual = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.846, 0.819}, {0.846, 1, 0.819}, {0.819, 0.819, 1}});
        assertEquals(expected, actual);
        assertEquals(3, correlation.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.846, 0.819}, {0.846, 1, 0.819}, {0.819, 0.819, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        double[][] expected = {{0.435, 0.425, 0.425}, {0.425, 0.435, 0.425}, {0.425, 0.425, 0.435}};
        assertEquals(new Array2DRowRealMatrix(expected), standardErrors);
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValues() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0.846, 0.819}, {0.846, 1, 0.819}, {0.819, 0.819, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(correlationMatrix);
        correlation.getCorrelationPValues();
    }
}