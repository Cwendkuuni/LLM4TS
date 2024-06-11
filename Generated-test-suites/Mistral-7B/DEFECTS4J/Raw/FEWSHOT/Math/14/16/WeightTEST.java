package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    @Test
    public void testWeightDiagonalCreation() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actual = weightInstance.getWeight();

        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareCreation() {
        double[] weight = new double[]{1.0, 2.0};
        new Weight(weight);
    }

    @Test
    public void testWeightMatrixCreation() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        Weight weightInstance = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix expected = new Array2DRowRealMatrix(weight);
        RealMatrix actual = weightInstance.getWeight();

        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixCreation() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0, 3.0}};
        new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightCopy() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actual = weightInstance.getWeight().copy();

        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightNull() {
        assertNull(new Weight(null));
    }

    @Test
    public void testWeightEmpty() {
        Weight weightInstance = new Weight(new Array2DRowRealMatrix(new double[0][0]));
        assertNull(weightInstance.getWeight());
    }

    @Test
    public void testWeightEquals() {
        double[] weight1 = new double[]{1.0, 2.0, 3.0};
        double[] weight2 = new double[]{1.0, 2.0, 3.0};
        Weight weightInstance1 = new Weight(weight1);
        Weight weightInstance2 = new Weight(weight2);

        assertTrue(weightInstance1.equals(weightInstance2));
    }

    @Test
    public void testWeightNotEquals() {
        double[] weight1 = new double[]{1.0, 2.0, 3.0};
        double[] weight2 = new double[]{1.0, 2.0, 4.0};
        Weight weightInstance1 = new Weight(weight1);
        Weight weightInstance2 = new Weight(weight2);

        assertFalse(weightInstance1.equals(weightInstance2));
    }
}