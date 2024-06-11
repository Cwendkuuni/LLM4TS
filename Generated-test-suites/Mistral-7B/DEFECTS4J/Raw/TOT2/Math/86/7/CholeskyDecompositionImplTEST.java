import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.4472, 1.0, 0.0},
                {0.2711, 0.8165, 1.0}});

        RealMatrix actualL = choleskyDecomposition.getL();

        // Check that L is equal to the expected result
        assertEquals(expectedL, actualL);

        // Test getLT() method
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.4472, 0.2711},
                {2.0, 1.0, 0.8165},
                {3.0, 6.0, 1.0}});

        RealMatrix actualLT = choleskyDecomposition.getLT();

        // Check that LT is equal to the expected result
        assertEquals(expectedLT, actualLT);

        // Test getDeterminant() method
        double expectedDeterminant = 1.0 * 1.0 * 1.0;
        double actualDeterminant = choleskyDecomposition.getDeterminant();

        // Check that determinant is equal to the expected result
        assertEquals(expectedDeterminant, actualDeterminant, 1e-15);

        // Test getSolver() method
        RealVector vectorToSolve = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = choleskyDecomposition.getSolver().solve(vectorToSolve);

        double[] expectedSolution = {-1.2186, -0.3221, -0.2321};

        // Check that solution is equal to the expected result
        assertArrayEquals(expectedSolution, solution.toArray(), 1e-15);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testThrowNonSquareMatrixException() {
        double[][] matrixData = {
                {1.0, 2.0},
                {2.0, 3.0, 4.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testThrowNotSymmetricMatrixException() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 4.0},
                {3.0, 4.0, 1.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testThrowNotPositiveDefiniteMatrixException() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, -1.0, 4.0},
                {3.0, 4.0, -1.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    @Test
    public void testGetSolverForSingularMatrix() {
        double[][] matrixData = {
                {1.0, 0.0},
                {0.0, 1.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 0.001, 0.001);

        try {
            choleskyDecomposition.getSolver().solve(new ArrayRealVector(new double[]{1.0, 1.0}));
            fail();
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }
}