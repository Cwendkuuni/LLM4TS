import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testValidDecomposition() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0, -FastMath.sqrt(2.0 / 3.0)},
                    {0.0, FastMath.sqrt(2.0), FastMath.sqrt(2.0 / 3.0)},
                    {FastMath.sqrt(2.0 / 3.0), -FastMath.sqrt(2.0 / 3.0), FastMath.sqrt(2.0 / 3.0)}
            });

            List<Pair<Integer, Double>> diagonalElements = RealMatrixUtils.diagonalEntries(matrix);
            for (int i = 0; i < rank; i++) {
                Assert.assertTrue("Expected diagonal element " + (i + 1) + " to be greater than threshold: " + diagonalElements.get(i).getSecond(), diagonalElements.get(i).getSecond() > small);
            }

            Assert.assertEquals("Expected decomposition matrix to be equal to expected root matrix", expectedRoot, root);
            Assert.assertEquals("Expected rank to be " + rank, rank, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testZeroDiagonalElement() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 0.0, 1.0},
                {1.0, 1.0, 2.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomposition.getRank();
            Assert.assertEquals("Expected rank to be 2", 2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("Expected NonPositiveDefiniteMatrixException with correct message", "The matrix is not positive semidefinite.", e.getMessage());
        }
    }

    @Test
    public void testNegativeDiagonalElement() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, -2.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomposition.getRank();
            Assert.assertEquals("Expected rank to be 2", 2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("Expected NonPositiveDefiniteMatrixException with correct message", "The matrix is not positive semidefinite.", e.getMessage());
        }
    }

    @Test
    public void testIdentityMatrix() {
        double small = 1e-6;
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            Assert.assertEquals("Expected decomposition matrix to be identity", matrix, root);
            Assert.assertEquals("Expected rank to be 5", 5, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testSingularMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0},
                {1.0, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomposition.getRank();
            Assert.assertEquals("Expected rank to be 1", 1, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testZeroMatrix() {
        double small = 1e-6;
        RealMatrix matrix = RealMatrixUtils.createZeroMatrix(3, 3);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            int rank = decomposition.getRank();
            Assert.assertEquals("Expected rank to be 0", 0, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testReshapeZeroMatrix() {
        double small = 1e-6;
        RealMatrix matrix = RealMatrixUtils.createZeroMatrix(3, 3);
        RealMatrix expectedMatrix = RealMatrixUtils.createIdentityMatrix(1);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(RealMatrixUtils.columnRealVector(expectedMatrix.getData()), small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            Assert.assertEquals("Expected decomposition matrix to be identity", expectedMatrix, root);
            Assert.assertEquals("Expected rank to be 1", 1, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testSmallMatrix() {
        double small = 1e-9;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            Assert.assertEquals("Expected decomposition matrix to be identity", matrix, root);
            Assert.assertEquals("Expected rank to be 2", 2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, -1.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("Expected NonPositiveDefiniteMatrixException with correct message", "The matrix is not positive semidefinite.", e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixExceptionWithNegativeDiagonalElement() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, -1.01}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("Expected NonPositiveDefiniteMatrixException with correct message", "The matrix is not positive semidefinite.", e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixExceptionWithZeroDiagonalElement() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 0.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals("Expected NonPositiveDefiniteMatrixException with correct message", "The matrix is not positive semidefinite.", e.getMessage());
        }
    }

}