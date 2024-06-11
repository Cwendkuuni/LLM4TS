import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    // Test constructor with positive definite matrix
    @Test
    public void testConstructor_PositiveDefiniteMatrix() {
        double[][] data = {{4, 0}, {0, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Assert statements
        assertEquals(decomposition.getRootMatrix(), matrix);
        assertEquals(decomposition.getRank(), 2);
    }

    // Test constructor with non-positive definite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix() {
        double[][] data = {{-4, 0}, {0, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    // Test constructor with small threshold
    @Test
    public void testConstructor_SmallThreshold() {
        double[][] data = {{4, 0}, {0, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Assert statements
        assertEquals(decomposition.getRootMatrix(), matrix);
        assertEquals(decomposition.getRank(), 2);
    }

    // Test getRootMatrix method
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 0}, {0, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Assert statement
        assertEquals(decomposition.getRootMatrix(), matrix);
    }

    // Test getRank method
    @Test
    public void testGetRank() {
        double[][] data = {{4, 0}, {0, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Assert statement
        assertEquals(decomposition.getRank(), 2);
    }
}