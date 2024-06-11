import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Before
    public void setUp() {
        RealMatrix testMatrix = new RealMatrixImpl(new double[][]{
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        });
        
        cholesky = new CholeskyDecompositionImpl(testMatrix);
    }

    @Test
    public void testGetL() {
        RealMatrix L = cholesky.getL();
        double[][] expectedLData = new double[][]{
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        };
        assertArrayEquals(expectedLData, L.getData());
    }

    @Test
    public void testGetLT() {
        RealMatrix LT = cholesky.getLT();
        double[][] expectedLTData = new double[][]{
            {2, 6, -8},
            {0, 1, 5},
            {0, 0, 3}
        };
        assertArrayEquals(expectedLTData, LT.getData());
    }

    @Test
    public void testGetDeterminant() {
        double determinant = cholesky.getDeterminant();
        double expectedDeterminant = 324;
        assertEquals(expectedDeterminant, determinant, 0.0001);
    }

    @Test
    public void testSolve() {
        double[] b = {1, 2, 3};
        double[] expectedSolution = {10, -26, 7};
        double[] solution = cholesky.getSolver().solve(b);
        assertArrayEquals(expectedSolution, solution, 0.0001);
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix b = new RealMatrixImpl(new double[][]{{1}, {2}, {3}});
        RealMatrix expectedSolution = new RealMatrixImpl(new double[][]{{10}, {-26}, {7}});
        RealMatrix solution = cholesky.getSolver().solve(b);
        assertArrayEquals(expectedSolution.getData(), solution.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveDimensionMismatch() {
        double[] b = {1, 2};
        cholesky.getSolver().solve(b);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSolveInvalidMatrix() {
        RealMatrix invalidMatrix = new RealMatrixImpl(new double[][]{{1, 2}, {3, 4}});
        cholesky.getSolver().solve(invalidMatrix);
    }
}