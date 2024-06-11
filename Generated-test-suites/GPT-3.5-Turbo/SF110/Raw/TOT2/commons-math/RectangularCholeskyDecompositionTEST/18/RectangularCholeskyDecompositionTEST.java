import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor() {
        // Create a positive definite matrix
        double[][] data = {{4, 1}, {1, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(2, decomposition.getRank());
        assertNotNull(decomposition.getRootMatrix());
    }
    
    @Test
    public void testConstructorNonPositiveDefiniteMatrix() {
        // Create a non-positive definite matrix
        double[][] data = {{4, 1}, {1, -3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception caught as expected
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 1}, {1, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(matrix.getRowDimension(), decomposition.getRootMatrix().getRowDimension());
        assertEquals(decomposition.getRootMatrix().getColumnDimension(), decomposition.getRank());
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{4, 1}, {1, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(2, decomposition.getRank());
    }
}