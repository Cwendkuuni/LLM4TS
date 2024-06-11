package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testDiagonalWeightConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testDiagonalWeightSetDiagonal() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        weight.getWeight().setEntry(1, 1, 0.5);
        double[] expected = new double[]{1.0, 0.5, 3.0};
        RealMatrix actual = weight.getWeight();
        assertArrayEquals(expected, actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSquareMatrixConstructionFailed() {
        double[][] weights = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weight = new Weight(weights);
    }

    @Test
    public void testSquareMatrixWeightConstruction() {
        double[][] weights = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testSquareMatrixWeightSetEntry() {
        double[][] weights = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        weight.getWeight().setEntry(0, 0, 0.5);
        double[][] expected = {{0.5, 0.0}, {0.0, 1.0}};
        double[][] actual = weight.getWeight().getData();
        assertArrayEquals(expected, actual, 0.0);
    }
}