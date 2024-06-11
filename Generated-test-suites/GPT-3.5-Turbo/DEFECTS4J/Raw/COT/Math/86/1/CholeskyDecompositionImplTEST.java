import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testConstructor() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        assertNotNull(choleskyDecomposition.getL());
        assertNotNull(choleskyDecomposition.getLT());
        assertEquals(matrix.getRowDimension(), choleskyDecomposition.getL().getRowDimension());
        assertEquals(matrix.getColumnDimension(), choleskyDecomposition.getL().getColumnDimension());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrixException() {
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(2, 3);
        new CholeskyDecompositionImpl(nonSquareMatrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorNotSymmetricMatrixException() {
        RealMatrix notSymmetricMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 2},
                {3, 4}
        });
        new CholeskyDecompositionImpl(notSymmetricMatrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorNotPositiveDefiniteMatrixException() {
        RealMatrix notPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {-1, 0},
                {0, -1}
        });
        new CholeskyDecompositionImpl(notPositiveDefiniteMatrix);
    }

    @Test
    public void testGetL() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();

        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                if (j > i) {
                    assertEquals(0.0, l.getEntry(i, j), EPSILON);
                } else {
                    assertTrue(l.getEntry(i, j) > 0);
                    assertTrue(l.getEntry(i, j) <= matrix.getEntry(i, j));
                }
            }
        }
    }

    @Test
    public void testGetLT() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lT = choleskyDecomposition.getLT();

        assertEquals(matrix.getRowDimension(), lT.getRowDimension());
        assertEquals(matrix.getColumnDimension(), lT.getColumnDimension());

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                if (j < i) {
                    assertEquals(0.0, lT.getEntry(i, j), EPSILON);
                } else {
                    assertTrue(lT.getEntry(i, j) > 0);
                    assertTrue(lT.getEntry(i, j) <= matrix.getEntry(i, j));
                }
            }
        }
    }

    @Test
    public void testGetDeterminant() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();

        assertEquals(matrix.getDeterminant(), determinant, EPSILON);
    }

    @Test
    public void testGetSolver() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        assertNotNull(solver);
        assertTrue(solver.isNonSingular());
    }
}