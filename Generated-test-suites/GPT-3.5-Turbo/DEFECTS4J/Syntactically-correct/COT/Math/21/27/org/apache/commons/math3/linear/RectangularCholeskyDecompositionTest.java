
import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Create a test matrix
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(data);

        // Create the expected root matrix
        double[][] expectedData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 0.0001);

        // Check that the root matrix matches the expected result
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertTrue(rootMatrix.equals(expectedRoot));
    }

    @Test
    public void testGetRank() {
        // Create a test matrix
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(data);

        // Perform the rectangular Cholesky decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 0.0001);

        // Check that the rank matches the expected result
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }
}

