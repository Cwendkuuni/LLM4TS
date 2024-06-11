package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * JUnit 4 test file for PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    /**
     * Test constructor with rectangular array.
     */
    @Test
    public void testConstructorWithData() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        RealArray2D correlationMatrixData = correlationMatrix.getData();
        assertArrayEquals(new double[][] { { 1.0, 0.696326795942516, 0.6154228696501833 },
            { 0.696326795942516, 1.0, 0.6154228696501833 },
            { 0.6154228696501833, 0.6154228696501833, 1.0 } }, correlationMatrixData.getData());
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 4.0, 5.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        RealArray2D correlationMatrixData = correlationMatrix.getData();
        assertArrayEquals(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } }, correlationMatrixData.getData());
        assertEquals(2, pearsonsCorrelation.nObs);
    }

    /**
     * Test constructor with Covariance.
     */
    @Test
    public void testConstructorWithCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        RealArray2D correlationMatrixData = correlationMatrix.getData();
        assertArrayEquals(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } }, correlationMatrixData.getData());
        assertEquals(2, pearsonsCorrelation.nObs);
    }

    /**
     * Test constructor with Covariance and number of observations.
     */
    @Test
    public void testConstructorWithCovarianceAndNumberOfObservations() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        RealArray2D correlationMatrixData = correlationMatrix.getData();
        assertArrayEquals(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } }, correlationMatrixData.getData());
        assertEquals(covariance.getN(), pearsonsCorrelation.nObs);
    }

    /**
     * Test getCorrelationMatrix() method.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        // ... other assertions
    }

    /**
     * Test getCorrelationStandardErrors() method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
        assertEquals(2, correlationStandardErrors.getRowDimension());
        assertEquals(2, correlationStandardErrors.getColumnDimension());
        RealArray2D correlationStandardErrorsData = correlationStandardErrors.getData();
        assertArrayEquals(new double[][] { { 0.4472136023275753, 0.3214664567769955 },
            { 0.3214664567769955, 0.4472136023275753 } }, correlationStandardErrorsData.getData());
    }

    /**
     * Test getCorrelationPValues() method.
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
        assertEquals(2, correlationPValues.getRowDimension());
        assertEquals(2, correlationPValues.getColumnDimension());
        double[][] correlationPValuesData = correlationPValues.getData();
        assertArrayEquals(new double[][] { { 0.045, 0.03878834325574034 },
            { 0.03878834325574034, 0.045 } }, correlationPValuesData);
    }

    /**
     * Test covarianceToCorrelation() method.
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        RealArray2D correlationMatrixData = correlationMatrix.getData();
        assertArrayEquals(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } }, correlationMatrixData.getData());
    }

    /**
     * Test checkSufficientData() method.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0 } });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.checkSufficientData(matrix);
    }
}