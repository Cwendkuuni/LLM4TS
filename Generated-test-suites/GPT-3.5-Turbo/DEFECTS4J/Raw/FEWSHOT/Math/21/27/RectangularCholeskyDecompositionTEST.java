import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
  
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.000001);
    }
  
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(1, 1, 4.0);
        matrix.setEntry(2, 2, 4.0);
        rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.000001);
        RealMatrix rootMatrix = rectangularCholeskyDecomposition.getRootMatrix();

        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(3, rootMatrix.getRowDimension());
    }
  
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4.0);
        matrix.setEntry(1, 1, 4.0);
        matrix.setEntry(2, 2, 4.0);
        rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.000001);
        int rank = rectangularCholeskyDecomposition.getRank();

        assertEquals(3, rank);
    }
}