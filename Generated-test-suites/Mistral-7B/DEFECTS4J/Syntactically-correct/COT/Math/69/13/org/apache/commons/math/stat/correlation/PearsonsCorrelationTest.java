package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test suite for {@link PearsonsCorrelation} class
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    /**
     * Test constructor from rectangular array
     */
    @Test
    public void testConstructorFromArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.832, 0.721},
                {0.832, 1.0, 0.832}, {0.721, 0.832, 1.0}});
        assertEquals(expectedCorrMatrix, pc.correlationMatrix);
        assertEquals(9, pc.nObs);
    }

    /**
     * Test constructor from RealMatrix
     */
    @Test
    public void testConstructorFromRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.832, 0.721},
                {0.832, 1.0, 0.832}, {0.721, 0.832, 1.0}});
        assertEquals(expectedCorrMatrix, pc.correlationMatrix);
        assertEquals(9, pc.nObs);
    }

    /**
     * Test constructor from Covariance
     */
    @Test
    public void testConstructorFromCovariance() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.832, 0.721},
                {0.832, 1.0, 0.832}, {0.721, 0.832, 1.0}});
        assertEquals(expectedCorrMatrix, pc.correlationMatrix);
        assertEquals(9, pc.nObs);
    }

    /**
     * Test constructor from RealMatrix and number of observations
     */
    @Test
    public void testConstructorFromRealMatrixAndNumberOfObservations() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        assertEquals(expectedCorrMatrix, pc.correlationMatrix);
        assertEquals(2, pc.nObs);
    }

    /**
     * Test getCorrelationMatrix()
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.832, 0.721},
                {0.832, 1.0, 0.832}, {0.721, 0.832, 1.0}});
        assertEquals(expectedCorrMatrix, pc.getCorrelationMatrix());
    }

    /**
     * Test getCorrelationStandardErrors()
     */
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrStandardErrors = new BlockRealMatrix(new double[][]{{0.619, 0.557, 0.539},
                {0.557, 0.619, 0.557}, {0.539, 0.557, 0.619}});
        assertEquals(expectedCorrStandardErrors, pc.getCorrelationStandardErrors());
    }

    /**
     * Test getCorrelationPValues()
     * @throws MathException
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expectedCorrPValues = new BlockRealMatrix(new double[][]{{0.0, 0.437, 0.279},
                {0.437, 0.0, 0.437}, {0.279, 0.437, 0.0}});
        assertEquals(expectedCorrPValues, pc.getCorrelationPValues());
    }

    /**
     * Test computeCorrelationMatrix()
     */
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.832, 0.721},
                {0.832, 1.0, 0.832}, {0.721, 0.832, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix computedCorrMatrix = pc.computeCorrelationMatrix(new BlockRealMatrix(data));
        assertEquals(expectedCorrMatrix, computedCorrMatrix);
    }

    /**
     * Test correlation()
     */
    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expectedCorrelation = 0.832;
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, correlation, 1e-8);
    }

    /**
     * Test covarianceToCorrelation()
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.5, 0.5, -0.5},
                {0.5, 2.5, 1.5}, {-0.5, 1.5, 3.5}});
        RealMatrix expectedCorrMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.832, 0.721},
                {0.832, 1.0, 0.832}, {0.721, 0.832, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix computedCorrMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertEquals(expectedCorrMatrix, computedCorrMatrix);
    }

    /**
     * Test checkSufficientData()
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataWithInsufficientData() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0}});
        new PearsonsCorrelation().checkSufficientData(matrix);
    }
}