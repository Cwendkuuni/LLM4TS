package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight w1 = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = w1.getWeight();
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareConstruction() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 2);
        Weight w1 = new Weight(weightMatrix);
    }

    @Test
    public void testWeightSquareConstruction() {
        RealMatrix weight = new Array2DRowRealMatrix(new Array2DRowRealMatrix(3, 3).transpose());
        Weight w1 = new Weight(weight);
        RealMatrix expected = weight.copy();

        RealMatrix actual = w1.getWeight();
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test
    public void testWeightGetNull() {
        Weight weight = null;
        assertNull(weight.getWeight());
    }
}