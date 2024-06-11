
package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
    @Test
    public void testConstructor1() {
        // Test with valid input
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = MatrixUtils.createRealDiagonalMatrix(weights);
        assertEquals(expectedMatrix, weight.getWeight());

        // Test with empty input
        double[] emptyWeights = {};
        Weight emptyWeight = new Weight(emptyWeights);
        RealMatrix expectedEmptyMatrix = MatrixUtils.createRealDiagonalMatrix(emptyWeights);
        assertEquals(expectedEmptyMatrix, emptyWeight.getWeight());
    }

    @Test
    public void testConstructor2() {
        // Test with valid input
        double[][] matrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);
        RealMatrix expectedMatrix = matrix.copy();
        assertEquals(expectedMatrix, weight.getWeight());

        // Test with non-square matrix
        double[][] nonSquareMatrixData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(nonSquareMatrixData);
        try {
            new Weight(nonSquareMatrix);
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Exception is expected
        }
    }

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = MatrixUtils.createRealDiagonalMatrix(weights);
        assertEquals(expectedMatrix, weight.getWeight());
    }
}
