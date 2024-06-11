package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData1() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData2() {
        double[][] data = new double[2][1];
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData3() {
        double[][] data = new double[1][2];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testConstructFromMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonsCorrelation.nObs);

        RealMatrix expected = new BlockRealMatrix(new double[][]{{1, 0.741, 0.713}, {0.741, 1, 0.741}, {0.713, 0.741, 1}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.getCorrelationMatrix().getData(), 0.001);
    }

    @Test
    public void testConstructFromCovariance() {
        double[][] covData = {{1, 2, 2, 2}, {2, 5, 2, 5}, {2, 2, 5, 8}, {2, 5, 8, 15}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covData);
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(4, pearsonsCorrelation.nObs);

        RealMatrix expected = new BlockRealMatrix(new double[][]{{1, 0.943, 0.927, 0.912}, {0.943, 1, 0.943, 0.934}, {0.927, 0.943, 1, 0.964}, {0.912, 0.934, 0.964, 1}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.getCorrelationMatrix().getData(), 0.001);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.5, -0.5, 0.5}, {-0.5, 0.5, -0.5}, {0.5, -0.5, 0.5}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.getCorrelationStandardErrors().getData(), 0.001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.676, 0.324, 0.676}, {0.324, 0.676, 0.324}, {0.676, 0.324, 0.676}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.getCorrelationPValues().getData(), 0.001);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData() {
        double[] xArray = {1};
        double[] yArray = {2, 3};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData2() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {2};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covData = {{1, 2, 2, 2}, {2, 5, 2, 5}, {2, 2, 5, 8}, {2, 5, 8, 15}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covData);
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1, 0.943, 0.927, 0.912}, {0.943, 1, 0.943, 0.934}, {0.927, 0.943, 1, 0.964}, {0.912, 0.934, 0.964, 1}});
        assertArrayEquals(expected.getData(), correlationMatrix.getData(), 0.001);
    }

}