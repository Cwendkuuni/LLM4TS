import org.apache.commons.math.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Test
    public void testGetL() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
        assertTrue(expectedL.equals(cholesky.getL()));
    }

    @Test
    public void testGetLT() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8}, {0, 1, 5}, {0, 0, 3}});
        assertTrue(expectedLT.equals(cholesky.getLT()));
    }

    @Test
    public void testGetDeterminant() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(118, cholesky.getDeterminant(), 1e-10);
    }

    @Test
    public void testGetSolver() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        assertTrue(solver instanceof CholeskyDecompositionImpl.Solver);
    }

    @Test
    public void testSolve() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{4, 8, -8}, false);
        RealVector expectedX = new RealVectorImpl(new double[]{1, 2, -2}, false);
        assertTrue(expectedX.equals(cholesky.solve(b)));
    }

    @Test
    public void testGetInverse() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(new double[][]{{1, -2, 1}, {-2, 4, -2}, {1, -2, 2}});
        assertTrue(expectedInverse.equals(cholesky.getInverse()));
    }

}