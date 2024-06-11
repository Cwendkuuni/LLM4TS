package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Random;

import org.junit.Test;

public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}
     */
    @Test
    public void testDefaultConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertEquals(0, pearsonsCorrelation.getNObs());
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}
     */
    @Test
    public void testDoubleArrayConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertEquals(3, pearsonsCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.872}, {0.8, 1.0, 0.8}, {0.872, 0.8, 1.0}}, correlationMatrix.getData());

        // test correlation standard errors
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertArrayEquals(new double[][]{{0.4826, 0.4338, 0.4178}, {0.4338, 0.4826, 0.4178}, {0.4178, 0.4178, 0.3889}}, correlationStandardErrors.getData(), 0.001);

        // test correlation p-values
        try {
            RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
            assertEquals(3, correlationPValues.getRowDimension());
            assertEquals(3, correlationPValues.getColumnDimension());
            assertArrayEquals(new double[][]{{1.0, 0.662, 0.627}, {0.662, 1.0, 0.627}, {0.627, 0.627, 1.0}}, correlationPValues.getData(), 0.001);
        } catch (MathException e) {
            fail("Unexpected MathException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}
     */
    @Test
    public void testRealMatrixConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(inputMatrix);
        assertEquals(3, pearsonsCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.872}, {0.8, 1.0, 0.8}, {0.872, 0.8, 1.0}}, correlationMatrix.getData());

        // test correlation standard errors
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertArrayEquals(new double[][]{{0.4826, 0.4338, 0.4178}, {0.4338, 0.4826, 0.4178}, {0.4178, 0.4178, 0.3889}}, correlationStandardErrors.getData(), 0.001);

        // test correlation p-values
        try {
            RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
            assertEquals(3, correlationPValues.getRowDimension());
            assertEquals(3, correlationPValues.getColumnDimension());
            assertArrayEquals(new double[][]{{1.0, 0.662, 0.627}, {0.662, 1.0, 0.627}, {0.627, 0.627, 1.0}}, correlationPValues.getData(), 0.001);
        } catch (MathException e) {
            fail("Unexpected MathException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(Covariance)}
     */
    @Test
    public void testCovarianceConstructor() {
        double[][] covarianceData = new double[][]{{0.1, 0.2, 0.1}, {0.2, 0.4, 0.2}, {0.1, 0.2, 0.4}};
        Covariance covariance = new Covariance(covarianceData);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        assertEquals(3, pearsonsCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.6, 0.6}, {0.6, 1.0, 0.6}, {0.6, 0.6, 1.0}}, correlationMatrix.getData());

        // test correlation standard errors
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertArrayEquals(new double[][]{{0.5, 0.25, 0.25}, {0.25, 0.5, 0.25}, {0.25, 0.25, 0.5}}, correlationStandardErrors.getData(), 0.001);

        // test correlation p-values
        try {
            RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
            assertEquals(3, correlationPValues.getRowDimension());
            assertEquals(3, correlationPValues.getColumnDimension());
            assertArrayEquals(new double[][]{{0.5, 0.466, 0.466}, {0.466, 0.5, 0.466}, {0.466, 0.466, 0.5}}, correlationPValues.getData(), 0.001);
        } catch (MathException e) {
            fail("Unexpected MathException: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.8, 0.872}, {0.8, 1.0, 0.8}, {0.872, 0.8, 1.0}}, correlationMatrix.getData());
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}
     */
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertArrayEquals(new double[][]{{0.4826, 0.4338, 0.4178}, {0.4338, 0.4826, 0.4178}, {0.4178, 0.4178, 0.3889}}, correlationStandardErrors.getData(), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.662, 0.627}, {0.662, 1.0, 0.627}, {0.627, 0.627, 1.0}}, correlationPValues.getData(), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}
     */
    @Test
    public void testCorrelationMethod() {
        double[] xArray = new double[]{1.0, 1.5, 2.0};
        double[] yArray = new double[]{2.0, 2.5, 2.0};
        SimpleRegression regression = new SimpleRegression();
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);

        double[] xArray2 = new double[]{1.0, 1.5, 2.0};
        double[] yArray2 = new double[]{2.0, 3.0, 2.0};
        try {
            correlation = PearsonsCorrelation.correlation(xArray2, yArray2);
            fail("Unexpected correlation value: " + correlation);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}