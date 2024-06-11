import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        // Test constructor with a valid matrix and small value
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        assertNotNull(decomposition);
    }

    @Test
    public void testRectangularCholeskyDecompositionGetRootMatrix() {
        // Test getRootMatrix method after decomposition
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
    }

    @Test
    public void testRectangularCholeskyDecompositionGetRank() {
        // Test getRank method after decomposition
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionConstructorNonPositiveDefinite() {
        // Test constructor with a non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{-1, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionConstructorZeroSmall() {
        // Test constructor with zero small value
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0);
    }

}