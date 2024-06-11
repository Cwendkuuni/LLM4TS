import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // create a test matrix
        double[][] testData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        
        // create the expected root matrix
        double[][] expectedData = {
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        };
        
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-10);
        
        assertEquals(expectedMatrix, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        // create a test matrix
        double[][] testData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        
        // the rank of the matrix is 3
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-10);
        
        assertEquals(3, decomposition.getRank());
    }
}