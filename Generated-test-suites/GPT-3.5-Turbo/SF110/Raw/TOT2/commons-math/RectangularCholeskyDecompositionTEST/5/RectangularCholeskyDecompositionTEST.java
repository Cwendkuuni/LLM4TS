import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1}, {-1, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testConstructorWithNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 4}});
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Successful test
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1}, {-1, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomposition.getRootMatrix();
        
        assertEquals(4.0, root.getEntry(0, 0), 0.0001);
        assertEquals(0.0, root.getEntry(0, 1), 0.0001);
        assertEquals(-1.0 / 4.0, root.getEntry(1, 0), 0.0001);
        assertEquals(3.0 / 4.0, root.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1}, {-1, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(2, decomposition.getRank());
    }
}