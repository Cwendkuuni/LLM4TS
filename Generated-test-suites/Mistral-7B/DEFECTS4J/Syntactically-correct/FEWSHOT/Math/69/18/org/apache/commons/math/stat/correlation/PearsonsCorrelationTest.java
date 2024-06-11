package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    @Test
    public void testDoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc.correlationMatrix);
        assertEquals(2, pc.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertNotNull(pc.correlationMatrix);
        assertEquals(2, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructorWithSize() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix, 3);
        assertNotNull(pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.966666666666667, 0.954545454545455},
            {0.954545454545455, 0.966666666666667}});
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertArrayEquals(expected.getData(), correlationMatrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.011111111111111, -0.011111111111111},
            {-0.011111111111111, 0.011111111111111}});
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertArrayEquals(expected.getData(), correlationStandardErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.045, 0.955},
            {0.955, 0.045}});
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertArrayEquals(expected.getData(), correlationPValues.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForIncorrectDataSize() {
        double[][] data = {{1.0, 2.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForEmptyData() {
        double[][] data = {};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForDifferentLengthArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {1.0, 2.0, 3.0, 4.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8, 0.6}, {0.6, 0.8}});
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertArrayEquals(expected.getData(), correlationMatrix.getData());
    }

    @Test
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInsufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.checkSufficientData(matrix);
    }

}