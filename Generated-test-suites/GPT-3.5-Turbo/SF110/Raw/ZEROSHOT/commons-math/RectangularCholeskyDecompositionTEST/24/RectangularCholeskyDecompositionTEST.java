import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test case 1: positive definite matrix
        double[][] data1 = {{1.0, 0.0},
                            {0.0, 2.0}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        try {
            RectangularCholeskyDecomposition rcd1 = new RectangularCholeskyDecomposition(matrix1);
            assertEquals(matrix1, rcd1.getRootMatrix());
            assertEquals(2, rcd1.getRank());
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
        
        // Test case 2: non-positive definite matrix
        double[][] data2 = {{-1.0, 0.0},
                            {0.0, 2.0}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        try {
            RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(matrix2);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getOffendingValue(), 0.0);
            assertEquals(0, e.getRowIndex());
            assertEquals(0.0, e.getSmall(), 0.0);
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{1.0, 0.0},
                            {0.0, 2.0},
                            {0.0, 0.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = rcd.getRootMatrix();
            assertEquals(1.0, rootMatrix.getEntry(0, 0), 0.0);
            assertEquals(0.0, rootMatrix.getEntry(1, 0), 0.0);
            assertEquals(0.0, rootMatrix.getEntry(2, 0), 0.0);
            assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.0);
            assertEquals(1.4142135623730951, rootMatrix.getEntry(1, 1), 0.0000000001);
            assertEquals(0.0, rootMatrix.getEntry(2, 1), 0.0);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{1.0, 0.0},
                            {0.0, 2.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            assertEquals(2, rcd.getRank());
            
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }
}