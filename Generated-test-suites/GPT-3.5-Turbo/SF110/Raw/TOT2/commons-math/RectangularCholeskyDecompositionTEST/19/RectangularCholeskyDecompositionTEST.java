import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid input matrix
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
        
        // Test constructor with non-positive definite matrix
        double[][] nonPosDefMatrixData = {{1, 2, 3}, {2, 5, 7}, {3, 7, 10}};
        RealMatrix nonPosDefMatrix = MatrixUtils.createRealMatrix(nonPosDefMatrixData);
        try {
            RectangularCholeskyDecomposition nonPosDefDecomposition = new RectangularCholeskyDecomposition(nonPosDefMatrix);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(4.0, rootMatrix.getEntry(0, 0), 0.0001);
        assertEquals(12.0, rootMatrix.getEntry(1, 0), 0.0001);
        assertEquals(-16.0, rootMatrix.getEntry(2, 0), 0.0001);
    }
    
    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, decomposition.getRank());
    }
}