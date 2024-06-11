package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.ArrayRealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new ArrayRealMatrix(new double[0][0]);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertEquals(0, correlation.getCorrelationMatrix().getRowDimension());
        assertEquals(0, correlation.getCorrelationMatrix().getColumnDimension());
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1, 0}, {0, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new ArrayRealMatrix(new double[][]{{1, 1}, {1, 1}});
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    @Test
    public void testSimpleRegression() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 4, 6, 8, 10};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(x, y);
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < x.length; i++) {
            regression.addData(x[i], y[i]);
        }
        assertEquals(correlation.getCorrelationMatrix().getEntry(0, 0), regression.getR(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputData() {
        double[] x = {1, 2};
        double[] y = {2, 3};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(x, y);
    }

    @Test
    public void testInvalidInputMatrix() {
        RealMatrix matrix = new ArrayRealMatrix(new double[1][2]);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        try {
            correlation.new PearsonsCorrelation(matrix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new ArrayRealMatrix(new double[][]{{1, 0.8453257284075354, 0.8453257284075354, 0.8453257284075354},
                {0.8453257284075354, 1, 0.8453257284075354, 0.8453257284075354},
                {0.8453257284075354, 0.8453257284075354, 1, 0.8453257284075354},
                {0.8453257284075354, 0.8453257284075354, 0.8453257284075354, 1}});
        assertEquals(expected, correlation.getCorrelationMatrix());
    }

    @Test
    public void testStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new ArrayRealMatrix(new double[][]{{1, 0.4331923597469578, 0.4331923597469578, 0.4331923597469578},
                {0.4331923597469578, 1, 0.4331923597469578, 0.4331923597469578},
                {0.4331923597469578, 0.4331923597469578, 1, 0.4331923597469578},
                {0.4331923597469578, 0.4331923597469578, 0.4331923597469578, 1}});
        assertEquals(expected, correlation.getCorrelationStandardErrors());
    }

    @Test
    public void testPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new ArrayRealMatrix(new double[][]{{1, 0.000152103970557823, 0.000152103970557823, 0.000152103970557823},
                {0.000152103970557823, 1, 0.000152103970557823, 0.000152103970557823},
                {0.000152103970557823, 0.000152103970557823, 1, 0.000152103970557823},
                {0.000152103970557823, 0.000152103970557823, 0.000152103970557823, 1}});
        assertEquals(expected, correlation.getCorrelationPValues());
    }
}