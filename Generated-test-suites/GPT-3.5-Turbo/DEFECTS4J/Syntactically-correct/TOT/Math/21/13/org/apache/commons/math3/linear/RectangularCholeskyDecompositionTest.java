import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        double[][] data = {
                {4.0, 1.0, 1.0, 1.0},
                {1.0, 4.0, -1.0, -1.0},
                {1.0, -1.0, 4.0, -1.0},
                {1.0, -1.0, -1.0, 4.0}
        };
        matrix = MatrixUtils.createRealMatrix(data);
        decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
            {2.0,  0.0,  0.0},
            {0.5,  1.936,  0.0},
            {0.5, -0.322,  1.936},
            {0.5, -0.322, -0.322},
        });
        RealMatrix root = decomposition.getRootMatrix();
        assertArrayEquals(expected.getData(), root.getData());
    }
    
    @Test
    public void testGetRank() {
        int expected = 3;
        int rank = decomposition.getRank();
        assertEquals(expected, rank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        double[][] data = {
                {4.0, 1.0, 1.0, 1.0},
                {1.0, 4.0, -1.0, -1.0},
                {1.0, -1.0, 5.0, -1.0},
                {1.0, -1.0, -1.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test
    public void testSmallThreshold() {
        double[][] data = {
                {4.0, 1.0, 1.0, 1.0},
                {1.0, 4.0, -1.0, -1.0},
                {1.0, -1.0, 4.0, -1.0},
                {1.0, -1.0, -1.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-100);
        int expected = 4;
        int rank = decomposition.getRank();
        assertEquals(expected, rank);
    }
    
    @Test
    public void testLargeThreshold() {
        double[][] data = {
                {4.0, 1.0, 1.0, 1.0},
                {1.0, 4.0, -1.0, -1.0},
                {1.0, -1.0, 4.0, -1.0},
                {1.0, -1.0, -1.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e10);
        int expected = 0;
        int rank = decomposition.getRank();
        assertEquals(expected, rank);
    }
    
}