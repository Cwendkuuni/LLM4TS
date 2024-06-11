import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] matrixData = {{2, 0, 0}, {0, -1, 0}, {0, 0, -3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.assertEquals(2, decomposition.getRank());
            Assert.assertEquals(matrix, decomposition.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("An unexpected exception was thrown.");
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionException() {
        double[][] matrixData = {{1, 0}, {1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.fail("Expected NonPositiveDefiniteMatrixException was not thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(0, e.getRow());
            Assert.assertEquals(1, e.getCol());
            Assert.assertEquals(matrix.getEntry(0, 1), e.getOffendingElement(), 0.0001);
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmall() {
        double[][] matrixData = {{4, 1}, {1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            Assert.assertEquals(2, decomposition.getRank());
            Assert.assertEquals(matrix, decomposition.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("An unexpected exception was thrown.");
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallException() {
        double[][] matrixData = {{-1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            Assert.fail("Expected NonPositiveDefiniteMatrixException was not thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(0, e.getRow());
            Assert.assertEquals(0, e.getCol());
            Assert.assertEquals(matrix.getEntry(0, 0), e.getOffendingElement(), 0.0001);
        }
    }
}