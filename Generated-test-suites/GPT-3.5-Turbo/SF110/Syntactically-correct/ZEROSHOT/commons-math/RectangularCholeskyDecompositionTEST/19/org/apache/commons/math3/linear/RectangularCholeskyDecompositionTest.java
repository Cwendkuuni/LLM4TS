import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        // Create a test matrix
        double[][] testData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        try {
            // Initialize the decomposition object with the test matrix
            decomposition = new RectangularCholeskyDecomposition(testMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        // Test the getRootMatrix() method
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {{1, 0}, {2, 1}, {3, 2}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertArrayEquals(expectedMatrix.getData(), rootMatrix.getData());
    }
    
    @Test
    public void testGetRank() {
        // Test the getRank() method
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }
}