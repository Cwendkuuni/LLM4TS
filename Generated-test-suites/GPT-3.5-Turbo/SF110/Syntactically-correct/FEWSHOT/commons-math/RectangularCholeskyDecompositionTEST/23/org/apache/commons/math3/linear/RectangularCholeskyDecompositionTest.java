package org.apache.commons.math3.linear;

import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() {
        // Test with a valid positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, -1}, {-1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(matrix, rootMatrix.multiply(rootMatrix.transpose()));
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1_Exception() {
        // Test with a non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructor2() {
        // Test with a valid positive definite matrix and small value
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, -1}, {-1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(matrix, rootMatrix.multiply(rootMatrix.transpose()));
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2_Exception() {
        // Test with a non-positive definite matrix and small value
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
    }
}