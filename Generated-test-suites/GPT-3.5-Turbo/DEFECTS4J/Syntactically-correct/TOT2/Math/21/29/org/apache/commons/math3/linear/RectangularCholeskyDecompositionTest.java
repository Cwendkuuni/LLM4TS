import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Test case for getting the root matrix
        double[][] data = {{25, 15}, {15, 18}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcDecomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);

        RealMatrix rootMatrix = rcDecomposition.getRootMatrix();

        assertEquals(25, rootMatrix.getEntry(0, 0), 0.0001);
        assertEquals(15, rootMatrix.getEntry(1, 0), 0.0001);
        assertEquals(0, rootMatrix.getEntry(0, 1), 0.0001);
        assertEquals(3, rootMatrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetRank() {
        // Test case for getting the rank
        double[][] data = {{25, 15}, {15, 18}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcDecomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);

        int rank = rcDecomposition.getRank();

        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        // Test case for handling NonPositiveDefiniteMatrixException
        double[][] data = {{-2, 1}, {1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcDecomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
    }

}