package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        assertArrayEquals(expected.getData(), weight.getWeight().getData(), 0.0);
    }

    @Test
    public void testWeightNonSquareMatrixException() {
        double[] weights = {1.0, 2.0};
        try {
            new Weight(new Array2DRowRealMatrix(3, 2, new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
            }));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        assertArrayEquals(expected.getData(), weight.getWeight().getData(), 0.0);
    }

    @Test
    public void testWeightCopy() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(weights.length, weights.length, weights);
        RealMatrix actual = weight.getWeight().copy();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }
}