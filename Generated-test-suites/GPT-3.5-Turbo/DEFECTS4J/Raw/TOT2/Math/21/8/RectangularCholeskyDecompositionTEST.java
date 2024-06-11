import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test the constructor with a basic input
        double[][] data = {{4, -1, 2}, {-1, 4.25, 2.5}, {2, 2.5, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        assertNotNull(rcd);
        assertEquals(3, rcd.getRank());
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        // Test the constructor with a non-positive definite matrix
        double[][] data = {{1, 2}, {2, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetRootMatrix() {
        // Test the getRootMatrix method
        double[][] data = {{4, -1, 2}, {-1, 4.25, 2.5}, {2, 2.5, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix root = rcd.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Test the getRank method
        double[][] data = {{4, -1, 2}, {-1, 4.25, 2.5}, {2, 2.5, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);

        assertEquals(3, rcd.getRank());
    }
}