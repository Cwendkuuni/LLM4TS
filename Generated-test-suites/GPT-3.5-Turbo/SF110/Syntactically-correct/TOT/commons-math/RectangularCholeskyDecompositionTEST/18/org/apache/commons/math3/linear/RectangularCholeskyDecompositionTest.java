import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_ThrowsNonPositiveDefiniteMatrixException() {
        // Test when the matrix has at least one non-positive diagonal element
        double[][] matrixData = {{1, 0}, {0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition rc = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException was not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1, e.getIndex());
            assertEquals(1, e.getSmall(), 0);
        }
    }

    @Test
    public void testConstructor_ThrowsNonPositiveDefiniteMatrixException_WhenSmallIsNonZero() {
        // Test when the matrix has at least one non-positive diagonal element and small is non-zero
        double[][] matrixData = {{2, 1}, {1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition rc = new RectangularCholeskyDecomposition(matrix, 0.1);
            fail("Expected NonPositiveDefiniteMatrixException was not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(0, e.getIndex());
            assertEquals(0.1, e.getSmall(), 0);
        }
    }

    @Test
    public void testConstructor_CreatesCholeskyDecomposition_WhenMatrixIsPositiveDefinite() {
        // Test when the matrix is positive definite
        double[][] matrixData = {{4, 1}, {1, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        RectangularCholeskyDecomposition rc = new RectangularCholeskyDecomposition(matrix);

        assertEquals(2, rc.getRank());
        RealMatrix rootMatrix = rc.getRootMatrix();
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 0);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0);
        assertEquals(0.25, rootMatrix.getEntry(1, 0), 0);
        assertEquals(2.20, rootMatrix.getEntry(1, 1), 0.01);
    }

    @Test
    public void testGetRootMatrix_ReturnsRootMatrix() {
        // Test when the root matrix is correctly returned
        double[][] matrixData = {{4, 1}, {1, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        RectangularCholeskyDecomposition rc = new RectangularCholeskyDecomposition(matrix);

        RealMatrix rootMatrix = rc.getRootMatrix();

        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 0);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0);
        assertEquals(0.25, rootMatrix.getEntry(1, 0), 0);
        assertEquals(2.20, rootMatrix.getEntry(1, 1), 0.01);
    }

    @Test
    public void testGetRank_ReturnsRank() {
        // Test when the rank is correctly returned
        double[][] matrixData = {{4, 1}, {1, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        RectangularCholeskyDecomposition rc = new RectangularCholeskyDecomposition(matrix);

        int rank = rc.getRank();

        assertEquals(2, rank);
    }

}