package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * WeightTest class.
 */
public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weightObj.getWeight();
        assertTrue(expectedWeight.equals(actualWeight));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareConstructor() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightSquareConstructor() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();

        RealMatrix actualWeight = weightObj.getWeight();
        assertTrue(expectedWeight.equals(actualWeight));
    }
}