import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructionWithPositiveDefiniteMatrix() {
        double[][] data = { { 4, -2, 2 }, { -2, 2, -4 }, { 2, -4, 11 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        
        int rank = decomp.getRank();
        assertEquals(3, rank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructionWithNonPositiveDefiniteMatrix() {
        double[][] data = { { -1, 2, 3 }, { 2, 5, 7 }, { 3, 7, 10 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = { { 4, -2, 2 }, { -2, 2, -4 }, { 2, -4, 11 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        
        double[][] expectedData = { { 2.0, 0.0, 0.0 }, { -1.0, 1.0, 0.0 }, { 1.0, -3.0, 3.0 } };
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);
        
        assertTrue(expectedRoot.equals(root));
    }
    
    @Test
    public void testGetRank() {
        double[][] data = { { 4, -2, 2 }, { -2, 2, -4 }, { 2, -4, 11 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        
        int rank = decomp.getRank();
        assertEquals(3, rank);
    }
}