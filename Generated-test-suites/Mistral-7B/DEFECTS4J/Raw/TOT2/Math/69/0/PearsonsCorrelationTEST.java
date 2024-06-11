package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * JUnit tests for Pearson's Correlation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test constructor without data.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearlson.correlationMatrix);
        assertEquals(0, pearson.nObs);
    }

    /**
     * Test constructor with rectangular array.
     */
    @Test
    public void testDataMatrixConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        assertEquals(3, pearson.nObs);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.8, 0.8660254},
                                                                    {0.8, 1, 0.9659259},
                                                                    {0.8660254, 0.9659259, 1}});
        assertEquals(expected, pearson.correlationMatrix);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        assertEquals(3, pearson.nObs);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.8, 0.8660254},
                                                                    {0.8, 1, 0.9659259},
                                                                    {0.8660254, 0.9659259, 1}});
        assertEquals(expected, pearson.correlationMatrix);
    }

    /**
     * Test getCorrelationMatrix() method.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.8, 0.8660254},
                                                                    {0.8, 1, 0.9659259},
                                                                    {0.8660254, 0.9659259, 1}});
        assertEquals(expected, pearson.getCorrelationMatrix());
    }

    /**
     * Test getCorrelationStandardErrors() method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.4472136, 0.4139381, 0.7343874},
                                                                    {0.4139381, 0.4472136, 0.7343874},
                                                                    {0.7343874, 0.7343874, 0.4448856}});
        assertEquals(expected, pearson.getCorrelationStandardErrors());
    }

    /**
     * Test getCorrelationPValues() method.
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1 - 0.0277651, 1 - 0.0277651, 1},
                                                                    {1 - 0.0277651, 1, 1 - 0.0277651},
                                                                    {1, 1 - 0.0277651, 1}});
        assertEquals(expected, pearson.getCorrelationPValues());
    }

    /**
     * Test correlation method with rectangular array.
     */
    @Test
    public void testCorrelationWithData() {
        double[][] xArray = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] yArray = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        double result = pearson.correlation(xArray, yArray);
        assertEquals(0.9659259, result, 1e-6);
    }

    /**
     * Test correlation method with different-sized arrays.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentSizeArrays() {
        double[] xArray = {1, 2};
        double[] yArray = {1, 2, 3};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test correlation method with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {1};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test covarianceToCorrelation method.
     */
    @Test
    public void testCovarianceToCorrelationWithIdentityMatrix() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix output = new PearsonsCorrelation().covarianceToCorrelation(input);
        assertEquals(expected, output);
    }

    /**
     * Test covarianceToCorrelation method.
     */
    @Test
    public void testCovarianceToCorrelationWithOffDiagonalMatrix() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{{1, 0.5, 0}, {0.5, 1, 0.5}, {0, 0.5, 1}});
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0.83205015625, 0.6660254},
                                                                    {0.83205015625, 1, 0.83146960829},
                                                                    {0.6660254, 0.83146960829, 1}});
        RealMatrix output = new PearsonsCorrelation().covarianceToCorrelation(input);
        assertEquals(expected, output);
    }

    /**
     * Test checkSufficientData method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithEmptyMatrix() {
        new PearsonsCorrelation().checkSufficientData(new Array2DRowRealMatrix(new double[][]{}));
    }

    /**
     * Test checkSufficientData method.
     */
    @Test
    public void testCheckSufficientDataWithSufficientData() {
        new PearsonsCorrelation().checkSufficientData(new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
    }

    /**
     * Test checkSufficientData method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientData() {
        new PearsonsCorrelation().checkSufficientData(new Array2DRowRealMatrix(new double[][]{{1, 2}, {4, 5}}));
    }
}