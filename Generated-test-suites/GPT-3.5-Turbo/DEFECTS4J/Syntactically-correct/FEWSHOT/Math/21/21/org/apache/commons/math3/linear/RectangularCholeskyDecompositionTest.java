import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
   
    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0);
        RealMatrix rootMatrix = decomposer.getRootMatrix();
        assertEquals(matrix, rootMatrix.multiplyTranspose(rootMatrix.transpose()));
        assertEquals(3, decomposer.getRank());
    }
    
    @Test
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 0, 0 }, { 0, -1, 0 }, { 0, 0, 1 } });
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1, e.getIndex());
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0);
        RealMatrix rootMatrix = decomposer.getRootMatrix();
        assertEquals(matrix, rootMatrix.multiplyTranspose(rootMatrix.transpose()));
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0);
        assertEquals(3, decomposer.getRank());
    }
}