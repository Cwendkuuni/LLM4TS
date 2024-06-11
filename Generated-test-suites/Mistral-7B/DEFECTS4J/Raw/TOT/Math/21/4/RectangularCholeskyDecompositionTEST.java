import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Vector2D;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testIdentityMatrix() {
        double tolerance = 1e-12;
        RealMatrix matrix = RealMatrixUtils.identity(5);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix expected = RealMatrixUtils.identity(decomposition.getRank());
            Assert.assertEquals(expected, decomposition.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Identity matrix should be positive semidefinite.");
        }
    }

    @Test
    public void testSingularMatrix() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 0, 1}, {0, 1, 0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix expected = new SingularValueDecomposition(matrix.transpose().multiply(matrix)).getU();
            Assert.assertEquals(expected, decomposition.getRootMatrix().transpose());
        } catch (NonPositiveDefiniteMatrixException e) {
            // Singular matrix is not positive semidefinite
        }
    }

    @Test
    public void testNonSymmetricMatrix() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            // Non-symmetric matrix should throw NonPositiveDefiniteMatrixException
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            fail("Non-symmetric matrix should not have a rectangular Cholesky decomposition.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected result
        }
    }

    @Test
    public void testNegativeDiagonalElement() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, -1}});
        try {
            // Negative diagonal elements should throw NonPositiveDefiniteMatrixException
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            fail("Negative diagonal elements should not be present in a positive semidefinite matrix.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected result
        }
    }

    @Test
    public void testSmallDiagonalElements() {
        double tolerance = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1, 0}, {1, 1, 1}, {0, 1, 1}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 1, 0}, {1, 1, 1}, {0, 0, Math.sqrt(0.5)}});
            Assert.assertEquals(expected, decomposition.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix with small diagonal elements should have a rectangular Cholesky decomposition.");
        }
    }

    @Test
    public void testLargeDiagonalElements() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{10000, 0}, {0, 10000}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix expected = RealMatrixUtils.createRealMatrix(2, 2).setEntry(0, 0, 100);
            Assert.assertEquals(expected, decomposition.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix with large diagonal elements should have a rectangular Cholesky decomposition.");
        }
    }

    @Test
    public void testRankDetermination() {
        double tolerance = 1e-12;
        // Rank 1 matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            Assert.assertEquals(1, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Rank 1 matrix should have a rectangular Cholesky decomposition.");
        }

        // Rank 2 matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 1}, {0, 1, 0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            Assert.assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Rank 2 matrix should have a rectangular Cholesky decomposition.");
        }

        // Rank 3 matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 1}, {0, 0, 1, 1}, {0, 1, 1, 0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            Assert.assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Rank 3 matrix should have a rectangular Cholesky decomposition.");
        }
    }

    @Test
    public void testRootMatrixMultiplication() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{Math.sqrt(5) / 2, -Math.sqrt(5) / 2},
                                                                             {-Math.sqrt(5) / 2, Math.sqrt(5) / 2}});
            RealMatrix product = rootMatrix.multiply(rootMatrix.transpose());
            Assert.assertEquals(MatrixUtils.createRealIdentityMatrix(2), product, tolerance);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Non-symmetric matrix should not have a rectangular Cholesky decomposition.");
        }
    }

    @Test
    public void testRankOneSingularVector() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}, {0, 1}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            Vector2D singularVector = new Vector2D(new double[]{1, 1});
            RealMatrix product = rootMatrix.transpose().multiply(singularVector);
            Assert.assertEquals(new Vector2D(new double[]{1, 0}), product, tolerance);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Rank 1 matrix should have a rectangular Cholesky decomposition.");
        }
    }

    @Test
    public void testRankTwoOrthogonalSingularVectors() {
        double tolerance = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 1}, {0, 1, 0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, tolerance);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            Vector2D v1 = new Vector2D(new double[]{1, 1});
            Vector2D v2 = new Vector2D(new double[]{-1, 1});
            RealMatrix product1 = rootMatrix.transpose().multiply(v1);
            RealMatrix product2 = rootMatrix.transpose().multiply(v2);
            Assert.assertEquals(new Vector2D(new double[]{0, 0}), product1, tolerance);
            Assert.assertEquals(new Vector2D(new double[]{0, 0}), product2, tolerance);
            Assert.assertEquals(1.0, Precision.doubleArrayEquals(v1, v2, tolerance), tolerance);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Rank 2 matrix should have a rectangular Cholesky decomposition.");
        }
    }
}