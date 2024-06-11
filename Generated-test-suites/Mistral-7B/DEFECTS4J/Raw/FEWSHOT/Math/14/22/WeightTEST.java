package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = weightObj.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = weightObj.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
    }

    @Test
    public void testWeightGetWeightNull() {
        Weight weightObj = new Weight(new Array2DRowRealMatrix(2, 2));
        assertNull(weightObj.getWeight());
    }

    @Test
    public void testWeightGetWeightNotNull() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix actual = weightObj.getWeight();
        assertNotNull(actual);
    }

    @Test
    public void testWeightEquals() {
        double[][] weight1 = {{1.0, 0.0}, {0.0, 2.0}};
        double[][] weight2 = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weight1Matrix = new Array2DRowRealMatrix(weight1);
        RealMatrix weight2Matrix = new Array2DRowRealMatrix(weight2);
        Weight weight1Obj = new Weight(weight1Matrix);
        Weight weight2Obj = new Weight(weight2Matrix);
        assertTrue(weight1Obj.equals(weight2Obj));
    }

    @Test
    public void testWeightNotEquals() {
        double[][] weight1 = {{1.0, 0.0}, {0.0, 2.0}};
        double[][] weight2 = {{1.0, 0.0}, {0.0, 3.0}};
        RealMatrix weight1Matrix = new Array2DRowRealMatrix(weight1);
        RealMatrix weight2Matrix = new Array2DRowRealMatrix(weight2);
        Weight weight1Obj = new Weight(weight1Matrix);
        Weight weight2Obj = new Weight(weight2Matrix);
        assertFalse(weight1Obj.equals(weight2Obj));
    }

    @Test
    public void testWeightHashCode() {
        double[][] weight1 = {{1.0, 0.0}, {0.0, 2.0}};
        double[][] weight2 = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weight1Matrix = new Array2DRowRealMatrix(weight1);
        RealMatrix weight2Matrix = new Array2DRowRealMatrix(weight2);
        Weight weight1Obj = new Weight(weight1Matrix);
        Weight weight2Obj = new Weight(weight2Matrix);
        int weight1HashCode = weight1Obj.hashCode();
        int weight2HashCode = weight2Obj.hashCode();
        assertTrue(weight1HashCode == weight2HashCode);
    }
}