import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedL = new RealMatrixImpl(expectedData);
        assertEquals(expectedL, cholesky.getL());
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double[][] expectedData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expectedLT = new RealMatrixImpl(expectedData);
        assertEquals(expectedLT, cholesky.getLT());
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double expectedDeterminant = 36;
        assertEquals(expectedDeterminant, cholesky.getDeterminant(), 0.0001);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1, 2, 3});
        RealVector expectedX = new RealVectorImpl(new double[]{0.375, -0.125, 1});
        assertEquals(expectedX, solver.solve(b));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -44}, {-16, -43, 98}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new RealMatrixImpl(new double[][]{{-4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }
}