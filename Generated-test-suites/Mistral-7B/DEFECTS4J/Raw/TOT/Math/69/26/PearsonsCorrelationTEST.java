package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PearsonsCorrelation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation matrix construction from a 2x2 array
     */
    @Test
    public void testCorrelationMatrix2x2() {
        double[][] data = new double[][]{{1.0, 2.0, 1.5}, {2.1, 3.0, 1.8}, {3.1, 4.0, 2.4}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.8225806451612973},
            {0.8225806451612973, 1.0}
        });
        Assert.assertEquals(expected, pearson.getCorrelationMatrix());
    }

    /**
     * Test correlation matrix construction from a 3x3 array
     */
    @Test
    public void testCorrelationMatrix3x3() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0},
            {2.1, 3.0, 4.0},
            {3.1, 4.0, 5.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.7416596225056233, 0.6595505617317978},
            {0.7416596225056233, 1.0, 0.787400675395244},
            {0.6595505617317978, 0.787400675395244, 1.0}
        });
        Assert.assertEquals(expected, pearson.getCorrelationMatrix());
    }

    /**
     * Test correlation matrix construction from a Covariance
     */
    @Test
    public void testCorrelationMatrixFromCovariance() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.1, 3.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.7416596225056233},
            {0.7416596225056233, 1.0}
        });
        Assert.assertEquals(expected, pearson.getCorrelationMatrix());
    }

    /**
     * Test correlation matrix construction from a RealMatrix with less than 2 columns
     */
    @Test(expected = MathRuntimeException.class)
    public void testInsufficientData() {
        double[][] data = new double[][]{
            {1.0}
        };
        new PearsonsCorrelation(new BlockRealMatrix(data));
    }

    /**
     * Test correlation matrix construction from a RealMatrix with less than 2 rows
     */
    @Test(expected = MathRuntimeException.class)
    public void testInsufficientRows() {
        double[][] data = new double[][]{
            {1.0, 2.0},
            {1.0}
        };
        new PearsonsCorrelation(new BlockRealMatrix(data));
    }

    /**
     * Test correlation computation from a 2x2 array
     */
    @Test
    public void testCorrelation2x2() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.1, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        double expected = 0.7416596225056233;
        Assert.assertEquals(expected, pearson.correlation(data[0], data[1]), 0.001);
    }

    /**
     * Test correlation computation from a RealMatrix with less than 2 columns
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientColumns() {
        double[][] data = new double[][]{{1.0}, {2.1}};
        new PearsonsCorrelation().correlation(data[0], data[1]);
    }

    /**
     * Test correlation computation from a RealMatrix with less than 2 rows
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientRows2() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.0}};
        new PearsonsCorrelation().correlation(data[0], data[1]);
    }

    /**
     * Test correlation standard errors
     */
    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.1, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix stdErrors = pearson.getCorrelationStandardErrors();
        double expected = 0.2792430122333009;
        Assert.assertEquals(expected, stdErrors.getEntry(0, 0), 0.001);
        Assert.assertEquals(expected, stdErrors.getEntry(1, 0), 0.001);
        Assert.assertEquals(expected, stdErrors.getEntry(0, 1), 0.001);
        Assert.assertEquals(expected, stdErrors.getEntry(1, 1), 0.001);
    }

    /**
     * Test correlation p-values
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.1, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix pValues = pearson.getCorrelationPValues();
        double expected = 0.04551295505849495;
        Assert.assertEquals(expected, pValues.getEntry(0, 0), 0.001);
        Assert.assertEquals(expected, pValues.getEntry(1, 0), 0.001);
        Assert.assertEquals(expected, pValues.getEntry(0, 1), 0.001);
        Assert.assertEquals(expected, pValues.getEntry(1, 1), 0.001);
    }
}