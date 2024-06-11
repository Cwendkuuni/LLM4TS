import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 13.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = choleskyDecomposition.getL();
        assertNotNull(l);
        assertEquals(matrix.getRowDimension(), l.getRowDimension());
        assertEquals(matrix.getColumnDimension(), l.getColumnDimension());

        RealMatrix lt = choleskyDecomposition.getLT();
        assertNotNull(lt);
        assertEquals(matrix.getRowDimension(), lt.getRowDimension());
        assertEquals(matrix.getColumnDimension(), lt.getColumnDimension());

        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(1.0 * 5.0 * 13.0, determinant, 1e-15);

        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealVector(new double[][]{{1.0}, {-1.0}, {1.5}});
        RealVector x = solver.solve(b);
        assertNotNull(x);
        assertEquals(3, x.getDimension());
        assertArrayEquals(new double[]{1.0 / Math.sqrt(1.0), -1.0 / Math.sqrt(5.0), 1.5 / (5.0 * Math.sqrt(13.0))}, x.toArray(), 1e-15);
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testNotSquareMatrixException() {
        double[][] matrixData = {
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] matrixData = {
                {1.0, 1.1},
                {2.0, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0001, 0.0001);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] matrixData = {
                {1.0, 2.0},
                {2.0, -1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0001, 0.0001);
    }

}