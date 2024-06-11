import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private RectangularCholeskyDecomposition cholesky;
    private RealMatrix matrix;
    private double small;
    
    @Before
    public void setUp() {
        // Create a test matrix
        double[][] data = {{1, 0, 0}, {0, 2, 0}, {0, 0, 3}};
        matrix = MatrixUtils.createRealMatrix(data);
        small = 1e-10;
        cholesky = new RectangularCholeskyDecomposition(matrix, small);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix rootMatrix = cholesky.getRootMatrix();
        assertNotNull(rootMatrix);
    }
    
    @Test
    public void testGetRank() {
        int rank = cholesky.getRank();
        assertEquals(3, rank);
    }
    
    @Test
    public void testConstructor() {
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, -1);
            fail("Exception not thrown for negative small value");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception should be thrown for negative small value
        }
        
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 1);
            assertTrue(true);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown for valid small value");
        }
    }
}