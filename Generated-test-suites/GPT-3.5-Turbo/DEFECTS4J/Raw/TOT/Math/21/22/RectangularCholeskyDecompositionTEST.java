import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private static final double SMALL_THRESHOLD = 1e-10;

    @Test
    public void testGetRootMatrix() {
        RealMatrix inputMatrix = createPositiveSemidefiniteMatrix();
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(inputMatrix, SMALL_THRESHOLD);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        // Verify that the root matrix has the correct number of rows and columns
        assertEquals(inputMatrix.getRowDimension(), rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());

        // Verify that the root matrix is a valid Cholesky decomposition (A = BB^T)
        RealMatrix product = rootMatrix.multiply(rootMatrix.transpose());
        assertEquals(inputMatrix.getRowDimension(), product.getRowDimension());
        assertEquals(inputMatrix.getRowDimension(), product.getColumnDimension());
        assertTrue(inputMatrix.subtract(product).isZero(SMALL_THRESHOLD));
    }

    @Test
    public void testGetRank() {
        RealMatrix inputMatrix = createPositiveSemidefiniteMatrix();
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(inputMatrix, SMALL_THRESHOLD);
        int rank = decomposition.getRank();

        // Verify that the rank is less than or equal to the dimension of the input matrix
        assertTrue(rank <= inputMatrix.getRowDimension());

        // Verify that the rank is equal to the number of positive eigenvalues (diagonal elements) of the input matrix
        int positiveEigenvalues = countPositiveEigenvalues(inputMatrix);
        assertEquals(positiveEigenvalues, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        RealMatrix inputMatrix = createNonPositiveDefiniteMatrix();
        new RectangularCholeskyDecomposition(inputMatrix, SMALL_THRESHOLD);
    }

    private RealMatrix createPositiveSemidefiniteMatrix() {
        return MatrixUtils.createRealMatrix(new double[][] {
                { 4, 1, 1 },
                { 1, 5, -1 },
                { 1, -1, 6 }
        });
    }

    private RealMatrix createNonPositiveDefiniteMatrix() {
        return MatrixUtils.createRealMatrix(new double[][] {
                { 4, 1, 1 },
                { 1, 5, -1 },
                { 1, -1, -6 }
        });
    }

    private int countPositiveEigenvalues(RealMatrix matrix) {
        int count = 0;
        for (double eigenvalue : matrix.getDiagonal()) {
            if (eigenvalue > 0) {
                count++;
            }
        }
        return count;
    }
}