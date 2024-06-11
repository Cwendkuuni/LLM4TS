import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition_Constructor_ValidMatrix() {
        double[][] testData = {{4.0, 12.0}, {12.0, 37.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(rcd);
        assertEquals(2, rcd.getRank());
        assertArrayEquals(testData, rcd.getRootMatrix().getData());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_Constructor_InvalidMatrix() {
        double[][] testData = {{1.0, 2.0}, {2.0, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        fail("An exception should have been thrown.");
    }
    
    @Test
    public void testRectangularCholeskyDecomposition_Constructor_Small() {
        double[][] testData = {{4.0, 12.0}, {12.0, 37.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        double small = 0.000001;
        
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        
        assertNotNull(rcd);
        assertEquals(2, rcd.getRank());
        assertArrayEquals(testData, rcd.getRootMatrix().getData());
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] testData = {{4.0, 12.0}, {12.0, 37.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = rcd.getRootMatrix();
        
        assertNotNull(rootMatrix);
        assertArrayEquals(testData, rootMatrix.getData());
    }
    
    @Test
    public void testGetRank() {
        double[][] testData = {{4.0, 12.0}, {12.0, 37.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        int rank = rcd.getRank();
        
        assertEquals(2, rank);
    }
}