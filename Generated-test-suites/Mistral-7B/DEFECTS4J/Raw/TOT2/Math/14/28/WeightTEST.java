package org.junit.tests;

import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {
    @Test
    public void testWeightConstructionWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData());
    }

    @Test
    public void testWeightConstructionWithMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = weight;
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightConstructionWithNullArray() {
        double[] weight = null;
        Weight weightObject = new Weight(weight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructionWithNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weightObject = new Weight(weight);
    }

    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weightObject.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData());
    }

    @Test
    public void testGetWeightWithMatrixInput() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        Weight weightObject = new Weight(weight);
        RealMatrix actualWeight = weightObject.getWeight();
        RealMatrix expectedWeight = weight;
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testGetWeightCopy() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weight);
        RealMatrix actualWeight = weightObject.getWeight();
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData());
    }

    @Test
    public void testGetWeightNullInput() {
        Weight weightObject = new Weight(null);
        assertNull(weightObject.getWeight());
    }
}