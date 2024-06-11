import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testValidDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 0.5}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0, 0.5 * FastMath.sqrt(2.0)},
                    {FastMath.sqrt(2.0) / 2.0, FastMath.sqrt(2.0), 0.5 * FastMath.sqrt(2.0) / 2.0},
                    {-FastMath.sqrt(2.0) / 2.0, -FastMath.sqrt(2.0) / 2.0, FastMath.sqrt(0.5)}
            });

            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(expectedRoot, root);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @org.junit.Test
    public void testInvalidDecompositionSmallDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 0.1}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Exception should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(0.1, e.getDiagonalElement(), 0.0);
            assertEquals(2, e.getRowIndex());
        }
    }

    @org.junit.Test
    public void testInvalidDecompositionNegativeDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, -1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Exception should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getDiagonalElement(), 0.0);
            assertEquals(2, e.getRowIndex());
        }
    }

    @org.junit.Test
    public void testRankOneMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0},
                    {-FastMath.sqrt(2.0) / 2.0, FastMath.sqrt(2.0) / 2.0}
            });

            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(expectedRoot, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @org.junit.Test
    public void testRankZeroMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0},
                {1.0, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Exception should be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

}