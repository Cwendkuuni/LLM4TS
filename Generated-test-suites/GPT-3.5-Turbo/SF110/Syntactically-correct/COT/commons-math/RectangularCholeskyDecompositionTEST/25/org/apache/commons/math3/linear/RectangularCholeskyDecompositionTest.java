
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition_withZeroSmallParameter() {
        // Test the constructor with small parameter equal to 0
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);
        
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }
    
    @Test
    public void testRectangularCholeskyDecomposition_withNonZeroSmallParameter() {
        // Test the constructor with small parameter greater than 0
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);
        
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_withNonPositiveDefiniteMatrix() {
        // Test the constructor with a non-positive definite matrix
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, -98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    // TODO: Add more test cases for edge cases and exception handling scenarios
    
}

