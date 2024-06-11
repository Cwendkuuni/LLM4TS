import org.junit.Test;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private final double EPSILON = 1e-10;

    @Test
    public void testConstructorWithValidMatrix() {
        double[][] data = {
            {4, -2, 2},
            {-2, 10, -4},
            {2, -4, 6}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {
            {2, 0, 0},
            {-1, 3, 0},
            {1, -2, 1}
        };
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        assertEquals(expectedRootMatrix, rootMatrix);
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithInvalidMatrix() {
        double[][] data = {
            {-1, 2, 3},
            {2, 1, 0},
            {3, 0, -1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithZeroMatrix() {
        double[][] data = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(data);
        
        assertEquals(expectedRootMatrix, rootMatrix);
        assertEquals(0, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {
            {4, -2, 2},
            {-2, 10, -4},
            {2, -4, 6}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = {
            {2, 0, 0},
            {-1, 3, 0},
            {1, -2, 1}
        };
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        assertEquals(expectedRootMatrix, rootMatrix);
    }

    @Test
    public void testGetRank() {
        double[][] data = {
            {4, -2, 2},
            {-2, 10, -4},
            {2, -4, 6}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testGetRankWithZeroRank() {
        double[][] data = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(0, decomposition.getRank());
    }

}