import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        RealMatrix rootMatrix = decomposition.getRootMatrix();

        assertEquals(rootMatrix.getRowDimension(), matrix.getRowDimension());
        assertEquals(rootMatrix.getColumnDimension(), decomposition.getRank());

        for (int i = 0; i < rootMatrix.getRowDimension(); i++) {
            for (int j = 0; j < rootMatrix.getColumnDimension(); j++) {
                assertEquals(rootMatrix.getEntry(i, j), decomposition.getRootMatrix().getEntry(i, j), 0.0001);
            }
        }
    }

    @Test
    public void testGetRank() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        int rank = decomposition.getRank();

        assertEquals(rank, 3);
    }

}