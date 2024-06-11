import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] data = {{10, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        assertNotNull(decomposition);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{10, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        assertNotNull(rootMatrix);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{10, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        int rank = decomposition.getRank();
        
        assertEquals(2, rank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] data = {{-1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }
    
}