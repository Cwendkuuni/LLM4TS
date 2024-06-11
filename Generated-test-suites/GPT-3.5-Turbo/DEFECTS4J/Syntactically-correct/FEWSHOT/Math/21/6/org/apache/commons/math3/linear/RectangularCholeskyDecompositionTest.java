import org.apache.commons.math3.linear.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-6);
        RealMatrix rootMatrix = decomp.getRootMatrix();
        assertEquals(3, decomp.getRank());
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(2.0, rootMatrix.getEntry(0,0), 1e-6);
        assertEquals(6.0, rootMatrix.getEntry(1,0), 1e-6);
        assertEquals(-8.0, rootMatrix.getEntry(2,0), 1e-6);
        assertEquals(5.0, rootMatrix.getEntry(1,1), 1e-6);
        assertEquals(-1.0, rootMatrix.getEntry(2,1), 1e-6);
        assertEquals(7.0, rootMatrix.getEntry(2,2), 1e-6);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{4, 12, -16}, {12, 37, -43}, {-16, -43, 9}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-6);
    }
    
}