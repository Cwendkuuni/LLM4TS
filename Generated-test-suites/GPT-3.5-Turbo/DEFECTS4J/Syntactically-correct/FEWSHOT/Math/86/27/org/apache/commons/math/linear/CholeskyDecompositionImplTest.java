import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.RealVectorImpl;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl decomposition;

    @Before
    public void setUp() throws Exception {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix L = decomposition.getL();
        assertTrue(MatrixUtils.isEqual(expectedMatrix, L, 1e-10));
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix LT = decomposition.getLT();
        assertTrue(MatrixUtils.isEqual(expectedMatrix, LT, 1e-10));
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 441;
        double determinant = decomposition.getDeterminant();
        assertEquals(expectedDeterminant, determinant, 1e-10);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = decomposition.getSolver();
        assertTrue(solver instanceof CholeskyDecompositionImpl.Solver);
    }

    @Test
    public void testSolveVector() {
        double[] b = {1, 2, 3};
        double[] expectedX = {0.25, 0.5, 0.75};
        RealVector xVector = decomposition.solve(new RealVectorImpl(b));
        double[] x = xVector.getData();
        assertArrayEquals(expectedX, x, 1e-10);
    }

    @Test
    public void testSolveMatrix() {
        double[][] bData = {{1, 2, 3}, {4, 5, 6}};
        double[][] expectedXData = {{0.25, 0.5, 0.75}, {0.5, -1, 2.25}};
        RealMatrix b = MatrixUtils.createRealMatrix(bData);
        RealMatrix expectedX = MatrixUtils.createRealMatrix(expectedXData);
        RealMatrix xMatrix = decomposition.solve(b);
        assertTrue(MatrixUtils.isEqual(expectedX, xMatrix, 1e-10));
    }

    @Test
    public void testGetInverse() {
        double[][] expectedInverseData = {{0.25, 0.5, 0.75}, {0.5, -1, 2.25}, {-0.75, 2.25, -3.25}};
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(expectedInverseData);
        RealMatrix inverse = decomposition.getInverse();
        assertTrue(MatrixUtils.isEqual(expectedInverse, inverse, 1e-10));
    }
}