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
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(weight);
        new Weight(matrix);
    }

    @Test
    public void testWeightMatrixCopy() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(weight);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightGetNull() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }
}