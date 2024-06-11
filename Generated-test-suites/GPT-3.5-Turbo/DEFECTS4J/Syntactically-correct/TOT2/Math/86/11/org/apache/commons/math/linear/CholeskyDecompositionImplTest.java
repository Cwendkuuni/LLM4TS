import static org.junit.Assert.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testDefaultConstructor() {
        // Create a square matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        
        // Test default constructor
        try {
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            assertNotNull(cholesky);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeterminant() {
        // Create a square matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        
        // Test determinant calculation
        try {
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            double determinant = cholesky.getDeterminant();
            assertEquals(432, determinant, 1e-15);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSolver() {
        // Create a square matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        
        // Test solver
        try {
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = cholesky.getSolver();
            RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
            RealVector x = solver.solve(b);
            double[] expected = new double[]{0.025, 0.05, 0.075};
            assertArrayEquals(expected, x.getData(), 1e-15);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }
}