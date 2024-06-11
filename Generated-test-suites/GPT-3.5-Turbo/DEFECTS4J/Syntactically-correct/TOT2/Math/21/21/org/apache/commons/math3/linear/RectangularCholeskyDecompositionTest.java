import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        assertNotNull(decomposition);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = {{1, 2, 3}, {2, 4, 6}, {3, 6, 10}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

    // Additional test cases for edge cases and error scenarios can be added here
    
}