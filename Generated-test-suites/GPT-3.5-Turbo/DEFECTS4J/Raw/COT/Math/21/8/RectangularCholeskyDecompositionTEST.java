import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4);
        matrix.setEntry(0, 1, 12);
        matrix.setEntry(0, 2, -16);
        matrix.setEntry(1, 0, 12);
        matrix.setEntry(1, 1, 37);
        matrix.setEntry(1, 2, -43);
        matrix.setEntry(2, 0, -16);
        matrix.setEntry(2, 1, -43);
        matrix.setEntry(2, 2, 98);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        RealMatrix expected = MatrixUtils.createRealMatrix(3, 3);
        expected.setEntry(0, 0, 2);
        expected.setEntry(0, 1, 6);
        expected.setEntry(0, 2, -4);
        expected.setEntry(1, 0, 0);
        expected.setEntry(1, 1, 1);
        expected.setEntry(1, 2, -5);
        expected.setEntry(2, 0, 0);
        expected.setEntry(2, 1, 0);
        expected.setEntry(2, 2, 3);

        Assert.assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4);
        matrix.setEntry(0, 1, 12);
        matrix.setEntry(0, 2, -16);
        matrix.setEntry(1, 0, 12);
        matrix.setEntry(1, 1, 37);
        matrix.setEntry(1, 2, -43);
        matrix.setEntry(2, 0, -16);
        matrix.setEntry(2, 1, -43);
        matrix.setEntry(2, 2, 98);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

        Assert.assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 4);
        matrix.setEntry(0, 1, 12);
        matrix.setEntry(0, 2, -16);
        matrix.setEntry(1, 0, 12);
        matrix.setEntry(1, 1, 37);
        matrix.setEntry(1, 2, -43);
        matrix.setEntry(2, 0, -15);
        matrix.setEntry(2, 1, -43);
        matrix.setEntry(2, 2, 98);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }

}