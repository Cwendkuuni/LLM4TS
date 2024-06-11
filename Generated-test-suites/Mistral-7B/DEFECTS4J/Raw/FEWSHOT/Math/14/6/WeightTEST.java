package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testDiagonalWeightConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testDiagonalWeightSetValues() {
        Weight weight = new Weight(new double[]{1.0, 2.0, 3.0});
        weight.setValue(0, 0, 5.0);
        weight.setValue(1, 1, 7.0);

        RealMatrix expected = RealMatrixUtils.diagonalMatrix(new double[]{5.0, 7.0, 3.0});
        RealMatrix actual = weight.getWeight();

        assertTrue(expected.equals(actual));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightConstruction() {
        double[][] weights = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        new Weight(weights);
    }

    @Test
    public void testWeightMultiplication() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix x = RealMatrixUtils.identityMatrix(3);

        RealMatrix expected = RealMatrixUtils.multiply(weight.getWeight(), x);
        RealMatrix actual = weight.multiply(x);

        assertTrue(expected.equals(actual));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMultiplication() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(2));
        RealMatrix x = RealMatrixUtils.identityMatrix(3);
        weight.multiply(x);
    }

    @Test
    public void testGetWeight() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(3));
        RealMatrix actual = weight.getWeight();

        RealMatrix expected = RealMatrixUtils.identityMatrix(3);

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetWeightNullInput() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(3));
        assertNull(weight.getWeight(null));
    }

    @Test
    public void testGetWeightEmptyInput() {
        Weight weight = new Weight(new double[0][0]);
        assertNull(weight.getWeight());
    }

    @Test
    public void testSetValue() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(3));
        weight.setValue(1, 1, 5.0);

        double[][] expected = {{1.0, 0.0, 0.0}, {0.0, 5.0, 0.0}, {0.0, 0.0, 1.0}};
        double[][] actual = weight.getWeight().getData();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSetValueOutOfBounds() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(3));

        assertFalse(weight.setValue(3, 3, 5.0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetValueIndexOutOfBounds() {
        Weight weight = new Weight(RealMatrixUtils.identityMatrix(3));
        weight.setValue(3, 0, 5.0);
    }
}