package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation between two arrays with known correlation.
     */
    @Test
    public void testCorrelationBetweenArrays() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.1, 2.1, 3.1, 4.1};
        double expectedCorrelation = 0.9;

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationResult = correlation.correlation(x, y);

        assertEquals("Correlation between arrays x and y should be equal to " + expectedCorrelation,
                expectedCorrelation, correlationResult, 0.001);
    }

    /**
     * Test correlation between two columns of a matrix with known correlation.
     */
    @Test
    public void testCorrelationBetweenColumns() {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        double expectedCorrelation = 0.9;

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.computeCorrelationMatrix(matrix);
        double correlationResult = correlation.getCorrelationMatrix().getEntry(0, 1);

        assertEquals("Correlation between columns 0 and 1 of matrix should be equal to " + expectedCorrelation,
                expectedCorrelation, correlationResult, 0.001);
    }

    /**
     * Test correlation between two arrays with zero length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationBetweenZeroLengthArrays() {
        double[] x = {};
        double[] y = {};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(x, y);
    }

    /**
     * Test correlation between two columns of a matrix with insufficient data.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationBetweenColumnsInsufficientData() {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.computeCorrelationMatrix(matrix);
        correlation.getCorrelationMatrix().setEntry(2, 2, 0);
        correlation.getCorrelationStandardErrors();
    }

    /**
     * Test correlation between two arrays with negative values.
     */
    @Test
    public void testCorrelationBetweenArraysWithNegativeValues() {
        double[] x = {-1.0, 2.0, -3.0, 4.0};
        double[] y = {1.1, -2.1, 3.1, -4.1};
        double expectedCorrelation = 0.6;

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationResult = correlation.correlation(x, y);

        assertEquals("Correlation between arrays x and y should be equal to " + expectedCorrelation,
                expectedCorrelation, correlationResult, 0.001);
    }

    /**
     * Test correlation between two columns of a matrix with negative values.
     */
    @Test
    public void testCorrelationBetweenColumnsWithNegativeValues() {
        double[][] data = {{1.0, -2.0}, {1.1, -2.1}, {1.2, -2.2}, {1.3, -2.3}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        double expectedCorrelation = 0.6;

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.computeCorrelationMatrix(matrix);
        double correlationResult = correlation.getCorrelationMatrix().getEntry(0, 1);

        assertEquals("Correlation between columns 0 and 1 of matrix should be equal to " + expectedCorrelation,
                expectedCorrelation, correlationResult, 0.001);
    }
}