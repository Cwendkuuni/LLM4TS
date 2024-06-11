package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixUtils;
import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testDoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = RealMatrixUtils.createCorrelationMatrix(RealMatrixUtils.createMatrix(data));
        assertEquals(expectedCorrelationMatrix, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expectedCorrelationMatrix = RealMatrixUtils.createCorrelationMatrix(matrix);
        assertEquals(expectedCorrelationMatrix, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix expectedCorrelationMatrix = RealMatrixUtils.createCorrelationMatrix(RealMatrixUtils.createMatrix(data));
        assertEquals(expectedCorrelationMatrix, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testNullRealMatrixConstructor() {
        RealMatrix matrix = null;
        assertThrows(MathRuntimeException.class, () -> new PearsonsCorrelation(matrix));
    }

    @Test
    public void testInsufficientDataRealMatrixConstructor() {
        double[][] data = {{1.0, 2.0}};
        assertThrows(MathRuntimeException.class, () -> new PearsonsCorrelation(new BlockRealMatrix(data)));
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data).covariance();
        RealMatrix expectedCorrelationMatrix = RealMatrixUtils.createCorrelationMatrix(RealMatrixUtils.createMatrix(data));
        RealMatrix correlationMatrix = new PearsonsCorrelation(covarianceMatrix, 3).correlationMatrix;
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix expectedCorrelationMatrix = RealMatrixUtils.createCorrelationMatrix(RealMatrixUtils.createMatrix(data));
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix actualCorrelationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(data).getCorrelationMatrix();
        RealMatrix expectedCorrelationStandardErrors = RealMatrixUtils.createMatrix(new double[][]{
                {0.8133557822732569, 0.6788394522342202, 0.510578898278354},
                {0.6788394522342202, 0.8133557822732569, 0.510578898278354},
                {0.510578898278354, 0.510578898278354, 0.8133557822732569}
        });
        RealMatrix actualCorrelationStandardErrors = new PearsonsCorrelation(data).getCorrelationStandardErrors();
        assertEquals(expectedCorrelationStandardErrors, actualCorrelationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix correlationMatrix = new PearsonsCorrelation(data).getCorrelationMatrix();
        RealMatrix expectedCorrelationPValues = RealMatrixUtils.createMatrix(new double[][]{
                {0.0, 0.4341545105324532, 0.8754410463143779},
                {0.4341545105324532, 0.0, 0.8754410463143779},
                {0.8754410463143779, 0.8754410463143779, 0.0}
        });
        RealMatrix actualCorrelationPValues = new PearsonsCorrelation(data).getCorrelationPValues();
        assertEquals(expectedCorrelationPValues, actualCorrelationPValues);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArrayDimensions() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {2.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}