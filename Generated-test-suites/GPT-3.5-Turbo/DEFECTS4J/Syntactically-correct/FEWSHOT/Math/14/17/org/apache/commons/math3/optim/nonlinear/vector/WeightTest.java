package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        assertEquals(3, w.getWeight().getRowDimension());
        assertEquals(3, w.getWeight().getColumnDimension());
        assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.0);
        assertEquals(2.0, w.getWeight().getEntry(1, 1), 0.0);
        assertEquals(3.0, w.getWeight().getEntry(2, 2), 0.0);
    }

    @Test
    public void testConstructorWithSquareMatrix() {
        double[][] data = {
                {1.0, 0.0},
                {0.0, 2.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
        assertEquals(2, w.getWeight().getRowDimension());
        assertEquals(2, w.getWeight().getColumnDimension());
        assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.0);
        assertEquals(2.0, w.getWeight().getEntry(1, 1), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] data = {
                {1.0, 0.0},
                {0.0, 2.0},
                {3.0, 4.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        new Weight(weightMatrix);
    }

}