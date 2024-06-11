import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl decomp;

    @Before
    public void setUp() {
        double[][] matrixData = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomp = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {
                {2, 0, 0},
                {6, 1, 0},
                {-8, 5, 3}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(decomp.getL().equals(expected));
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {
                {2, 6, -8},
                {0, 1, 5},
                {0, 0, 3}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(decomp.getLT().equals(expected));
    }

    @Test
    public void testGetDeterminant() {
        double expected = 396;
        assertEquals(expected, decomp.getDeterminant(), 0);
    }

    @Test
    public void testGetSolver() {
        assertTrue(decomp.getSolver() instanceof CholeskyDecompositionImpl.Solver);
    }

    @Test
    public void testSolverIsNonSingular() {
        assertTrue(decomp.getSolver().isNonSingular());
    }

    @Test
    public void testSolve() {
        double[] b = {1, 2, 3};
        double[] expected = {0.125, -0.25, 1};
        double[] x = decomp.getSolver().solve(b);
        for (int i = 0; i < x.length; i++) {
            assertEquals(expected[i], x[i], 0);
        }
    }

    @Test
    public void testSolveWithRealVector() {
        double[] b = {1, 2, 3};
        double[] expected = {0.125, -0.25, 1};
        RealVector bVector = new ArrayRealVector(b);
        RealVector expectedVector = new ArrayRealVector(expected);
        RealVector xVector = decomp.getSolver().solve(bVector);
        assertTrue(xVector.equals(expectedVector));
    }

    @Test
    public void testSolveWithRealMatrix() {
        double[][] bData = {{1, 4}, {2, 5}, {3, 6}};
        double[][] expectedData = {{0.125, -0.25}, {-1.875, 0.625}, {3.125, -0.875}};
        RealMatrix bMatrix = MatrixUtils.createRealMatrix(bData);
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix xMatrix = decomp.getSolver().solve(bMatrix);
        assertTrue(xMatrix.equals(expectedMatrix));
    }

    @Test
    public void testGetInverse() {
        double[][] expectedData = {
                {0.125, -0.25, 0.0},
                {-0.25, 0.625, 0.125},
                {0.0, 0.125, 0.375}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(decomp.getSolver().getInverse().equals(expected));
    }
}