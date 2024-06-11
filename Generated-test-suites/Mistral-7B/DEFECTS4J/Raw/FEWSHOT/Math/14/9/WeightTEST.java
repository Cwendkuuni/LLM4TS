package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);

        RealMatrix weightMatrix = weightObj.getWeight();

        RealVector vector1 = weightMatrix.getColumnVector(0);
        RealVector vector2 = weightMatrix.getColumnVector(1);
        RealVector vector3 = weightMatrix.getColumnVector(2);

        assertEquals(3.0, vector1.getEntry(0), 0.0);
        assertEquals(0.0, vector1.getEntry(1), 0.0);
        assertEquals(0.0, vector1.getEntry(2), 0.0);

        assertEquals(0.0, vector2.getEntry(0), 0.0);
        assertEquals(2.0, vector2.getEntry(1), 0.0);
        assertEquals(0.0, vector2.getEntry(2), 0.0);

        assertEquals(0.0, vector3.getEntry(0), 0.0);
        assertEquals(0.0, vector3.getEntry(1), 0.0);
        assertEquals(3.0, vector3.getEntry(2), 0.0);
    }

    @Test
    public void testWeightSquareMatrix() {
        double[][] weight = {{1.0, 0.5}, {0.5, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));

        RealMatrix weightMatrix = weightObj.getWeight();

        RealVector vector1 = weightMatrix.getColumnVector(0);
        RealVector vector2 = weightMatrix.getColumnVector(1);

        assertEquals(1.0, vector1.getEntry(0), 0.0);
        assertEquals(0.5, vector1.getEntry(1), 0.0);

        assertEquals(0.5, vector2.getEntry(0), 0.0);
        assertEquals(1.0, vector2.getEntry(1), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrix() {
        double[][] weight = {{1.0, 0.5}, {0.5, 1.0, 2.0}};
        new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightNull() {
        Weight weightObj = new Weight(null);
        assertNull(weightObj.getWeight());
    }
}