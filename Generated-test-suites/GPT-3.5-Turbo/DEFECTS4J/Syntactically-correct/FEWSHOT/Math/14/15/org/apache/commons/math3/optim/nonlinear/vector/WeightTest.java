package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeight_diagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertTrue(weight.getWeight().equals(expectedMatrix));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeight_nonSquareMatrixConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        new Weight(weight);
    }

    @Test
    public void testWeight_squareMatrixConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(2, 2);
        Weight w = new Weight(weight);
        assertTrue(w.getWeight().equals(weight));
    }
}