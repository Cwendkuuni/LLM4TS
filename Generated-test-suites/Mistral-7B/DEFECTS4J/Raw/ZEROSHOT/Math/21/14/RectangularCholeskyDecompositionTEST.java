import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
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
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0, 0.0},
                    {-0.5 * FastMath.sqrt(2.0), FastMath.sqrt(2.0), 0.0},
                    {-0.5 * FastMath.sqrt(2.0), -0.5 * FastMath.sqrt(2.0), FastMath.sqrt(2.0)}
            });
            assertEquals(expectedRoot, root);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 1.0, -1.0},
            {1.0, 1.0, 2.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getDiagonalElement(), 1e-6);
            assertEquals(2, e.getColumnIndex());
            assertEquals(small, e.getSmall(), 1e-6);
        }
    }

    @org.junit.Test
    public void testRankOne() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 0.0, 0.0},
            {0.0, 2.0, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0},
                    {0.0, FastMath.sqrt(2.0)}
            });
            assertEquals(expectedRoot, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testRankZero() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {0.0, 0.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            new RectangularCholeskyDecomposition(MatrixUtils.createIdentityMatrix(1), small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGetRank() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetRootMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0, 0.0},
                    {-0.5 * FastMath.sqrt(2.0), FastMath.sqrt(2.0), 0.0},
                    {-0.5 * FastMath.sqrt(2.0), -0.5 * FastMath.sqrt(2.0), FastMath.sqrt(2.0)}
            });
            assertEquals(expectedRoot, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}