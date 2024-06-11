import org.apache.commons.math.linear.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private static final double EPSILON = 1e-10;

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 3);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 1, 1.0);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setRow(0, new double[] { -1.0, 0.0 });
        matrix.setRow(1, new double[] { 0.0, -1.0 });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(0, 0);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertTrue(l.isSquare());
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());
        assertEquals(matrix.getData(), l.multiply(l.transpose()).getData());
    }

    @Test
    public void testZeroDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 0.0);
        matrix.setEntry(1, 1, 0.0);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertTrue(l.isSquare());
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());
        assertEquals(matrix.getData(), l.multiply(l.transpose()).getData());
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1e-12);
        matrix.setEntry(1, 1, 1e-12);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertTrue(l.isSquare());
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());
        assertEquals(matrix.getData(), l.multiply(l.transpose()).getData(), EPSILON);
    }
}