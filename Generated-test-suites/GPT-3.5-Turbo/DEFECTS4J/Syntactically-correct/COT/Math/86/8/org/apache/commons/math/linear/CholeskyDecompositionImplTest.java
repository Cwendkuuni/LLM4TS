import org.apache.commons.math.linear.*;
import org.apache.commons.math.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expected.equals(cholesky.getL()));
    }

    @Test
    public void testGetLT() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double[][] expectedData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(expected.equals(cholesky.getLT()));
    }

    @Test
    public void testGetDeterminant() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(4.0, cholesky.getDeterminant(), 1e-15);
    }

    @Test
    public void testGetSolver() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        double[] b = {1, 3, -4};
        double[] expected = {-3, 5, -2};
        assertArrayEquals(expected, solver.solve(b), 1e-15);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] testData = {{4, 12, -16}, {15, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] testData = {{-4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        new CholeskyDecompositionImpl(matrix);
    }

}