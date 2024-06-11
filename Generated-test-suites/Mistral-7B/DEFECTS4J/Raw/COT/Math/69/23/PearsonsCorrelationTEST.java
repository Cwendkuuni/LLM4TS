package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * Test class for PearsonsCorrelation.
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data.
     */
    @org.junit.Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    /**
     * Test constructor with data.
     */
    @org.junit.Test
    public void testDataConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.888888888888889, 0.774596669241403},
                                                                                   {0.888888888888889, 1.0, 0.888888888888889},
                                                                                   {0.774596669241403, 0.888888888888889, 1.0}});
        assertEquals(expectedCorrelationMatrix, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @org.junit.Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertEquals(expectedCorrelationMatrix, pc.correlationMatrix);
        assertEquals(2, pc.nObs);
    }

    /**
     * Test constructor with Covariance.
     */
    @org.junit.Test
    public void testCovarianceConstructor() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0},
                           {4.0, 5.0, 6.0},
                           {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.888888888888889, 0.774596669241403},
                                                                                   {0.888888888888889, 1.0, 0.888888888888889},
                                                                                   {0.774596669241403, 0.888888888888889, 1.0}});
        assertEquals(expectedCorrelationMatrix, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test constructor with CovarianceMatrix.
     */
    @org.junit.Test
    public void testCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.5, 0.5, 1.0},
                                                                             {0.5, 2.5, 0.5},
                                                                             {1.0, 0.5, 3.5}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5714285714285713, 0.6666666666666667},
                                                                                   {0.5714285714285713, 1.0, 0.5},
                                                                                   {0.6666666666666667, 0.5, 1.0}});
        assertEquals(expectedCorrelationMatrix, pc.correlationMatrix);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test getCorrelationMatrix method.
     */
    @org.junit.Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0},
                           {4.0, 5.0, 6.0},
                           {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.888888888888889, 0.774596669241403},
                                                                                   {0.888888888888889, 1.0, 0.888888888888889},
                                                                                   {0.774596669241403, 0.888888888888889, 1.0}});
        RealMatrix actualCorrelationMatrix = pc.getCorrelationMatrix();
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    /**
     * Test getCorrelationStandardErrors method.
     */
    @org.junit.Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0},
                           {4.0, 5.0, 6.0},
                           {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.888888888888889, 0.774596669241403},
                                                                                   {0.888888888888889, 1.0, 0.888888888888889},
                                                                                   {0.774596669241403, 0.888888888888889, 1.0}});
        RealMatrix actualCorrelationMatrix = pc.getCorrelationMatrix();
        RealMatrix actualCorrelationStandardErrors = pc.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new BlockRealMatrix(new double[][]{{0.4443866464646465, 0.4168575345858435, 0.3872983345744315},
                                                                                         {0.4168575345858435, 0.4443866464646465, 0.4168575345858435},
                                                                                         {0.3872983345744315, 0.4168575345858435, 0.4443866464646465}});
        assertEquals(expectedCorrelationStandardErrors, actualCorrelationStandardErrors);
    }

    /**
     * Test getCorrelationPValues method.
     */
    @org.junit.Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0},
                           {4.0, 5.0, 6.0},
                           {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.888888888888889, 0.774596669241403},
                                                                                   {0.888888888888889, 1.0, 0.888888888888889},
                                                                                   {0.774596669241403, 0.888888888888889, 1.0}});
        RealMatrix actualCorrelationMatrix = pc.getCorrelationMatrix();
        RealMatrix actualCorrelationPValues = pc.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new BlockRealMatrix(new double[][]{{1.0, 0.02411233053434742, 0.04722466106869485},
                                                                                    {0.02411233053434742, 1.0, 0.02411233053434742},
                                                                                    {0.04722466106869485, 0.02411233053434742, 1.0}});
        assertEquals(expectedCorrelationPValues, actualCorrelationPValues);
    }

    /**
     * Test correlation method with double array.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidDoubleArray() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with valid double arrays.
     */
    @org.junit.Test
    public void testCorrelationWithValidDoubleArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expectedCorrelation = 0.8888888888888889;
        double actualCorrelation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, actualCorrelation, 0.001);
    }

    /**
     * Test covarianceToCorrelation method.
     */
    @org.junit.Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{2.0, 1.0, 1.0},
                                                                             {1.0, 3.0, 2.0},
                                                                             {1.0, 2.0, 5.0}});
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5, 0.5773502691896258},
                                                                                   {0.5, 1.0, 0.7407407407407408},
                                                                                   {0.5773502691896258, 0.7407407407407408, 1.0}});
        RealMatrix actualCorrelationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }
}