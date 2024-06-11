import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)});
        Assert.assertArrayEquals(expectedX.toArray(), x.toArray(), 0.0);

        RealMatrix inverse = choleskyDecomposition.getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealIdentityMatrix(2);
        Assert.assertEquals(expectedInverse, inverse);
    }

    @Test
    public void testSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecomposition choleskyDecomposition = null;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
        Assert.assertNull(choleskyDecomposition);

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecomposition choleskyDecomposition2 = null;
        try {
            choleskyDecomposition2 = new CholeskyDecompositionImpl(matrix2, 1.0e-9, 1.0e-9);
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
        Assert.assertNull(choleskyDecomposition2);

        RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 2.0}});
        CholeskyDecomposition choleskyDecomposition3 = new CholeskyDecompositionImpl(matrix3, 1.0e-9, 1.0e-9);
        RealMatrix l = choleskyDecomposition3.getL();
        RealMatrix lt = choleskyDecomposition3.getLT();
        DecompositionSolver solver = choleskyDecomposition3.getSolver();

        RealVector b = new RealVectorImpl(new double[]{1.0, 0.0, 1.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{-0.3361865642813863, 0.4999999999999999, -0.4472135954999579});
        Assert.assertArrayEquals(expectedX.toArray(), x.toArray(), 0.0);

        RealMatrix inverse = choleskyDecomposition3.getInverse();
        SingularValueDecomposition svd = new SingularValueDecomposition(MatrixUtils.createRealIdentityMatrix(3));
        RealMatrix expectedInverse = svd.getTranspose().getMatrixA();
        Assert.assertEquals(expectedInverse, inverse);
    }

    @Test
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}});
        CholeskyDecomposition choleskyDecomposition = null;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NonSquareMatrixException e) {
            // expected
        }
        Assert.assertNull(choleskyDecomposition);
    }

    @Test
    public void testNegativeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, -1.0}});
        CholeskyDecomposition choleskyDecomposition = null;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
        Assert.assertNull(choleskyDecomposition);
    }

    @Test
    public void testOffDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0e-14}, {1.0e-14, 1.0}});
        CholeskyDecomposition choleskyDecomposition = null;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
        Assert.assertNull(choleskyDecomposition);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-9, 1.0e-9);
        double determinant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(3.1622776601683795E-17, determinant, 0.0);
    }

    @Test
    public void testSolveVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-9, 1.0e-9);
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{0.5, 0.5000000000000001});
        Assert.assertArrayEquals(expectedX.toArray(), x.toArray(), 0.0);
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-9, 1.0e-9);
        RealMatrix b = MatrixUtils.createRealIdentityMatrix(2);
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        RealMatrix expectedX = MatrixUtils.createRealIdentityMatrix(2);
        Assert.assertEquals(expectedX, x);
    }

}