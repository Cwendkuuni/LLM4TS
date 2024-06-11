import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 2.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertNotNull(choleskyDecomposition.getL());
        assertNotNull(choleskyDecomposition.getLT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0},
            {1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.1},
            {1.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, -1.0}
        });
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {3.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertEquals(3, l.getRowDimension());
        assertEquals(3, l.getColumnDimension());
        assertNull(choleskyDecomposition.getCachedL());
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {3.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertEquals(3, lt.getRowDimension());
        assertEquals(3, lt.getColumnDimension());
        assertNull(choleskyDecomposition.getCachedLT());
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {3.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(36.0, determinant, 1e-14);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {3.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double[] b = {1.0, 0.0, 0.0};
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(0.25, x.getEntry(0), 1e-14);
        assertEquals(-0.416666666666667, x.getEntry(1), 1e-14);
        assertEquals(0.5, x.getEntry(2), 1e-14);
    }

}