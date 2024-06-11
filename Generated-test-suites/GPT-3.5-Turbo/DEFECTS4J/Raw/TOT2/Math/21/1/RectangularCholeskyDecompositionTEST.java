import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Arrange
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        });
        double small = 1e-10;

        // Act
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);

        // Assert
        assertNotNull(decomposer.getRootMatrix());
        assertEquals(3, decomposer.getRank());
    }

    @Test
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrixException() {
        // Arrange
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 0, 0},
                {0, -1, 0},
                {0, 0, 1}
        });
        double small = 1e-10;

        // Act & Assert
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    // Add more test cases for other public methods if needed

}