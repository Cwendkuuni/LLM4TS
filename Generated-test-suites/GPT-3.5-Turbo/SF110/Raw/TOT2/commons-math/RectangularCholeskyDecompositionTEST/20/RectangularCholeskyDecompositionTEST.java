import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(0, 1, 1.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 3.0);

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(rcd);
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(0, 1, 1.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 3.0);

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = rcd.getRootMatrix();
        
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(0, 1, 1.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 3.0);

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        int rank = rcd.getRank();
        
        assertEquals(2, rank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, -1.0);
        matrix.setEntry(0, 1, 0.0);
        matrix.setEntry(1, 0, 0.0);
        matrix.setEntry(1, 1, 3.0);

        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
    }
}