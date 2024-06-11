import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 2}});
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.1);
        RealMatrix root = decomposer.getRootMatrix();
        assertEquals(1, root.getEntry(0, 0), 0.001);
        assertEquals(0, root.getEntry(0, 1), 0.001);
        assertEquals(0, root.getEntry(1, 0), 0.001);
        assertEquals(Math.sqrt(2), root.getEntry(1, 1), 0.001);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 2}});
        RectangularCholeskyDecomposition decomposer1 = new RectangularCholeskyDecomposition(matrix1, 0.1);
        assertEquals(2, decomposer1.getRank());

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        RectangularCholeskyDecomposition decomposer2 = new RectangularCholeskyDecomposition(matrix2, 0.1);
        assertEquals(1, decomposer2.getRank());

        RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][]{{1, 0}, {1, 1}});
        RectangularCholeskyDecomposition decomposer3 = new RectangularCholeskyDecomposition(matrix3, 0.1);
        assertEquals(1, decomposer3.getRank());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, -1}});
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.1);
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0.01}});
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.1);
        assertEquals(1, decomposer.getRootMatrix().getEntry(0, 0), 0.001);
        assertEquals(0, decomposer.getRootMatrix().getEntry(0, 1), 0.001);
        assertEquals(0, decomposer.getRootMatrix().getEntry(1, 0), 0.001);
        assertEquals(Math.sqrt(0.01), decomposer.getRootMatrix().getEntry(1, 1), 0.001);
    }

}