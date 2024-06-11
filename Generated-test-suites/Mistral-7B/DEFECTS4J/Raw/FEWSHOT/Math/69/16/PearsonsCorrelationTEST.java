package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataConstructor() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8181818181818182, 0.8571428571428572},
                {0.8181818181818182, 1.0, 0.8181818181818182},
                {0.8571428571428572, 0.8181818181818182, 1.0}},
                correlationMatrix.getData(), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8181818181818182, 0.8571428571428572},
                {0.8181818181818182, 1.0, 0.8181818181818182},
                {0.8571428571428572, 0.8181818181818182, 1.0}},
                correlationMatrix.getData(), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8181818181818182, 0.8571428571428572},
                {0.8181818181818182, 1.0, 0.8181818181818182},
                {0.8571428571428572, 0.8181818181818182, 1.0}},
                correlationMatrix.getData(), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data).covariance();
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8181818181818182, 0.8571428571428572},
                {0.8181818181818182, 1.0, 0.8181818181818182},
                {0.8571428571428572, 0.8181818181818182, 1.0}},
                correlationMatrix.getData(), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8181818181818182, 0.8571428571428572},
                {0.8181818181818182, 1.0, 0.8181818181818182},
                {0.8571428571428572, 0.8181818181818182, 1.0}},
                correlationMatrix.getData(), 0.001);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertArrayEquals(new double[][]{{0.4924888590234735, 0.4269442487233231, 0.4758831805220184},
                {0.4269442487233231, 0.4924888590234735, 0.4269442487233231},
                {0.4758831805220184, 0.4269442487233231, 0.4924888590234735}},
                correlationStandardErrors.getData(), 0.001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        double[][] expected = new double[][]{{0.0, 0.0192456880472985, 0.0},
                {0.0192456880472985, 0.0, 0.0192456880472985},
                {0.0, 0.0192456880472985, 0.0}};
        assertArrayEquals(expected, correlationPValues.getData(), 0.001);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData() {
        double[] xArray = {1};
        double[] yArray = {};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationDataLength() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {1, 2};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8181818181818182, 0.8571428571428572},
                {0.8181818181818182, 1.0, 0.8181818181818182},
                {0.8571428571428572, 0.8181818181818182, 1.0}},
                correlationMatrix.getData(), 0.001);
    }
}