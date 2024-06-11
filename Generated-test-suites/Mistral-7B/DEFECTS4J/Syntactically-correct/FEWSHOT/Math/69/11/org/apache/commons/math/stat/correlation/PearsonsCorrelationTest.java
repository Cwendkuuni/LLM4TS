package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCreateEmptyInstance() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.correlationMatrix);
        assertEquals(0, correlation.nObs);
    }

    @Test
    public void testCreateFrom2DArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.correlationMatrix);
        assertEquals(3, correlation.nObs);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.741, 0.632}, {0.741, 1.0, 0.81}, {0.632, 0.81, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlation.correlationMatrix);
    }

    @Test
    public void testCreateFromRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertNotNull(correlation.correlationMatrix);
        assertEquals(3, correlation.nObs);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.741, 0.632}, {0.741, 1.0, 0.81}, {0.632, 0.81, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlation.correlationMatrix);
    }

    @Test
    public void testCreateFromCovariance() {
        double[][] covarianceData = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        Covariance covariance = new Covariance(covarianceData, 3);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        assertNotNull(correlation.correlationMatrix);
        assertEquals(3, correlation.nObs);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.741, 0.632}, {0.741, 1.0, 0.81}, {0.632, 0.81, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlation.correlationMatrix);
    }

    @Test
    public void testCreateFromCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 3);
        assertNotNull(correlation.correlationMatrix);
        assertEquals(3, correlation.nObs);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.741, 0.632}, {0.741, 1.0, 0.81}, {0.632, 0.81, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlation.correlationMatrix);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.741, 0.632}, {0.741, 1.0, 0.81}, {0.632, 0.81, 1.0}});
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.707, 0.422, 0.422}, {0.422, 0.707, 0.617}, {0.422, 0.617, 0.707}});
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{{0.499, 0.019, 0.019}, {0.019, 0.499, 0.031}, {0.019, 0.031, 0.499}});
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromInvalid2DArray() {
        double[][] data = {{1.0, 2.0}, {4.0, 5.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromInvalidRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.741, correlation, 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLengthArrays() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLengthData() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {4.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }
}