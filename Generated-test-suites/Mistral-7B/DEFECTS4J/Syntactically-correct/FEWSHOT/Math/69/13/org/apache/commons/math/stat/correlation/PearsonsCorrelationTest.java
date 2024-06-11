package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation}
 */
public class PearsonsCorrelationTest {

    /**
     * Test empty constructor
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.nObs);
    }

    /**
     * Test constructor with 2x2 input array
     */
    @Test
    public void test2x2Constructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertEquals(expected, correlation.getCorrelationMatrix());
        assertEquals(4, correlation.nObs);
    }

    /**
     * Test constructor with 3x3 input matrix
     */
    @Test
    public void test3x3Constructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.64280955, 0.53846154}, {0.64280955, 1.0, 0.8}, {0.53846154, 0.8, 1.0}});
        assertEquals(expected, correlation.getCorrelationMatrix());
        assertEquals(9, correlation.nObs);
    }

    /**
     * Test Covariance constructor
     */
    @Test
    public void testCovarianceConstructor() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.64280955}, {0.64280955, 1.0}});
        assertEquals(expected, correlation.getCorrelationMatrix());
        assertEquals(6, correlation.nObs);
    }

    /**
     * Test Covariance Matrix constructor
     */
    @Test
    public void testCovarianceMatrixConstructor() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 6);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.64280955}, {0.64280955, 1.0}});
        assertEquals(expected, correlation.getCorrelationMatrix());
        assertEquals(6, correlation.nObs);
    }

    /**
     * Test correlation method with double arrays
     */
    @Test
    public void testCorrelationDoubleArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    /**
     * Test correlation method with SimpleRegression
     */
    @Test
    public void testCorrelationSimpleRegression() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }
        double correlation = regression.getR();
        assertEquals(0.8, correlation, 0.01);
    }

    /**
     * Test covarianceToCorrelation method
     */
    @Test
    public void testCovarianceToCorrelation() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.64280955}, {0.64280955, 1.0}});
        assertEquals(expected, correlationMatrix);
    }
}