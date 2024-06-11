import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        double[][] data = {{1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.assertNotNull(decomposition);
            Assert.assertEquals(matrix, decomposition.getRootMatrix());
            Assert.assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-1, 0}, {0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithSmallThreshold() {
        double[][] data = {{1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
            Assert.assertNotNull(decomposition);
            Assert.assertEquals(matrix, decomposition.getRootMatrix());
            Assert.assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSmallThresholdAndNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-1, 0}, {0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
}