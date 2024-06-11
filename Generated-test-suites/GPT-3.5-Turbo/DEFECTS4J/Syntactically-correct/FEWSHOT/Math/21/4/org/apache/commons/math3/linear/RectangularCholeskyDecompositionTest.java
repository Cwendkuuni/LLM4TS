import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    // Test case for matrix with rank 1
    @Test
    public void testRank1Matrix() {
        double[][] data = {{2}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(1, decomposition.getRank());
            assertEquals(1, root.getRowDimension());
            assertEquals(1, root.getColumnDimension());
            assertEquals(Math.sqrt(2), root.getEntry(0, 0), 0.000001);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when not expected");
        }
    }

    // Test case for matrix with rank 2
    @Test
    public void testRank2Matrix() {
        double[][] data = {{4, 2}, {2, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(2, decomposition.getRank());
            assertEquals(2, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());
            assertEquals(2.828427, root.getEntry(0, 0), 0.000001);
            assertEquals(1.414214, root.getEntry(1, 0), 0.000001);
            assertEquals(0.0, root.getEntry(0, 1), 0.000001);
            assertEquals(1.581139, root.getEntry(1, 1), 0.000001);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when not expected");
        }
    }

    // Test case for non-positive definite matrix
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-1, 0}, {0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    // Test case for matrix with rank 3
    @Test
    public void testRank3Matrix() {
        double[][] data = {{9, 3, 6}, {3, 5, 1}, {6, 1, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, decomposition.getRank());
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            assertEquals(3.0, root.getEntry(0, 0), 0.000001);
            assertEquals(1.0, root.getEntry(0, 1), 0.000001);
            assertEquals(2.0, root.getEntry(0, 2), 0.000001);
            assertEquals(0.0, root.getEntry(1, 0), 0.000001);
            assertEquals(1.732051, root.getEntry(1, 1), 0.000001);
            assertEquals(-0.57735, root.getEntry(1, 2), 0.000001);
            assertEquals(0.0, root.getEntry(2, 0), 0.000001);
            assertEquals(0.0, root.getEntry(2, 1), 0.000001);
            assertEquals(1.788854, root.getEntry(2, 2), 0.000001);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown when not expected");
        }
    }
}