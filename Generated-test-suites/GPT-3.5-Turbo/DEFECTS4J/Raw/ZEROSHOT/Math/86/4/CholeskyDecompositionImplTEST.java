import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.linear.*;

import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl choleskyDecomposition;

    // Test matrix
    private double[][] matrix = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
    };

    @Before
    public void setUp() {
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(realMatrix);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            fail("Failed to create Cholesky decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testGetL() {
        double[][] expectedLTData = {
                {2, 0, 0},
                {6, 1, 0},
                {-8, 5, 3}
        };
        RealMatrix expectedL = MatrixUtils.createRealMatrix(expectedLTData);
        RealMatrix actualL = choleskyDecomposition.getL();
        assertArrayEquals(expectedL.getData(), actualL.getData());
    }

    @Test
    public void testGetLT() {
        double[][] expectedLTData = {
                {2, 6, -8},
                {0, 1, 5},
                {0, 0, 3}
        };
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(expectedLTData);
        RealMatrix actualLT = choleskyDecomposition.getLT();
        assertArrayEquals(expectedLT.getData(), actualLT.getData());
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 442;
        double actualDeterminant = choleskyDecomposition.getDeterminant();
        assertEquals(expectedDeterminant, actualDeterminant, 0.00001);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolve() {
        double[] b = {3, 5, 7};
        double[] expectedSolution = {1, -1, 2};
        double[] actualSolution = null;
        try {
            actualSolution = choleskyDecomposition.getSolver().solve(b);
        } catch (IllegalArgumentException | InvalidMatrixException e) {
            fail("Failed to solve linear equation: " + e.getMessage());
        }
        assertArrayEquals(expectedSolution, actualSolution, 0.00001);
    }

    @Test
    public void testSolveRealVector() {
        double[] b = {3, 5, 7};
        double[] expectedSolution = {1, -1, 2};
        RealVector expectedVector = new RealVectorImpl(expectedSolution);
        RealVector actualVector = null;
        try {
            actualVector = choleskyDecomposition.getSolver().solve(new RealVectorImpl(b));
        } catch (IllegalArgumentException | InvalidMatrixException e) {
            fail("Failed to solve linear equation: " + e.getMessage());
        }
        assertArrayEquals(expectedVector.getData(), actualVector.getData(), 0.00001);
    }

    @Test
    public void testSolveRealMatrix() {
        double[][] b = {
                {3, 4},
                {5, 6},
                {7, 8}
        };
        double[][] expectedSolution = {
                {1, 0},
                {-1, 0},
                {2, 0}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedSolution);
        RealMatrix actualMatrix = null;
        try {
            actualMatrix = choleskyDecomposition.getSolver().solve(MatrixUtils.createRealMatrix(b));
        } catch (IllegalArgumentException | InvalidMatrixException e) {
            fail("Failed to solve linear equation: " + e.getMessage());
        }
        assertArrayEquals(expectedMatrix.getData(), actualMatrix.getData(), 0.00001);
    }

    @Test
    public void testGetInverse() {
        double[][] expectedInverseData = {
                {0.25, -0.083333, 0.0},
                {-0.083333, 0.0625, -0.020833},
                {0.0, -0.020833, 0.034722}
        };
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(expectedInverseData);
        RealMatrix actualInverse = null;
        try {
            actualInverse = choleskyDecomposition.getSolver().getInverse();
        } catch (InvalidMatrixException e) {
            fail("Failed to get inverse: " + e.getMessage());
        }
        assertArrayEquals(expectedInverse.getData(), actualInverse.getData(), 0.00001);
    }

}