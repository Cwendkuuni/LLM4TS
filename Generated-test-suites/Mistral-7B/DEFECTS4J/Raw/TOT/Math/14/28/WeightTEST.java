package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.optimization.Weight;
import org.junit.Test;

/**
 * Test class for Weight class.
 */
public class WeightTest extends RealMatrixTestCase {

    /**
     * Test constructor with a diagonal weight matrix.
     */
    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertEquals(expected, weightObj.getWeight());
    }

    /**
     * Test constructor with a non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrix() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
    }

    /**
     * Test constructor with a square weight matrix.
     */
    @Test
    public void testSquareWeightMatrix() {
        double[][] weight = {{1.0, 2.0}, {2.0, 3.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 2.0);
        expected.setEntry(1, 0, 2.0);
        expected.setEntry(1, 1, 3.0);
        assertEquals(expected, weightObj.getWeight());
    }
}