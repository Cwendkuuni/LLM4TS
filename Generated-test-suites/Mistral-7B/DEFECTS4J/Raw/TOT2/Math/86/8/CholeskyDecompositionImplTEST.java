import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                      {2.0, 3.0, 4.0},
                                                                      {2.0, 4.0, 5.0}});

        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();

        // Check that the L matrix is lower triangular
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, l.getEntry(i, j), 1e-12);
            }
        }

        // Check that the LT matrix is the inverse of the original matrix
        RealMatrix lt = decomp.getLT();
        RealMatrix inverseMatrix = lt.transpose().multiply(l);
        RealMatrix originalMatrix = MatrixUtils.createRealMatrix(matrix.getData());
        assertEquals(originalMatrix, inverseMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                      {2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 1.0},
                                                                      {2.0, 3.0, 4.0},
                                                                      {1.0, 4.0, 5.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                      {2.0, -1.0, 2.0},
                                                                      {2.0, 2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0},
                                                                      {2.0, 2.0}});
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        assertEquals(3.0 * 3.0, decomp.getDeterminant(), 1e-12);
    }

}