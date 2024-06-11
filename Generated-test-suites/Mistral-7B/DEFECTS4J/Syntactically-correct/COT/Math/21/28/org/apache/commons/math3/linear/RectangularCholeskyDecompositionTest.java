import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_validInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, 8.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                                            {0.4472135955223881, 1.0, 0.0, 0.0},
                                                                            {0.7343855583332122, 0.4472135955223881, 1.0, 0.0},
                                                                            {0.95105651610255, 0.7343855583332122, 0.4828393450842476, 1.0}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_nonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 2.0},
                                                                      {2.0, -1.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, -8.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                      {2.0, 3.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, 8.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, 8.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                                            {0.4472135955223881, 1.0, 0.0, 0.0},
                                                                            {0.7343855583332122, 0.4472135955223881, 1.0, 0.0},
                                                                            {0.95105651610255, 0.7343855583332122, 0.4828393450842476, 1.0}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrix_smallRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, 7.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                                            {0.4472135955223881, 1.0, 0.0, 0.0},
                                                                            {0.7343855583332122, 0.4472135955223881, 1.0, 0.0},
                                                                            {0.95105651610255, 0.7343855583332122, 0.4828393450842476, 1.0},
                                                                            {0.0, 0.0, 0.0, 0.9162396923943955}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(4, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix_threshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0e-10, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, 8.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                                            {0.4472135955223881, 1.0, 0.0, 0.0},
                                                                            {0.7343855583332122, 0.4472135955223881, 1.0, 0.0},
                                                                            {0.95105651610255, 0.7343855583332122, 0.4828393450842476, 1.0}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix_threshold_smallRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0e-10, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, 7.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                                            {0.4472135955223881, 1.0, 0.0, 0.0},
                                                                            {0.7343855583332122, 0.4472135955223881, 1.0, 0.0},
                                                                            {0.95105651610255, 0.7343855583332122, 0.4828393450842476, 1.0},
                                                                            {0.0, 0.0, 0.0, 0.9162396923943955}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(4, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix_threshold_negativeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 2.0},
                                                                      {2.0, 2.0, 3.0, 3.0},
                                                                      {2.0, 3.0, 5.0, 6.0},
                                                                      {2.0, 3.0, 6.0, -8.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("NonPositiveDefiniteMatrixException expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(2.0, Precision.round(e.getDiagonal()[2], 4), 0.0);
            assertEquals(2, e.getIndex()[2]);
        }
    }
}