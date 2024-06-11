import org.junit.Test;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] data = {{ 1, 0 }, { 0, 1 }}; 
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            
            assertEquals(matrix, decomposition.getRootMatrix());
            assertEquals(2, decomposition.getRank());
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNonPositiveMatrix() {
        double[][] data = {{ -1, 0 }, { 0, -1 }}; 
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithSmallParameter() {
        double[][] data = {{ 1, 0 }, { 0, 1 }}; 
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        double small = 0.01;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRectangularCholeskyDecompositionWithNullMatrix() {
        RealMatrix matrix = null;
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{ 1, 0 }, { 0, 1 }}; 
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(matrix, decomposition.getRootMatrix());
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{ 1, 0 }, { 0, 1 }}; 
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(2, decomposition.getRank());
    }

}