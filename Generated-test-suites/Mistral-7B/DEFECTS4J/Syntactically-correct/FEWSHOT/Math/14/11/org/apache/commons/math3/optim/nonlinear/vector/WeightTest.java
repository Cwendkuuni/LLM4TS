package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightDiagonal() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();

        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrix() {
        double[] weights = {1.0, 2.0};
        new Weight(weights);
    }

    @Test
    public void testWeightCopy() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = new Array2DRowRealMatrix(3, 3).copy();
        RealMatrix actual = weight.getWeight().copy();

        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testWeightNull() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }
}