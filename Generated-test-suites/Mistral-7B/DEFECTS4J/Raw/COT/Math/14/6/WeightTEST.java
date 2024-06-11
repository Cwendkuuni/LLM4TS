package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightImpl;
import org.apache.commons.math3.optim.PointValuePair;

import org.junit.Test;

/**
 * Test class for Weight.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = RealMatrixUtils.identityMatrix(3);
        RealMatrix actualWeight = weightObj.getWeight();
        assertMatrixEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix weight = RealMatrixUtils.identityMatrix(3);
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertMatrixEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix weight = RealMatrixUtils.create(new double[][] { {1.0, 2.0}, {3.0, 4.0} });
        new Weight(weight);
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeight() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = RealMatrixUtils.identityMatrix(3);
        RealMatrix actualWeight = weightObj.getWeight();
        assertMatrixEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with RealMatrix.
     */
    @Test
    public void testGetWeightWithRealMatrix() {
        RealMatrix weight = RealMatrixUtils.identityMatrix(3);
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertMatrixEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWeightWithNull() {
        Weight weightObj = new Weight(new double[0]);
        weightObj.getWeight();
    }

    /**
     * Test equals method with same instance.
     */
    @Test
    public void testEqualsWithSameInstance() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj1 = new Weight(weight);
        Weight weightObj2 = weightObj1;
        assertTrue(weightObj1.equals(weightObj2));
    }

    /**
     * Test equals method with different instance.
     */
    @Test
    public void testEqualsWithDifferentInstance() {
        double[] weight1 = new double[] { 1.0, 2.0, 3.0 };
        double[] weight2 = new double[] { 1.0, 2.0, 4.0 };
        Weight weightObj1 = new Weight(weight1);
        Weight weightObj2 = new Weight(weight2);
        assertFalse(weightObj1.equals(weightObj2));
    }

    /**
     * Test equals method with different type.
     */
    @Test
    public void testEqualsWithDifferentType() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj = new Weight(weight);
        PointValuePair pointValuePair = new PointValuePair(new double[0], 0.0);
        assertFalse(weightObj.equals(pointValuePair));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj1 = new Weight(weight);
        Weight weightObj2 = new Weight(weight);
        int hashCode1 = weightObj1.hashCode();
        int hashCode2 = weightObj2.hashCode();
        assertTrue(hashCode1 == hashCode2);
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() throws CloneNotSupportedException {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj = new Weight(weight);
        Weight clonedWeightObj = (Weight) weightObj.clone();
        RealMatrix expectedWeight = RealMatrixUtils.identityMatrix(3);
        RealMatrix actualWeight1 = weightObj.getWeight();
        RealMatrix actualWeight2 = clonedWeightObj.getWeight();
        assertMatrixEquals(expectedWeight, actualWeight1);
        assertMatrixEquals(expectedWeight, actualWeight2);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        double[] weight = new double[] { 1.0, 2.0, 3.0 };
        Weight weightObj = new Weight(weight);
        String expectedString = "Weight[weightMatrix=IdentityMatrix(3)]";
        String actualString = weightObj.toString();
        assertEquals(expectedString, actualString);
    }

    /**
     * Helper method for equal matrices comparisons.
     *
     * @param expectedMatrix The expected matrix.
     * @param actualMatrix The actual matrix.
     */
    private void assertMatrixEquals(RealMatrix expectedMatrix, RealMatrix actualMatrix) {
        if (!expectedMatrix.equals(actualMatrix)) {
            throw new AssertionError("Expected:\n" + expectedMatrix + "\n"
                    + "  but was:\n" + actualMatrix);
        }
    }

    /**
     * Test method for {@link Weight#Weight(double[])}.
     */
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weights = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
        Weight weight = new Weight(weights);
        Weight expectedWeight = new WeightImpl(new Array2DRowRealMatrix(3, 3, weights));
        assertEquals(expectedWeight, weight);
    }

    /**
     * Test method for {@link Weight#Weight(RealMatrix)}.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[] weights = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3, weights);
        Weight weight = new Weight(weightMatrix);
        Weight expectedWeight = new WeightImpl(new Array2DRowRealMatrix(3, 3, weights));
        assertEquals(expectedWeight, weight);
    }

    /**
     * Test method for {@link Weight#Weight(RealMatrix)}.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareRealMatrix() {
        double[] weights = new double[] { 1.0, 2.0, 3.0, 4.0 };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 4, weights);
        new Weight(weightMatrix);
    }
}