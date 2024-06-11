
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double[][] matrixData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomp.getRootMatrix());
    }

    @Test
    public void testConstructorWithSmall() {
        double[][] matrixData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.01);
        assertNotNull(decomp.getRootMatrix());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{1, 2, 3}, {2, 5, 6}, {3, 6, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        assertEquals(matrix.getRowDimension(), decomp.getRootMatrix().getRowDimension());
        assertEquals(decomp.getRank(), decomp.getRootMatrix().getColumnDimension());
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, decomp.getRank());
    }
}
