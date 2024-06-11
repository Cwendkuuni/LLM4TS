import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Create a test matrix
        double[][] data = {{4, 2, 1}, {2, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        // Perform the decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);

        // Check if the root matrix is correct
        RealMatrix root = decomposition.getRootMatrix();
        double[][] expectedData = {{2, 1, 0}, {1, 2, 1}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(root.equals(expectedRoot));
    }

    @Test
    public void testGetRank() {
        // Create a test matrix
        double[][] data = {{4, 2, 1}, {2, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        // Perform the decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);

        // Check if the rank is correct
        int rank = decomposition.getRank();
        int expectedRank = 2;
        assertEquals(expectedRank, rank);
    }
}