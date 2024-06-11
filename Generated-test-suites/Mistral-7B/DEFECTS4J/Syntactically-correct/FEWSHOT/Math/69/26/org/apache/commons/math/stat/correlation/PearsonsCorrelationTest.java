package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCreateInstanceWithoutData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.correlationMatrix);
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInstanceWithNullData() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testCreateInstanceWithRectangularArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertEquals(4, pc.nObs);
        assertNotNull(pc.correlationMatrix);
    }

    @Test
    public void testCreateInstanceWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertEquals(4, pc.nObs);
        assertNotNull(pc.correlationMatrix);
    }

    @Test
    public void testCreateInstanceWithCovariance() {
        Covariance covariance = new Covariance(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertEquals(2, pc.nObs);
        assertNotNull(pc.correlationMatrix);
    }

    @Test
    public void testCreateInstanceWithCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 2);
        assertEquals(2, pc.nObs);
        assertNotNull(pc.correlationMatrix);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.48284271, 0.48284271}, {0.48284271, 0.48284271}});
        RealMatrix actual = pc.getCorrelationStandardErrors();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.39269908, 0.39269908}, {0.39269908, 0.39269908}});
        RealMatrix actual = pc.getCorrelationPValues();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {5.0, 6.0, 7.0}, {7.0, 8.0, 9.0}};
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.96592583, 0.95625967, 0.94930533},
                {0.95625967, 0.96592583, 0.95625967},
                {0.94930533, 0.95625967, 0.96592583}});
        RealMatrix actual = new PearsonsCorrelation().computeCorrelationMatrix(data);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCorrelationMatrixWithRealMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {5.0, 6.0, 7.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.96592583, 0.95625967, 0.94930533},
                {0.95625967, 0.96592583, 0.95625967},
                {0.94930533, 0.95625967, 0.96592583}});
        RealMatrix actual = new PearsonsCorrelation().computeCorrelationMatrix(matrix);
        assertEquals(expected, actual);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 4.0, 6.0, 8.0};
        double expected = 0.96592583;
        double actual = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceToCorrelationWithNullCovarianceMatrix() {
        new PearsonsCorrelation().covarianceToCorrelation(null);
    }
}