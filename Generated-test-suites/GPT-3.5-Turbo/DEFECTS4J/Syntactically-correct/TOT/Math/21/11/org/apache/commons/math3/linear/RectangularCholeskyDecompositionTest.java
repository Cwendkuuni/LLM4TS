import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        // Typical use case: Decompose a valid symmetric positive semidefinite matrix
        double[][] matrixData = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double tolerance = 1e-10;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, tolerance);
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();

        // Check the dimensions of the root matrix
        assertEquals(matrix.getRowDimension(), root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        // Check the matrix multiplication result
        RealMatrix product = root.multiply(root.transpose());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), product.getEntry(i, j), tolerance);
            }
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidMatrix() {
        // Error scenario: Decompose an invalid matrix with negative diagonal elements
        double[][] matrixData = {{2, -1}, {-1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double tolerance = 1e-10;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, tolerance);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidSmallThreshold() {
        // Error scenario: Decompose a matrix with a small threshold that results in a non-positive definite matrix
        double[][] matrixData = {{2, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double tolerance = 1e-12;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, tolerance);
    }

    @Test
    public void testEdgeCase() {
        // Edge case: Decompose a matrix with zero diagonal elements
        double[][] matrixData = {{0, 1}, {1, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double tolerance = 1e-10;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, tolerance);
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();

        // Check the dimensions of the root matrix
        assertEquals(matrix.getRowDimension(), root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        // Check the matrix multiplication result
        RealMatrix product = root.multiply(root.transpose());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), product.getEntry(i, j), tolerance);
            }
        }
    }

}