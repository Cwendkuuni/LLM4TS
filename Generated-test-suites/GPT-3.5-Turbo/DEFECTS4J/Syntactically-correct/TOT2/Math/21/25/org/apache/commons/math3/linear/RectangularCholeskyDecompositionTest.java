import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRootMatrix() {
        double[][] data = {{4, 1}, {1, 2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposition.getRootMatrix();
        
        double[][] expectedData = {{2, 0}, {0.5, 1}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);
        
        assertEquals(expectedRoot, root);
    }

    @Test
    public void testRank() {
        double[][] data = {{1, 0}, {0, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();
        
        assertEquals(2, rank);
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double[][] data = {{-1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testInvalidMatrixSize() {
        double[][] data = {{1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}