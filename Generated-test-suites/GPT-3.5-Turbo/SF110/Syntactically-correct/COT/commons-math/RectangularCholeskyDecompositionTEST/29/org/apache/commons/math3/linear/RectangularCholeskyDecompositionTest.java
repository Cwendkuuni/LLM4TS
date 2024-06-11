
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(matrix.getRowDimension(), decomposition.getRank());
    }

    @Test
    public void testConstructorWithSmall() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{9, 6}, {6, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(matrix.getRowDimension(), decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix1() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{-1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix2() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{1, 0}, {0, -1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix3() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = {{4, 2}, {2, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 1e-10;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    // Add more test cases for other methods if needed

}

