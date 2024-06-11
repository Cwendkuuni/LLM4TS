import org.apache.commons.math3.linear.Array2DRandomAccessRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test construction with valid input.
     */
    @Test
    public void testValidConstruction() {
        double[][] inputMatrix = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRandomAccessRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.5, 1.5}, {0.0, 0.0, 3.1622776601683795}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not expected", expectedRoot, actualRoot);
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not expected", expectedRank, actualRank);
    }

    /**
     * Test construction with non-symmetric matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonSymmetricConstruction() {
        double[][] inputMatrix = {
            {1.0, 2.0},
            {2.0, -1.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test construction with non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteConstruction() {
        double[][] inputMatrix = {
            {1.0, 2.0},
            {2.0, -1.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
    }

    /**
     * Test construction with very small diagonal elements.
     */
    @Test
    public void testVerySmallDiagonal() {
        double[][] inputMatrix = {
            {1.0e-20, 2.0, 3.0},
            {2.0, 5.0e-20, 6.0},
            {3.0, 6.0, 1.0e-20}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 1.0e-18;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRandomAccessRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.5, 1.5}, {0.0, 0.0, 0.0}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not expected", expectedRoot, actualRoot);
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not expected", expectedRank, actualRank);
    }

    /**
     * Test construction with small diagonal elements.
     */
    @Test
    public void testSmallDiagonal() {
        double[][] inputMatrix = {
            {1.0, 2.0, 3.0},
            {2.0, 0.1, 6.0},
            {3.0, 6.0, 1.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertTrue("Exception should be thrown", decomposition == null);
    }

    /**
     * Test construction with very small tolerance value.
     */
    @Test
    public void testVerySmallTolerance() {
        double[][] inputMatrix = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRandomAccessRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 2.5, 1.5}, {0.0, 0.0, 3.1622776601683795}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not expected", expectedRoot, actualRoot);
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not expected", expectedRank, actualRank);
    }

    /**
     * Test construction with zero tolerance value.
     */
    @Test
    public void testZeroTolerance() {
        double[][] inputMatrix = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertTrue("Exception should be thrown", decomposition == null);
    }

    /**
     * Test construction with negative tolerance value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTolerance() {
        double[][] inputMatrix = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRandomAccessRealMatrix(inputMatrix);
        double small = -0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test construction with empty matrix.
     */
    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = RealMatrix.IDENTITY_2;
        MatrixUtils.setIdentity(matrix, 1, 1);
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = MatrixUtils.createRealIdentityMatrix(1);
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not expected", expectedRoot, actualRoot);
        int expectedRank = 1;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not expected", expectedRank, actualRank);
    }
}