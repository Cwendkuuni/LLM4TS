import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case for constructing RectangularCholeskyDecomposition with valid input
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertNotNull(decomp);
    }

    @Test
    public void testGetRootMatrix() {
        // Test case for getting root matrix from RectangularCholeskyDecomposition
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix root = decomp.getRootMatrix();
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Test case for getting rank from RectangularCholeskyDecomposition
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-10);
        int rank = decomp.getRank();
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrixException() {
        // Test case for constructing RectangularCholeskyDecomposition with non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 4}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

}