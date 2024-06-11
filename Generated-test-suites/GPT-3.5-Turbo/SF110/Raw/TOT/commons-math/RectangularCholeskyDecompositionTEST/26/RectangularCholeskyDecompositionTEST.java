import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    // Test cases for RectangularCholeskyDecomposition constructor 1
    @Test
    public void testConstructor1WithPositiveDefiniteMatrix() {
        double[][] data = {{4, -2, 2}, {-2, 5, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(data.length, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1WithNonPositiveDefiniteMatrix() {
        double[][] data = {{4, 2, 2}, {2, 5, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix);
    }

    // Test cases for RectangularCholeskyDecomposition constructor 2
    @Test
    public void testConstructor2WithPositiveDefiniteMatrix() {
        double[][] data = {{4, -2, 2}, {-2, 5, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(data.length, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2WithNonPositiveDefiniteMatrix() {
        double[][] data = {{4, 2, 2}, {2, 5, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix, 0.01);
    }

    // Test cases for getRootMatrix
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, -2, 2}, {-2, 5, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(matrix, decomposition.getRootMatrix());
    }

    // Test cases for getRank
    @Test
    public void testGetRank() {
        double[][] data = {{4, -2, 2}, {-2, 5, -4}, {2, -4, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(data.length, decomposition.getRank());
    }
}