import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor() {
        // Test basic functionality of the constructor
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1, 1}, {-1, 4.25, 2.75}, {1, 2.75, 3.5}});
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(cholesky);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrixException() {
        // Test exception handling when a non-positive definite matrix is provided
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1, 1}, {-1, 3.5, 2.75}, {1, 2.75, 3.5}});
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testGetRootMatrix() {
        // Test the getRootMatrix() method
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1, 1}, {-1, 4.25, 2.75}, {1, 2.75, 3.5}});
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = cholesky.getRootMatrix();
        assertNotNull(root);
        assertEquals(root.getRowDimension(), matrix.getRowDimension());
        assertEquals(root.getColumnDimension(), cholesky.getRank());
    }
    
    @Test
    public void testGetRank() {
        // Test the getRank() method
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, -1, 1}, {-1, 4.25, 2.75}, {1, 2.75, 3.5}});
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        int rank = cholesky.getRank();
        assertEquals(rank, matrix.getRowDimension());
    }
}