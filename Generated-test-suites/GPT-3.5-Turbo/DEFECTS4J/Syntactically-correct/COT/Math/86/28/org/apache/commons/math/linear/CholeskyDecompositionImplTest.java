
import org.apache.commons.math.linear.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {
    @Test
    public void testGetL() {
        // Create a matrix for testing
        double[][] testData = {{4, 1, 1}, {1, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a CholeskyDecompositionImpl instance
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the L matrix
        RealMatrix lMatrix = decomposition.getL();

        // Perform assertions on the L matrix
        double[][] expectedData = {{2, 0, 0}, {0.5, 2.179449471770337, 0}, {0.5, 1.1631610329058933, 1.200134136897717}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        Assert.assertEquals(expectedMatrix, lMatrix);
    }

    @Test
    public void testGetLT() {
        // Create a matrix for testing
        double[][] testData = {{4, 1, 1}, {1, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a CholeskyDecompositionImpl instance
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the L^T matrix
        RealMatrix ltMatrix = decomposition.getLT();

        // Perform assertions on the L^T matrix
        double[][] expectedData = {{2, 0.5, 0.5}, {0, 2.179449471770337, 1.1631610329058933}, {0, 0, 1.200134136897717}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        Assert.assertEquals(expectedMatrix, ltMatrix);
    }

    @Test
    public void testGetDeterminant() {
        // Create a matrix for testing
        double[][] testData = {{4, 1, 1}, {1, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a CholeskyDecompositionImpl instance
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the determinant
        double determinant = decomposition.getDeterminant();

        // Perform assertions on the determinant
        double expectedDeterminant = 38.999999999999986;
        Assert.assertEquals(expectedDeterminant, determinant, 0);
    }

    @Test
    public void testGetSolver() {
        // Create a matrix for testing
        double[][] testData = {{4, 1, 1}, {1, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Create a CholeskyDecompositionImpl instance
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the solver
        DecompositionSolver solver = decomposition.getSolver();

        // Perform assertions on the solver
        Assert.assertNotNull(solver);
    }
}
