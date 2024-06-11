import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        // Create the matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the L matrix
        RealMatrix lMatrix = decomposition.getL();

        // Check that the L matrix is correct
        assertEquals(2, lMatrix.getRowDimension());
        assertEquals(2, lMatrix.getColumnDimension());
        assertEquals(Math.sqrt(4), lMatrix.getEntry(0, 0), 0.0001);
        assertEquals(Math.sqrt(37 - 12 * 12 / 4), lMatrix.getEntry(1, 0), 0.0001);
        assertEquals(Math.sqrt(98 - 16 * 16 / 4 - 43 * 43 / 37), lMatrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetLT() {
        // Create the matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the LT matrix
        RealMatrix ltMatrix = decomposition.getLT();

        // Check that the LT matrix is correct
        assertEquals(2, ltMatrix.getRowDimension());
        assertEquals(2, ltMatrix.getColumnDimension());
        assertEquals(Math.sqrt(4), ltMatrix.getEntry(0, 0), 0.0001);
        assertEquals(Math.sqrt(37 - 12 * 12 / 4), ltMatrix.getEntry(0, 1), 0.0001);
        assertEquals(Math.sqrt(98 - 16 * 16 / 4 - 43 * 43 / 37), ltMatrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetDeterminant() {
        // Create the matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the determinant
        double determinant = decomposition.getDeterminant();

        // Check that the determinant is correct
        assertEquals(16, determinant, 0.0001);
    }

    @Test
    public void testGetSolver() {
        // Create the matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        // Create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the solver
        DecompositionSolver solver = decomposition.getSolver();

        // Check that the solver is not null
        assertNotNull(solver);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrixException() {
        // Create a non-square matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}};

        // Try to create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorNotSymmetricMatrixException() {
        // Create a non-symmetric matrix
        double[][] matrixData = {{4, 12, -16}, {8, 37, -43}, {-16, -43, 98}};

        // Try to create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorNotPositiveDefiniteMatrixException() {
        // Create a not positive definite matrix
        double[][] matrixData = {{4, 4, -16}, {4, 4, -43}, {-16, -43, 98}};

        // Try to create the CholeskyDecompositionImpl object
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }
}