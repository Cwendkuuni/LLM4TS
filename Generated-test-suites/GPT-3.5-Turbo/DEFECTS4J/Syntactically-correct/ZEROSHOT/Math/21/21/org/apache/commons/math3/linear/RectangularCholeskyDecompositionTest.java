import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Arrange
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        // Act
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.00001);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8}, {0, 1, -5}, {0, 0, 3}});
        RealMatrix actual = decomposition.getRootMatrix();

        // Assert
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGetRank() {
        // Arrange
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        // Act
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.00001);
        int expected = 3;
        int actual = decomposition.getRank();

        // Assert
        assertEquals(expected, actual);
    }
}