import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImplConstructor() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(3, choleskyDecomposition.getL().getRowDimension());
        assertEquals(3, choleskyDecomposition.getL().getColumnDimension());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyDecompositionImplNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 3);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionImplNotSymmetricMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3, 4.0);
        matrix.setEntry(1, 2, 5.0);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionImplNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, -1.0);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertEquals(3, l.getRowDimension());
        assertEquals(3, l.getColumnDimension());
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertEquals(3, lt.getRowDimension());
        assertEquals(3, lt.getColumnDimension());
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(determinant, 4.0 * 4.0 * 4.0, 1e-12);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolverVector() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false);
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(x.getDimension(), 3, 0.0);
        assertEquals(x.getEntry(0), 0.3333333333333332, 1e-12);
        assertEquals(x.getEntry(1), -0.4166666666666666, 1e-12);
        assertEquals(x.getEntry(2), 0.25, 1e-12);
    }

    @Test
    public void testSolverMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 4.0);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealMatrix(3, 1, new double[]{1.0, 2.0, 3.0});
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(x.getRowDimension(), 3);
        assertEquals(x.getColumnDimension(), 1);
        assertEquals(x.getEntry(0, 0), 0.3333333333333332, 1e-12);
        assertEquals(x.getEntry(1, 0), -0.4166666666666666, 1e-12);
        assertEquals(x.getEntry(2, 0), 0.25, 1e-12);
    }

}