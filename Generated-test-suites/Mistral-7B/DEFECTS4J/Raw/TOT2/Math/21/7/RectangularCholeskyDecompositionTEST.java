import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructWithIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertEquals(matrix, root.transpose().multiply(root));
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown for identity matrix
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testConstructWithSingularMatrix() {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructWithSmallDiagonal() {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, 0.1, 0.0}, {0.0, 0.0, 0.0} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.1);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        matrixData = new double[][] { {1.0, 0.0, 0.0}, {0.0, 0.1, 0.1}, {0.0, 0.1, 0.1} };
        matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix inverse = new LUDecomposition(matrix).getSolver().getInverse();
            assertEquals(root, inverse);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testConstructWithSymmetricMatrix() {
        double[][] matrixData = { {1.0, 2.0, 2.0}, {2.0, 1.0, 2.0}, {2.0, 2.0, 1.0} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        matrixData = new double[][] { {1.0, 2.0, 2.0}, {2.0, 1.0, 2.0}, {2.0, 2.0, 3.0} };
        matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1.0);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix inverse = new SingularValueDecomposition(matrix).getSolver().getInverse();
            assertEquals(root, inverse);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testConstructWithEmptyMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(1, 1);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructWithNullMatrix() {
        RealMatrix matrix = null;
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            int rank = decomp.getRank();
            assertEquals(rank, 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testGetRankZero() {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, 0.0, 0.0} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            int rank = decomp.getRank();
            assertEquals(rank, 0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testGetRankOne() {
        double[][] matrixData = { {1.0, 0.0} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(1, 2, matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            int rank = decomp.getRank();
            assertEquals(rank, 1);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = { {1.0, 0.0, 0.0}, {0.0, 0.1, 0.1}, {0.0, 0.1, 0.1} };
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(3, 3, matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomp.getRootMatrix();
            assertNotNull(root);
            assertEquals(root.getRowDimension(), 3);
            assertEquals(root.getColumnDimension(), 2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testGetRootMatrixEmpty() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(1, 1);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertNull(root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this should not be thrown
            fail("Should not have thrown NonPositiveDefiniteMatrixException");
        }
    }

}