package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * WeightTest class to test Weight methods.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weightObj.getWeight();
        assertTrue(expectedWeight.equals(actualWeight));
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 2.0);
        weight.setEntry(2, 2, 3.0);

        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();

        RealMatrix actualWeight = weightObj.getWeight();
        assertTrue(expectedWeight.equals(actualWeight));
    }

    /**
     * Test constructor with non-square RealMatrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);

        new Weight(weight);
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeightMethod() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weightObj.getWeight();
        assertTrue(expectedWeight.equals(actualWeight));
    }
}