import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Before
    public void setUp() {
        // Create a test matrix
        double[][] testMatrixData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix testMatrix = new RealMatrixImpl(testMatrixData);

        // Initialize Cholesky Decomposition
        cholesky = new CholeskyDecompositionImpl(testMatrix);
    }

    @Test
    public void testGetL() {
        RealMatrix L = cholesky.getL();
        // Test the lower triangular matrix properties of L
        // assert statements for L values based on decomposition calculations
        assertEquals(2.0, L.getEntry(0, 0), 0.0);
        assertEquals(0.0, L.getEntry(1, 0), 0.0);
        assertEquals(-8.0, L.getEntry(1, 1), 0.0);
        assertEquals(0.0, L.getEntry(2, 0), 0.0);
        assertEquals(5.0, L.getEntry(2, 1), 0.0);
        assertEquals(3.0, L.getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetLT() {
        RealMatrix LT = cholesky.getLT();
        // Test the transpose of L matrix
        // assert statements for LT values based on decomposition calculations
        assertEquals(2.0, LT.getEntry(0, 0), 0.0);
        assertEquals(0.0, LT.getEntry(0, 1), 0.0);
        assertEquals(0.0, LT.getEntry(0, 2), 0.0);
        assertEquals(0.0, LT.getEntry(1, 0), 0.0);
        assertEquals(-8.0, LT.getEntry(1, 1), 0.0);
        assertEquals(5.0, LT.getEntry(1, 2), 0.0);
        assertEquals(0.0, LT.getEntry(2, 0), 0.0);
        assertEquals(0.0, LT.getEntry(2, 1), 0.0);
        assertEquals(3.0, LT.getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetDeterminant() {
        double determinant = cholesky.getDeterminant();
        // Test the determinant value
        assertEquals(36.0, determinant, 0.0);
    }

    @Test
    public void testSolver() {
        double[] b = {1, 2, 3};
        double[] solution = cholesky.getSolver().solve(b);
        // Test the solution vector
        assertArrayEquals(new double[]{0.25, -0.125, 0.125}, solution, 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] nonSquareMatrixData = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix nonSquareMatrix = new RealMatrixImpl(nonSquareMatrixData);
        new CholeskyDecompositionImpl(nonSquareMatrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] notSymmetricMatrixData = {{1, 2, 3}, {2, 4, 5}, {3, 5, 6}};
        RealMatrix notSymmetricMatrix = new RealMatrixImpl(notSymmetricMatrixData);
        new CholeskyDecompositionImpl(notSymmetricMatrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] notPositiveDefiniteMatrixData = {{1, -1, 1}, {-1, 1, -1}, {1, -1, 1}};
        RealMatrix notPositiveDefiniteMatrix = new RealMatrixImpl(notPositiveDefiniteMatrixData);
        new CholeskyDecompositionImpl(notPositiveDefiniteMatrix);
    }

}