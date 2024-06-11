import org.junit.Test;
import org.apache.commons.math.linear.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        // Create matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get L matrix
        RealMatrix L = decomposition.getL();

        // Check values
        double[][] expectedLData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedL = new Array2DRowRealMatrix(expectedLData);
        assertTrue(L.equals(expectedL));
    }

    @Test
    public void testGetLT() {
        // Create matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get LT matrix
        RealMatrix LT = decomposition.getLT();

        // Check values
        double[][] expectedLTData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expectedLT = new Array2DRowRealMatrix(expectedLTData);
        assertTrue(LT.equals(expectedLT));
    }

    @Test
    public void testGetDeterminant() {
        // Create matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get determinant
        double determinant = decomposition.getDeterminant();

        // Check value
        assertEquals(576.0, determinant, 1e-10);
    }

    @Test
    public void testGetSolver() {
        // Create matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get solver
        DecompositionSolver solver = decomposition.getSolver();

        // Check solver
        assertNotNull(solver);
    }

    @Test
    public void testIsNonSingular() {
        // Create matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Check non-singularity
        assertTrue(decomposition.getSolver().isNonSingular());
    }

    @Test
    public void testSolve() {
        // Create matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Create RHS vector
        double[] b = {-40, 69, 95};

        // Solve equation Ax = b
        double[] x = decomposition.getSolver().solve(b);

        // Check solution
        double[] expectedX = {1, 2, 3};
        assertArrayEquals(expectedX, x, 1e-10);
    }

}