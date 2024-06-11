package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNotNull(pearson.correlationMatrix);
        assertEquals(0, pearson.nObs);
    }

    /**
     * Test constructor from a 2x2 rectangle array
     */
    @Test
    public void testConstructorWithData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        assertNotNull(pearson.correlationMatrix);
        assertEquals(2, pearson.nObs);
    }

    /**
     * Test constructor from a RealMatrix
     */
    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        assertNotNull(pearson.correlationMatrix);
        assertEquals(2, pearson.nObs);
    }

    /**
     * Test getCorrelationMatrix() method
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
    }

    /**
     * Test getCorrelationStandardErrors() method
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearson.getCorrelationStandardErrors();
        assertEquals(2, correlationStandardErrors.getRowDimension());
        assertEquals(2, correlationStandardErrors.getColumnDimension());
        assertEquals(0.4472135954997559, correlationStandardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.4472135954997559, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.4472135954997559, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.4472135954997559, correlationStandardErrors.getEntry(1, 1), 0.001);
    }

    /**
     * Test getCorrelationPValues() method
     * @throws MathException
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearson.getCorrelationPValues();
        assertEquals(2, correlationPValues.getRowDimension());
        assertEquals(2, correlationPValues.getColumnDimension());
        // The values are not exactly 0.5 but close to it due to floating point
        // precision
        assertEquals(0.666587383935346, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(0.666587383935346, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.666587383935346, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.666587383935346, correlationPValues.getEntry(1, 1), 0.001);
    }

    /**
     * Test correlation method with two arrays
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrays() {
        double[] xArray = new double[]{1.0};
        double[] yArray = new double[]{2.0, 3.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with valid arrays
     */
    @Test
    public void testCorrelationWithValidArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    /**
     * Test covarianceToCorrelation method
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
    }
}