package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for PearsonsCorrelation
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor with no data
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    /**
     * Test constructor with data array
     */
    @Test
    public void testDataArrayConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.86602540378444, 0.88505701787219},
            {0.86602540378444, 1.0, 0.86602540378444},
            {0.88505701787219, 0.86602540378444, 1.0}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test constructor with RealMatrix
     */
    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.86602540378444, 0.88505701787219},
            {0.86602540378444, 1.0, 0.86602540378444},
            {0.88505701787219, 0.86602540378444, 1.0}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test constructor with Covariance
     */
    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        PearsonsCorrelation pc = new PearsonsCorrelation(new Covariance(covarianceMatrix));
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.44721360333254},
            {0.44721360333254, 1.0}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(2, pc.nObs);
    }

    /**
     * Test constructor with CovarianceMatrix and number of observations
     */
    @Test
    public void testCovarianceMatrixWithNumberOfObservationsConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.44721360333254},
            {0.44721360333254, 1.0}
        });
        RealMatrix actual = pc.correlationMatrix;
        assertEquals(expected, actual);
        assertEquals(3, pc.nObs);
    }

    /**
     * Test getCorrelationMatrix()
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.86602540378444, 0.88505701787219},
            {0.86602540378444, 1.0, 0.86602540378444},
            {0.88505701787219, 0.86602540378444, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        assertEquals(expected, actual);
    }

    /**
     * Test getCorrelationStandardErrors()
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {0.43301270189222, 0.41612687984714, 0.41612687984714},
            {0.41612687984714, 0.43301270189222, 0.41612687984714},
            {0.41612687984714, 0.41612687984714, 0.43301270189222}
        });
        RealMatrix actual = pc.getCorrelationStandardErrors();
        assertEquals(expected, actual);
    }

    /**
     * Test getCorrelationPValues()
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0253101465306485, 0.0253101465306485},
            {0.0253101465306485, 1.0, 0.0253101465306485},
            {0.0253101465306485, 0.0253101465306485, 1.0}
        });
        RealMatrix actual = pc.getCorrelationPValues();
        assertEquals(expected, actual);
    }

    /**
     * Test computation of correlation matrix from a RealMatrix
     */
    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9}
        });
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.86602540378444, 0.88505701787219},
            {0.86602540378444, 1.0, 0.86602540378444},
            {0.88505701787219, 0.86602540378444, 1.0}
        });
        RealMatrix actual = new PearsonsCorrelation().computeCorrelationMatrix(matrix);
        assertEquals(expected, actual);
    }

    /**
     * Test computation of correlation matrix from a data array
     */
    @Test
    public void testComputeCorrelationMatrixWithDataArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.86602540378444, 0.88505701787219},
            {0.86602540378444, 1.0, 0.86602540378444},
            {0.88505701787219, 0.86602540378444, 1.0}
        });
        RealMatrix actual = new PearsonsCorrelation().computeCorrelationMatrix(data);
        assertEquals(expected, actual);
    }

    /**
     * Test correlation method with data arrays.
     */
    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 3, 4, 5, 6};
        double expected = 0.8;
        double actual = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(expected, actual, 1e-10);
    }

    /**
     * Test covarianceToCorrelation method
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2}, {2, 3}
        });
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.44721360333254},
            {0.44721360333254, 1.0}
        });
        RealMatrix actual = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(expected, actual);
    }

    /**
     * Test checkSufficientData method
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1}
        });
        new PearsonsCorrelation().checkSufficientData(matrix);
    }
}