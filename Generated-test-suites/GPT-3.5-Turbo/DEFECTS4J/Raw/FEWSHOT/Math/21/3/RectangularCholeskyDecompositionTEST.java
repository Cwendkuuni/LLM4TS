package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test(expected=NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0},
            {0, -1}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0);
    }

    @Test
    public void testGetRootMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {4, 2},
            {2, 3}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(1, 0), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testGetRank() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {4, 2},
            {2, 3}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0);
        assertEquals(2, decomposition.getRank());
    }
}