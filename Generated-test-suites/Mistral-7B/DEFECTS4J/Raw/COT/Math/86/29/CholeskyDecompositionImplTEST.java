import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithRealMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();
        assertEquals(1.0, l.getEntry(0, 0), 1e-15);
        assertEquals(2.0, l.getEntry(0, 1), 1e-15);
        assertEquals(2.0, l.getEntry(1, 0), 1e-15);
        assertEquals(3.0, l.getEntry(1, 1), 1e-15);
        assertArrayEquals(new double[]{-1.4142135625, 0.7071067812}, lT.getRowVector(0).toArray(), 1e-15);
        assertArrayEquals(new double[]{0.7071067812, 0.7071067812}, lT.getRowVector(1).toArray(), 1e-15);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNotSymmetricMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNotPositiveDefiniteMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertEquals(l.getRowDimension(), l.getColumnDimension());
        assertEquals(1.0, l.getEntry(0, 0), 1e-15);
        assertEquals(2.0, l.getEntry(0, 1), 1e-15);
        assertEquals(2.0, l.getEntry(1, 0), 1e-15);
        assertEquals(3.0, l.getEntry(1, 1), 1e-15);
    }

    @Test
    public void testGetLT() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lT = choleskyDecomposition.getLT();
        assertEquals(lT.getRowDimension(), matrix.getRowDimension());
        assertEquals(lT.getColumnDimension(), matrix.getColumnDimension());
        assertArrayEquals(new double[]{-1.4142135625, 0.7071067812}, lT.getRowVector(0).toArray(), 1e-15);
        assertArrayEquals(new double[]{0.7071067812, 0.7071067812}, lT.getRowVector(1).toArray(), 1e-15);
    }

    @Test
    public void testGetDeterminant() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(3.0, choleskyDecomposition.getDeterminant(), 1e-15);
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{3.0, -1.0, -1.0}, {-1.0, 2.0, 2.0}, {-1.0, 2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(1.0/3.0, x.getEntry(0), 1e-15);
        assertEquals(1.0/2.0, x.getEntry(1), 1e-15);
        assertEquals(1.0/3.0, x.getEntry(2), 1e-15);
    }

}