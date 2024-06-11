import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case 1: Positive Definite Matrix
        double[][] matrix1 = {
                {4.0, 1.0, -1.0},
                {1.0, 3.0, 2.0},
                {-1.0, 2.0, 5.0}
        };
        RealMatrix inputMatrix1 = new Array2DRowRealMatrix(matrix1);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition rectangularDecomposition1 = new RectangularCholeskyDecomposition(inputMatrix1, small);
            RealMatrix expectedRootMatrix1 = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(4.0), 1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(5.0)},
                    {-1.0 / FastMath.sqrt(4.0), (3.0 - 1.0 / 3.0) / FastMath.sqrt(3.0), (5.0 - 2.0 / 3.0) / FastMath.sqrt(5.0)},
                    {1.0 / FastMath.sqrt(4.0), -(1.0 / 3.0) / FastMath.sqrt(3.0), (5.0 - 2.0 / 3.0) / FastMath.sqrt(5.0) - (2.0 / 3.0) / FastMath.sqrt(5.0)}});
            RealMatrix actualRootMatrix1 = rectangularDecomposition1.getRootMatrix();
            assertEquals(expectedRootMatrix1, actualRootMatrix1);
            assertEquals(3, rectangularDecomposition1.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }

        // Test case 2: Positive Semidefinite Matrix
        double[][] matrix2 = {
                {4.0, 1.0, -1.0},
                {1.0, 3.0, 2.0},
                {-1.0, 2.0, 0.0}
        };
        RealMatrix inputMatrix2 = new Array2DRowRealMatrix(matrix2);
        try {
            RectangularCholeskyDecomposition rectangularDecomposition2 = new RectangularCholeskyDecomposition(inputMatrix2, 1e-10);
            RealMatrix expectedRootMatrix2 = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(4.0), 1.0 / FastMath.sqrt(3.0), 0.0},
                    {-1.0 / FastMath.sqrt(4.0), (3.0 - 1.0 / 3.0) / FastMath.sqrt(3.0), 0.0},
                    {1.0 / FastMath.sqrt(4.0), -(1.0 / 3.0) / FastMath.sqrt(3.0), 0.0}});
            RealMatrix actualRootMatrix2 = rectangularDecomposition2.getRootMatrix();
            assertEquals(expectedRootMatrix2, actualRootMatrix2);
            assertEquals(3, rectangularDecomposition2.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }

        // Test case 3: Singular Matrix
        double[][] matrix3 = {
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        };
        RealMatrix inputMatrix3 = new Array2DRowRealMatrix(matrix3);
        try {
            RectangularCholeskyDecomposition rectangularDecomposition3 = new RectangularCholeskyDecomposition(inputMatrix3, 1e-10);
            RealMatrix expectedRootMatrix3 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                    {0.0, 1.0, 0.0},
                    {0.0, 0.0, 1.0}});
            RealMatrix actualRootMatrix3 = rectangularDecomposition3.getRootMatrix();
            assertEquals(expectedRootMatrix3, actualRootMatrix3);
            assertEquals(3, rectangularDecomposition3.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }

        // Test case 4: Non-Positive Definite Matrix
        double[][] matrix4 = {
                {-1.0, -1.0, 0.0},
                {-1.0, 1.0, -1.0},
                {0.0, -1.0, 1.0}
        };
        RealMatrix inputMatrix4 = new Array2DRowRealMatrix(matrix4);
        try {
            RectangularCholeskyDecomposition rectangularDecomposition4 = new RectangularCholeskyDecomposition(inputMatrix4, 1e-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected result
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrix = {
                {1.0, 0.0, -1.0},
                {0.0, 1.0, -1.0},
                {-1.0, -1.0, 1.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrix);
        RectangularCholeskyDecomposition rectangularDecomposition = null;
        try {
            rectangularDecomposition = new RectangularCholeskyDecomposition(inputMatrix, 1e-10);
        } catch (NonPositiveDefiniteMatrixException e) {
            rectangularDecomposition = e;
        }
        assertEquals(-1.0, rectangularDecomposition.getDiagonalElement(), Precision.SAFE_EPSILON);
        assertEquals(2, rectangularDecomposition.getRowIndex());
        assertEquals(1e-10, rectangularDecomposition.getThreshold());
    }
}