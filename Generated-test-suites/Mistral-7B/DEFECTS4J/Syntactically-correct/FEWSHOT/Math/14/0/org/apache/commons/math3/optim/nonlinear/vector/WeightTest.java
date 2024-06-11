package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weightObject.getWeight();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixException() {
        double[] weights = {1.0, 2.0};
        new Weight(weights);
    }

    @Test
    public void testWeightMatrixCopy() {
        RealMatrix input = new Array2DRowRealMatrix(3, 3);
        input.setEntry(0, 0, 1.0);
        input.setEntry(1, 1, 2.0);
        input.setEntry(2, 2, 3.0);

        Weight weightObject = new Weight(input);
        RealMatrix expected = input.copy();
        RealMatrix actual = weightObject.getWeight();

        assertEquals(expected, actual);
    }
}