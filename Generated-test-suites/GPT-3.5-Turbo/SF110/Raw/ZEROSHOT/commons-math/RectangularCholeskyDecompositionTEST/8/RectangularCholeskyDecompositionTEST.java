import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test the RectangularCholeskyDecomposition constructor with RealMatrix parameter.
     */
    @Test
    public void testConstructorRealMatrix() {
        double[][] data = {{4, 2}, {2, 5}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(decomp);
    }
    
    /**
     * Test the RectangularCholeskyDecomposition constructor with RealMatrix and small parameter.
     */
    @Test
    public void testConstructorRealMatrixSmall() {
        double[][] data = {{4, 2}, {2, 5}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        double small = 0.1;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        
        assertNotNull(decomp);
    }
    
    /**
     * Test the getRootMatrix method.
     */
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 2}, {2, 5}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomp.getRootMatrix();
        
        assertNotNull(rootMatrix);
    }
    
    /**
     * Test the getRank method.
     */
    @Test
    public void testGetRank() {
        double[][] data = {{4, 2}, {2, 5}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        int rank = decomp.getRank();
        
        assertEquals(2, rank);
    }
}