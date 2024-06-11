
@Deprecated
package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[][] weight = { { 1.0 }, { 2.0 }, { 3.0 } };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObject = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testGetWeightMethod() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightConstructorWithEmptyWeightMatrix() {
        double[] weight = new double[0];
        Weight weightObject = new Weight(weight);
    }

    @Test
    public void testWeightConstructorWithNegativeValues() {
        double[] weight = new double[] { -1.0, 2.0, 3.0 };
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightConstructorWithNullWeightMatrix() {
        RealMatrix weightMatrix = null;
        Weight weightObject = new Weight(weightMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weight = { { 1.0 }, { 2.0, 3.0 } };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObject = new Weight(weightMatrix);
    }
}
