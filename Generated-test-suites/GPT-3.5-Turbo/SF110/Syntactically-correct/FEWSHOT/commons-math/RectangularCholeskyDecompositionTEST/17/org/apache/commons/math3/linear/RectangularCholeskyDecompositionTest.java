import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = createRealMatrix();
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(decomposition);
        assertEquals(3, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithInvalidMatrix() {
        RealMatrix matrix = createInvalidRealMatrix();
        
        new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructorWithMatrixAndSmall() {
        RealMatrix matrix = createRealMatrix();
        double small = 0.0;
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        assertNotNull(decomposition);
        assertEquals(3, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithInvalidMatrixAndSmall() {
        RealMatrix matrix = createInvalidRealMatrix();
        double small = 0.0;
        
        new RectangularCholeskyDecomposition(matrix, small);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = createRealMatrix();
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        assertNotNull(rootMatrix);
        assertEquals(matrix.getRowDimension(), rootMatrix.getRowDimension());
        assertEquals(decomposition.getRank(), rootMatrix.getColumnDimension());
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = createRealMatrix();
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        int rank = decomposition.getRank();
        
        assertEquals(3, rank);
    }
    
    private RealMatrix createRealMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {2.0, 1.0, 0.0}, {3.0, 2.0, 1.0}};
        return new Array2DRowRealMatrix(data);
    }
    
    private RealMatrix createInvalidRealMatrix() {
        double[][] data = {{1.0, -1.0, 0.0}, {2.0, 1.0, 0.0}, {3.0, 2.0, 1.0}};
        return new Array2DRowRealMatrix(data);
    }
}