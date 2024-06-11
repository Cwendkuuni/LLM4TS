package org.apache.commons.math3.linear;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor1_PositiveMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0}, {0, 4}});
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, cholesky.getRank());
    }
    
    @Test
    public void testConstructor2_NegativeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-2, 0}, {0, 4}});
        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0}, {0, 4}});
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = cholesky.getRootMatrix();
        assertEquals(matrix.getColumnDimension(), rootMatrix.getColumnDimension());
        assertEquals(matrix.getRowDimension(), rootMatrix.getRowDimension());
    }

}