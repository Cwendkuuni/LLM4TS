import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.*;
import org.junit.Test;


public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix root = cholesky.getRootMatrix();
        assertEquals(root.getRowDimension(), 3);
        assertEquals(root.getColumnDimension(), 3);

        double[][] expectedRootData = {{2, 6, -8}, {0, 3, 5}, {0, 0, 3}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
        assertTrue(expectedRoot.subtract(root).getNorm() < 1e-10);
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 1e-10);

        int rank = cholesky.getRank();
        assertEquals(rank, 3);
    }

    // Edge cases and exception handling scenarios

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, -98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    // Add more test cases for edge cases and exception handling scenarios

}