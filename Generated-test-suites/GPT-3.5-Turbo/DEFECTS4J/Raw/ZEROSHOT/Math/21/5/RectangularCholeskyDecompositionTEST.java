import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test data
        double[][] data = {
            {4.0, 12.0, -16.0},
            {12.0, 37.0, -43.0},
            {-16.0, -43.0, 98.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;

        // Create instance of RectangularCholeskyDecomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        // Verify instance variables
        assertNotNull(decomp.getRootMatrix());
        assertEquals(3, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix() {
        // Test data
        double[][] data = {
            {1.0, 2.0},
            {2.0, 5.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;

        // Create instance of RectangularCholeskyDecomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        // Test data
        double[][] data = {
            {4.0, 12.0, -16.0},
            {12.0, 37.0, -43.0},
            {-16.0, -43.0, 98.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;

        // Create instance of RectangularCholeskyDecomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        // Verify root matrix
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(2.0, root.getEntry(0, 0), 0.0);
        assertEquals(6.0, root.getEntry(0, 1), 0.0);
        assertEquals(-8.0, root.getEntry(0, 2), 0.0);
        assertEquals(0.0, root.getEntry(1, 0), 0.0);
        assertEquals(1.0, root.getEntry(1, 1), 0.0);
        assertEquals(-3.0, root.getEntry(1, 2), 0.0);
        assertEquals(0.0, root.getEntry(2, 0), 0.0);
        assertEquals(0.0, root.getEntry(2, 1), 0.0);
        assertEquals(3.0, root.getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetRank() {
        // Test data
        double[][] data = {
            {4.0, 12.0, -16.0},
            {12.0, 37.0, -43.0},
            {-16.0, -43.0, 98.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 1e-10;

        // Create instance of RectangularCholeskyDecomposition
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        // Verify rank
        assertEquals(3, decomp.getRank());
    }

}