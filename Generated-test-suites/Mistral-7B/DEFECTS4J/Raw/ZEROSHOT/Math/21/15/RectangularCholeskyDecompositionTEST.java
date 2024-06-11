import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}.
     */
    @Test
    public void testDecomposition() {
        // Test with positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            int rank = decomp.getRank();
            assertTrue(rank == 3);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865475, 1.414213741317435, 0.0}, {0.0, 0.7071067811865475, 1.414213741317435}});
            assertEquals(expected, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with positive semidefinite matrix with small diagonal values
        RealMatrix smallMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 0.1, 6.0}, {3.0, 6.0, 0.1}});
        try {
            RectangularCholeskyDecomposition decompSmall = new RectangularCholeskyDecomposition(smallMatrix, 0.1);
            RealMatrix rootSmall = decompSmall.getRootMatrix();
            int rankSmall = decompSmall.getRank();
            assertTrue(rankSmall == 3);
            assertEquals(3, rootSmall.getRowDimension());
            assertEquals(rankSmall, rootSmall.getColumnDimension());
            RealMatrix expectedSmall = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.3535533905945596, 0.0}, {0.0, 0.7071067811865476, 0.3535533905945596}});
            assertEquals(expectedSmall, rootSmall);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with non positive semidefinite matrix
        RealMatrix nonPositiveMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -1.0}});
        try {
            RectangularCholeskyDecomposition decompNonPositive = new RectangularCholeskyDecomposition(nonPositiveMatrix, 0.1);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRank()}.
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            int rank = decomp.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix smallMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 0.1, 6.0}, {3.0, 6.0, 0.1}});
        try {
            RectangularCholeskyDecomposition decompSmall = new RectangularCholeskyDecomposition(smallMatrix, 0.1);
            int rankSmall = decompSmall.getRank();
            assertEquals(3, rankSmall);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix nonPositiveMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -1.0}});
        try {
            RectangularCholeskyDecomposition decompNonPositive = new RectangularCholeskyDecomposition(nonPositiveMatrix, 0.1);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRootMatrix()}.
     */
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            int rank = decomp.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            RealVector vector = root.getColumnVector(0);
            assertEquals(1.0, Precision.round(vector.getEntry(0), 15), 0.0);
            assertEquals(0.7071067811865475, Precision.round(vector.getEntry(1), 15), 0.0);
            assertEquals(0.0, Precision.round(vector.getEntry(2), 15), 0.0);
            vector = root.getColumnVector(1);
            assertEquals(0.7071067811865475, Precision.round(vector.getEntry(0), 15), 0.0);
            assertEquals(1.414213741317435, Precision.round(vector.getEntry(1), 15), 0.0);
            assertEquals(0.7071067811865475, Precision.round(vector.getEntry(2), 15), 0.0);
            vector = root.getColumnVector(2);
            assertEquals(0.0, Precision.round(vector.getEntry(0), 15), 0.0);
            assertEquals(0.7071067811865475, Precision.round(vector.getEntry(1), 15), 0.0);
            assertEquals(0.7071067811865475, Precision.round(vector.getEntry(2), 15), 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix smallMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 0.1, 6.0}, {3.0, 6.0, 0.1}});
        try {
            RectangularCholeskyDecomposition decompSmall = new RectangularCholeskyDecomposition(smallMatrix, 0.1);
            RealMatrix rootSmall = decompSmall.getRootMatrix();
            int rankSmall = decompSmall.getRank();
            assertEquals(3, rankSmall);
            assertEquals(3, rootSmall.getRowDimension());
            assertEquals(rankSmall, rootSmall.getColumnDimension());
            RealVector vector = rootSmall.getColumnVector(0);
            assertEquals(1.0, Precision.round(vector.getEntry(0), 15), 0.0);
            assertEquals(0.7071067811865476, Precision.round(vector.getEntry(1), 15), 0.0);
            assertEquals(0.0, Precision.round(vector.getEntry(2), 15), 0.0);
            vector = rootSmall.getColumnVector(1);
            assertEquals(0.7071067811865476, Precision.round(vector.getEntry(0), 15), 0.0);
            assertEquals(0.3535533905945596, Precision.round(vector.getEntry(1), 15), 0.0);
            assertEquals(0.0, Precision.round(vector.getEntry(2), 15), 0.0);
            vector = rootSmall.getColumnVector(2);
            assertEquals(0.0, Precision.round(vector.getEntry(0), 15), 0.0);
            assertEquals(0.3535533905945596, Precision.round(vector.getEntry(1), 15), 0.0);
            assertEquals(0.3535533905945596, Precision.round(vector.getEntry(2), 15), 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix nonPositiveMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -1.0}});
        try {
            RectangularCholeskyDecomposition decompNonPositive = new RectangularCholeskyDecomposition(nonPositiveMatrix, 0.1);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }
}