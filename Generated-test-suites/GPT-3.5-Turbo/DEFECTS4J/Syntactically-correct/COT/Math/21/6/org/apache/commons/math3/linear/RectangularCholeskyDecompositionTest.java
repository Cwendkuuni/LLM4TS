import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Create a test matrix
        double[][] data = {{2, -1}, {-1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // Create the decomposition object
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        // Perform the test
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(matrix.getRowDimension(), rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetRank() {
        // Create a test matrix
        double[][] data = {{2, -1}, {-1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // Create the decomposition object
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        // Perform the test
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }
}