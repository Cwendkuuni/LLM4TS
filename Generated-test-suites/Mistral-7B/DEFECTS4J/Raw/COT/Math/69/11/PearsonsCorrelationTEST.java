package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

/**
 * Tests for the PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Tests the default constructor.
     */
    @Test(expected = AssertionError.class)
    public void testDefaultConstructor() {
        new PearsonsCorrelation();
    }

    /**
     * Tests the constructor from a null input array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFromNullData() {
        new PearsonsCorrelation(null);
    }

    /**
     * Tests the constructor from a rectangular array with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFromInsufficientData() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    /**
     * Tests the constructor from a rectangular array with correct data.
     */
    @Test
    public void testConstructorFromData() {
        double[][] data = new double[][] { { 1, 2, 3 }, { 4, 5, 6 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.8, 0.6 }, { 0.8, 1.0, 0.8 }, { 0.6, 0.8, 1.0 } });
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    /**
     * Tests the constructor from a RealMatrix with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFromInsufficientRealMatrixData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1 }, { 2 } });
        new PearsonsCorrelation(matrix);
    }

    /**
     * Tests the constructor from a RealMatrix with correct data.
     */
    @Test
    public void testConstructorFromRealMatrixData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.8, 0.6 }, { 0.8, 1.0, 0.8 }, { 0.6, 0.8, 1.0 } });
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    /**
     * Tests the constructor from a covariance matrix with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFromInsufficientCovarianceData() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 2, 1 } });
        new PearsonsCorrelation(covarianceMatrix, 3);
    }

    /**
     * Tests the constructor from a covariance matrix with correct data.
     */
    @Test
    public void testConstructorFromCovarianceData() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1, 2, 2 }, { 2, 4, 2 }, { 2, 2, 4 } });
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.8, 0.6 }, { 0.8, 1.0, 0.8 }, { 0.6, 0.8, 1.0 } });
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    /**
     * Tests the getCorrelationMatrix method with a default correlation matrix.
     */
    @Test
    public void testGetCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
    }

    /**
     * Tests the getCorrelationStandardErrors method with correct data.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][] { { 1, 2, 3 }, { 4, 5, 6 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.7071067811865476, 0.577350269189626, 0.3828264565398732 },
                { 0.577350269189626, 0.8164965809277261, 0.577350269189626 },
                { 0.3828264565398732, 0.577350269189626, 0.8164965809277261 } });
        RealMatrix actual = correlation.getCorrelationStandardErrors();
        assertEquals(expected, actual);
    }

    /**
     * Tests the getCorrelationPValues method with correct data.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][] { { 1, 2, 3 }, { 4, 5, 6 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.02312251814643252, 0.005787482980155646, 0.005787482980155646 },
                { 0.005787482980155646, 0.02312251814643252, 0.005787482980155646 },
                { 0.005787482980155646, 0.005787482980155646, 0.02312251814643252 } });
        RealMatrix actual = correlation.getCorrelationPValues();
        assertEquals(expected, actual);
    }

    /**
     * Tests the correlation method with a null input array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationFromNullArray() {
        new PearsonsCorrelation().correlation(null, null);
    }

    /**
     * Tests the correlation method with different length arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengthArrays() {
        double[] xArray = new double[] { 1, 2, 3 };
        double[] yArray = new double[] { 1, 2 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Tests the correlation method with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = new double[] { 1 };
        double[] yArray = new double[] { 1 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Tests the correlation method with correct data.
     */
    @Test
    public void testCorrelationWithCorrectData() {
        double[] xArray = new double[] { 1, 2, 3, 4 };
        double[] yArray = new double[] { 1, 4, 9, 16 };
        double expected = 0.96;
        double actual = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(expected, actual, 0.01);
    }

    /**
     * Tests the covarianceToCorrelation method with a covariance matrix with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceToCorrelationWithInsufficientData() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1 } });
        new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
    }

    /**
     * Tests the covarianceToCorrelation method with a covariance matrix with correct data.
     */
    @Test
    public void testCovarianceToCorrelationWithCorrectData() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] { { 1, 2, 2 }, { 2, 4, 2 }, { 2, 2, 4 } });
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.8, 0.6 }, { 0.8, 1.0, 0.8 }, { 0.6, 0.8, 1.0 } });
        RealMatrix actual = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(expected, actual);
    }

    /**
     * Tests the checkSufficientData method with an empty matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {});
        new PearsonsCorrelation().checkSufficientData(matrix);
    }

    /**
     * Tests the checkSufficientData method with a matrix with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1 }, { 2 } });
        new PearsonsCorrelation().checkSufficientData(matrix);
    }

    /**
     * Tests the checkSufficientData method with a matrix with sufficient data.
     */
    @Test
    public void testCheckSufficientDataWithSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        new PearsonsCorrelation().checkSufficientData(matrix);
    }
}