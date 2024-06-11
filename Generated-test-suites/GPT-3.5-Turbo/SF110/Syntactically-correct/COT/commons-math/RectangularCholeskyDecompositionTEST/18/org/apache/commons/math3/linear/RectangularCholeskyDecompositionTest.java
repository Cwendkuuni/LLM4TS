
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorThrowsExceptionForNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, -1}});
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException, but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception was thrown, test passed
        }
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 0}, {1, 2}});
        
        assertTrue(rootMatrix.equals(expected));
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        int rank = decomposition.getRank();
        
        assertEquals(2, rank);
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testConstructorThrowsExceptionForZeroMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 0}});
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException, but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception was thrown, test passed
        }
    }

    @Test
    public void testConstructorWithSmallParameter() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        double small = 0.1;
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2.023715, 0}, {1.011858, 2.022825}});
        
        assertTrue(rootMatrix.equals(expected));
    }

    @Test
    public void testGetRootMatrixWithZeroMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 0}});
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 0}});
        
        assertTrue(rootMatrix.equals(expected));
    }

    @Test
    public void testGetRankWithZeroMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 0}});
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        int rank = decomposition.getRank();
        
        assertEquals(0, rank);
    }
}

