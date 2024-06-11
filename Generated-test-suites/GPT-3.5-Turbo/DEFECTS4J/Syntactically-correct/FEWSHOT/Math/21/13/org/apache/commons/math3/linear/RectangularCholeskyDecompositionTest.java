package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_negativeDiagonalElement() {
        double[][] matrixData = {
            {1, 0},
            {0, -1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0001);
    }

    @Test
    public void testRectangularCholeskyDecomposition_equalDiagonalElements() {
        double[][] matrixData = {
            {1, 0},
            {0, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0001);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(matrix, root.multiply(root.transpose()));
        assertEquals(2, decomp.getRank());
    }

    @Test
    public void testRectangularCholeskyDecomposition_differentDiagonalElements() {
        double[][] matrixData = {
            {4, 0},
            {0, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0001);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(matrix, root.multiply(root.transpose()));
        assertEquals(2, decomp.getRank());
    }

    @Test
    public void testRectangularCholeskyDecomposition_dependentColumns() {
        double[][] matrixData = {
            {4, 2},
            {2, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0001);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(matrix, root.multiply(root.transpose()));
        assertEquals(1, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_invalidPositiveSemidefiniteMatrix() {
        double[][] matrixData = {
            {4, -2},
            {-2, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0001);
    }

}