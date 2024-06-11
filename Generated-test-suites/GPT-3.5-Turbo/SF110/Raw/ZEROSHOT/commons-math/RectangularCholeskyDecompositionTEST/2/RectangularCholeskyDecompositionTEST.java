import org.apache.commons.math3.linear.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test case 1: positive definite matrix, small = 0.0
        double[][] data1 = {{4.0, -1.0, -2.0}, {-1.0, 4.0, 0.0}, {-2.0, 0.0, 4.0}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        RectangularCholeskyDecomposition c1 = new RectangularCholeskyDecomposition(matrix1);
        assertNotNull(c1.getRootMatrix());
        assertEquals(3, c1.getRank());

        // Test case 2: positive definite matrix, small = 1e-10
        double[][] data2 = {{4.0, -1.0, -2.0}, {-1.0, 4.0, 0.0}, {-2.0, 0.0, 4.0}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        RectangularCholeskyDecomposition c2 = new RectangularCholeskyDecomposition(matrix2, 1e-10);
        assertNotNull(c2.getRootMatrix());
        assertEquals(3, c2.getRank());

        // Test case 3: non-positive definite matrix, small = 0.0
        double[][] data3 = {{1.0, -2.0, -1.0}, {-2.0, 5.0, 1.0}, {-1.0, 1.0, 6.0}};
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(data3);
        try {
            RectangularCholeskyDecomposition c3 = new RectangularCholeskyDecomposition(matrix3);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getOffendingValue(), 0.0);
            assertEquals(0, e.getRow());
            assertEquals(0.0, e.getSmall(), 0.0);
        }

        // Test case 4: non-positive definite matrix, small = 1e-10
        double[][] data4 = {{1.0, -2.0, -1.0}, {-2.0, 5.0, 1.0}, {-1.0, 1.0, 6.0}};
        RealMatrix matrix4 = MatrixUtils.createRealMatrix(data4);
        try {
            RectangularCholeskyDecomposition c4 = new RectangularCholeskyDecomposition(matrix4, 1e-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getOffendingValue(), 0.0);
            assertEquals(0, e.getRow());
            assertEquals(1e-10, e.getSmall(), 0.0);
        }
    }

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4.0, -1.0, -2.0}, {-1.0, 4.0, 0.0}, {-2.0, 0.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition c = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = c.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        double[][] data = {{4.0, -1.0, -2.0}, {-1.0, 4.0, 0.0}, {-2.0, 0.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition c = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, c.getRank());
    }
}