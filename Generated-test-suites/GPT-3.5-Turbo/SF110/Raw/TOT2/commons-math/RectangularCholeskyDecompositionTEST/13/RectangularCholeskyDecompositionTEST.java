
import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case for constructor with RealMatrix input
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(rcd.getRootMatrix());
        assertEquals(3, rcd.getRank());
    }
    
    @Test
    public void testGetRootMatrix() {
        // Test case for getRootMatrix method
        double[][] data = {{9, 2, 4}, {2, 6, 3}, {4, 3, 5}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = rcd.getRootMatrix();
        
        double[][] expectedData = {{3, 0, 0}, {2.0 / 3.0, 5.0 / 6.0, 0}, {4.0 / 3.0, 1.0 / 3.0, 1.0}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedData);
        
        assertTrue(rootMatrix.subtract(expected).getNorm() < 1e-15);
    }
    
    @Test
    public void testGetRank() {
        // Test case for getRank method
        double[][] data = {{3, 0, 0}, {2.0 / 3.0, 5.0 / 6.0, 0}, {4.0 / 3.0, 1.0 / 3.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(3, rcd.getRank());
    }
}
