
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructor() {
        // Create test matrix
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Test Cholesky decomposition constructor
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertNotNull(cholesky);

        // Assert that L and LT are both valid matrices
        RealMatrix L = cholesky.getL();
        RealMatrix LT = cholesky.getLT();
        assertNotNull(L);
        assertNotNull(LT);

        // Test determinant calculation
        double determinant = cholesky.getDeterminant();
        assertEquals(3616, determinant, 0.0001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        // Create test matrix
        double[][] testData = {{4, 12, -16}, {12, 37, -43}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Test Cholesky decomposition constructor with non-square matrix
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorNotSymmetricMatrix() {
        // Create test matrix
        double[][] testData = {{4, 12, -16}, {11, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Test Cholesky decomposition constructor with non-symmetric matrix
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorNotPositiveDefiniteMatrix() {
        // Create test matrix
        double[][] testData = {{4, 12, -16}, {12, 37, -44}, {-16, -44, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Test Cholesky decomposition constructor with non-positive definite matrix
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testSolve() {
        // Create test matrix
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Test Cholesky decomposition constructor
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        // Create test vector
        double[] testVector = {1, 2, 3};
        RealVector vector = MatrixUtils.createRealVector(testVector);

        // Solve linear equation
        RealVector solution = cholesky.getSolver().solve(vector);

        // Assert that solution is correct
        double[] expectedSolution = {-0.3922, -0.3922, 0.0194};
        for (int i = 0; i < solution.getDimension(); i++) {
            assertEquals(expectedSolution[i], solution.getEntry(i), 0.0001);
        }
    }

    @Test
    public void testGetInverse() {
        // Create test matrix
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Test Cholesky decomposition constructor
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        // Calculate inverse of original matrix using solution method
        RealMatrix inverseSolution = cholesky.getSolver().getInverse();

        // Calculate inverse of original matrix using inverse method
        RealMatrix inverse = cholesky.getL().multiply(cholesky.getLT());

        // Assert that both calculated inverses are equal
        assertArrayEquals(inverse.getData(), inverseSolution.getData());
    }
}

