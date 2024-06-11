package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testDiagonalWeightConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, weights[0]);
        expectedWeight.setEntry(1, 1, weights[1]);
        expectedWeight.setEntry(2, 2, weights[2]);

        RealMatrix actualWeight = weight.getWeight();

        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testSquareMatrixWeightConstructor() {
        double[][] weightMatrixData = {
                {1.0, 0.5},
                {0.5, 1.5}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weight = new Weight(weightMatrix);

        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weight.getWeight();

        assertEquals(weightMatrix.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(weightMatrix.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(weightMatrix.getData(), actualWeight.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixWeightConstructor() {
        double[][] weightMatrixData = {
                {1.0, 0.5},
                {0.5}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        new Weight(weightMatrix);
    }

    @Test
    public void testNullWeightConstructor() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }
}