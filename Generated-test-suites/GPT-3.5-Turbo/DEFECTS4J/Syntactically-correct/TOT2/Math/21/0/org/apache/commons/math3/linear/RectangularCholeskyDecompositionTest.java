import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] data = {{10, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            assertNotNull(decomposition);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when it shouldn't have been.");
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{10, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            RealMatrix root = decomposition.getRootMatrix();
            
            assertEquals(10.0499, root.getEntry(0,0), 1e-4);
            assertEquals(0.3779, root.getEntry(1,0), 1e-4);
            assertEquals(5, root.getEntry(1,1), 1e-4);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when it shouldn't have been.");
        }
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{10, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            int rank = decomposition.getRank();
            
            assertEquals(2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when it shouldn't have been.");
        }
    }
}