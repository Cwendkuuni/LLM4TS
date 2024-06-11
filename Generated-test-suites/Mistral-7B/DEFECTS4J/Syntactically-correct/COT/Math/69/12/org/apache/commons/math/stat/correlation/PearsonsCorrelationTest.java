package org.apache.commons.math.stat.correlation.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.apache.commons.math.MathException;
import org.apache.commons.math.NoDataException;
import org.junit.Test;

public class PearsonsCorrelationTest {

    /**
     * Test constructor without data
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertEquals(0, pc.getCorrelationMatrix().getRowDimension());
        assertEquals(0, pc.getCorrelationMatrix().getColumnDimension());
    }

    /**
     * Test constructor with 2x2 data array
     */
    @Test
    public void test2x2DataArray() {
        double[][] data = { { 1, 2 }, { 2, 3 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1, 0.8 }, { 0.8, 1 } });
        assertEquals(expected, pc.getCorrelationMatrix());
    }

    /**
     * Test constructor with 3x3 RealMatrix
     */
    @Test
    public void test3x3RealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.988807703, 0.884187986, 0.707106825 },
            { 0.884187986, 0.988807703, 0.707106825 },
            { 0.707106825, 0.707106825, 1 } });
        assertEquals(expected, pc.getCorrelationMatrix());
    }

    /**
     * Test constructor with Covariance
     */
    @Test
    public void testCovariance() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        RealMatrix covarianceMatrix = new Covariance().covariance(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.96729988, 0.93421957, 0.82216692 },
            { 0.93421957, 0.96729988, 0.82216692 },
            { 0.82216692, 0.82216692, 1 } });
        assertEquals(expected, pc.getCorrelationMatrix());
    }

    /**
     * Test getCorrelationMatrix()
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = { { 1, 2 }, { 2, 3 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1, 0.8 }, { 0.8, 1 } });
        assertEquals(expected, pc.getCorrelationMatrix());
    }

    /**
     * Test getCorrelationStandardErrors()
     */
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.032544317, 0.027831746, 0.02150601 },
            { 0.027831746, 0.032544417, 0.02150601 },
            { 0.02150601, 0.02150601, 0.0 } });
        assertEquals(expected, pc.getCorrelationStandardErrors());
    }

    /**
     * Test getCorrelationPValues()
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.03189364, 0.03192258, 0.03192258 },
            { 0.03192258, 0.03189364, 0.03192258 },
            { 0.03192258, 0.03192258, 0.0 } });
        assertEquals(expected, pc.getCorrelationPValues());
    }

    /**
     * Test correlation() with 2x1 arrays
     */
    @Test
    public void testCorrelation2x1Arrays() {
        double[] xArray = { 1, 2, 3 };
        double[] yArray = { 2, 3, 4 };
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    /**
     * Test correlation() with 1x1 arrays
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation1x1Arrays() {
        double[] xArray = { 1 };
        double[] yArray = { 1 };
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation() with arrays of different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationDifferentLengthArrays() {
        double[] xArray = { 1, 2, 3 };
        double[] yArray = { 1 };
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation() with arrays of insufficient data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = { 1 };
        double[] yArray = { 1, 2 };
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test covarianceToCorrelation()
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrix = new double[][] { { 1, 3, 2 }, { 3, 5, 4 }, { 2, 4, 1 } };
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(new BlockRealMatrix(covarianceMatrix));
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 0.916666667, 0.666666667, 0.6 },
            { 0.666666667, 0.916666667, 0.6 },
            { 0.6, 0.6, 1 } });
        assertEquals(expected, correlationMatrix);
    }

    /**
     * Test checkSufficientData()
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1 } });
        PearsonsCorrelation.checkSufficientData(matrix);
    }

}