package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testConstructor() {
        // Test diagonal weight matrix with positive values
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        Array2DRowRealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertEquals(expectedMatrix, w.getWeight());

        // Test diagonal weight matrix with negative values
        double[] weight2 = {-1.0, -2.0, -3.0};
        Weight w2 = new Weight(weight2);
        Array2DRowRealMatrix expectedMatrix2 = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0, 0.0}, {0.0, -2.0, 0.0}, {0.0, 0.0, -3.0}});
        assertEquals(expectedMatrix2, w2.getWeight());

        // Test square weight matrix
        double[][] weight3 = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Array2DRowRealMatrix weightMatrix3 = new Array2DRowRealMatrix(weight3);
        Weight w3 = new Weight(weightMatrix3);
        assertEquals(weightMatrix3, w3.getWeight());

        // Test non-square weight matrix
        double[][] weight4 = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        Array2DRowRealMatrix weightMatrix4 = new Array2DRowRealMatrix(weight4);
        try {
            new Weight(weightMatrix4);
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }

}