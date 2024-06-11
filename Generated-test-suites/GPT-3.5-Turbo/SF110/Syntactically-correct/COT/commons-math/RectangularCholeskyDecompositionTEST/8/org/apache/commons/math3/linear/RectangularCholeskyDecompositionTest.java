
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1, 1},
                                                                       {1, 2, 3},
                                                                       {1, 3, 6}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.assertNotNull(decomposition);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void testConstructorWithSmall() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, 1},
                                                                       {12, 2, 9},
                                                                       {1, 9, 6}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.assertNotNull(decomposition);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, 1, 1},
                                                                       {1, 2, 3},
                                                                       {1, 3, 6}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSmallAndNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, 12, 1},
                                                                       {12, 2, 9},
                                                                       {1, 9, 6}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1, 1},
                                                                       {1, 2, 3},
                                                                       {1, 3, 6}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = decomposition.getRootMatrix();
            Assert.assertNotNull(root);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 1, 1},
                                                                       {1, 2, 3},
                                                                       {1, 3, 6}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            int rank = decomposition.getRank();
            Assert.assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception not expected.");
        }
    }
    
}
