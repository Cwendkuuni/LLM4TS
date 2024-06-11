import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposer;
    
    @Before
    public void setUp() {
        // Create a 3x3 positive semidefinite matrix
        double[][] data = {{4, 2, 1},
                           {2, 5, 3},
                           {1, 3, 6}};
        matrix = MatrixUtils.createRealMatrix(data);
        try {
            decomposer = new RectangularCholeskyDecomposition(matrix, 1e-10);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when it should not be thrown.");
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {{2, 0, 0},
                                                                              {1, 2, 0},
                                                                              {0.5, 0.7, 2}});
        RealMatrix actualRoot = decomposer.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
    }
    
    @Test
    public void testGetRank() {
        int expectedRank = 3;
        int actualRank = decomposer.getRank();
        assertEquals(expectedRank, actualRank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidMatrix() {
        double[][] data = {{1, -2, 1},
                           {-2, 5, -3},
                           {1, -3, 6}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposer = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testZeroTolerance() {
        double[][] data = {{4, 2, 1},
                           {2, 5, 3},
                           {1, 3, 6}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposer = new RectangularCholeskyDecomposition(matrix, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTolerance() {
        double[][] data = {{4, 2, 1},
                           {2, 5, 3},
                           {1, 3, 6}};
        matrix = MatrixUtils.createRealMatrix(data);
        decomposer = new RectangularCholeskyDecomposition(matrix, -1e-10);
    }
}