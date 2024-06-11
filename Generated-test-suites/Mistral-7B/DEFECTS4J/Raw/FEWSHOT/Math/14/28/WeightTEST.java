package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weights = new double[][]{
                {1.0, 0.5},
                {0.5, 2.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix;

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixConstructorThrowsException() {
        double[][] weights = new double[][]{
                {1.0, 0.5},
                {0.5, 2.0},
                {0.1, 0.2}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        new Weight(weightMatrix);
    }

    @Test
    public void testWeightGetWeight() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });

        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight.getRowDimension(), actualWeight.getRowDimension());
        assertEquals(expectedWeight.getColumnDimension(), actualWeight.getColumnDimension());
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testWeightGetWeightCopy() {
        double[][] weights = new double[][]{
                {1.0, 0.5},
                {0.5, 2.0}
        };
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights);
        Weight expectedWeightWeight = new Weight(expectedWeight);

        RealMatrix actualWeight = weight.getWeight();
        Weight actualWeightWeight = new Weight(actualWeight);

        assertEquals(expectedWeightWeight.getWeight().getRowDimension(), actualWeightWeight.getWeight().getRowDimension());
        assertEquals(expectedWeightWeight.getWeight().getColumnDimension(), actualWeightWeight.getWeight().getColumnDimension());
        assertArrayEquals(expectedWeightWeight.getWeight().getData(), actualWeightWeight.getWeight().getData(), 0.0);
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }
}