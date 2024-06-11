import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.singularvalues.SingularValueDecomposition;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl_ValidInput()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        Assert.assertEquals(U.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(U.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(S.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(S.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(V.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(V.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{Math.sqrt(5.0), 0.0}, svd.getSingularValues(), 0.01);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_InvalidInput()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 1}});
        new SingularValueDecompositionImpl(matrix); // Throws InvalidMatrixException
    }

    @Test
    public void testSingularValueDecompositionImpl_ZeroMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2;
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        Assert.assertEquals(U.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(U.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(S.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(S.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(V.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(V.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{1.0, 0.0}, svd.getSingularValues(), 0.01);
    }

    @Test
    public void testSingularValueDecompositionImpl_DiagonalMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 2}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        Assert.assertEquals(U.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(U.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(S.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(S.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(V.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(V.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{Math.sqrt(1.0), Math.sqrt(2.0)}, svd.getSingularValues(), 0.01);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 1}});
        RealVector b = new ArrayRealVector(new double[]{1, 2});
        RealVector x = solver.solve(b);
        RealMatrix X = new Array2DRowRealMatrix(new double[][]{{x.getEntry(0), x.getEntry(1)},
                {-x.getEntry(1), x.getEntry(0)}});
        RealMatrix Ax = A.multiply(X);
        RealVector AxVector = Ax.operate(new ArrayRealVector(new double[]{1, 2}));
        RealVector bVector = RealVectorFactory.getInstance().createVector(new double[]{1, 2});
        RealVector error = bVector.subtract(AxVector);
        Assert.assertEquals(Math.sqrt(Math.pow(error.getEntry(0), 2) + Math.pow(error.getEntry(1), 2)), 0.0, 0.01);
        RealMatrix C = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix pseudoInv = svd.getSolver().getInverse();
        RealMatrix X_expected = pseudoInv.multiply(new Array2DRowRealMatrix(new double[][]{{1, -1}, {1, 1}}));
        Assert.assertEquals(X.getRowDimension(), X_expected.getRowDimension());
        Assert.assertEquals(X.getColumnDimension(), X_expected.getColumnDimension());
        Assert.assertTrue(MatrixUtils.isEqual(X, X_expected, 0.01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSolver_InvalidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}, {3, 1}});
        solver.solve(new ArrayRealVector(new double[]{1, 2, 3})); // Throws IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSolver_NonSquareMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getSolver(); // Throws IllegalArgumentException
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRank_SingularMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getRank(); // Throws IllegalStateException
    }

    @Test
    public void testGetRank()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        Assert.assertEquals(rank, 2);
    }

    @Test
    public void testGetRank_ZeroMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2;
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        Assert.assertEquals(rank, 2);
    }

    @Test
    public void testGetCovariance_ValidInput()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}, {3, 4}, {4, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = svd.getSingularValues()[svd.getSingularValues().length - 1];
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{1.0 / (Math.pow(minSingularValue, 2)), 0.0},
                {0.0, 1.0 / (Math.pow(minSingularValue, 2))}});
        Assert.assertEquals(covariance.getRowDimension(), 2);
        Assert.assertEquals(covariance.getColumnDimension(), 2);
        Assert.assertTrue(MatrixUtils.isEqual(covariance, expectedCovariance, 0.01));
    }

    @Test
    public void testGetCovariance_ZeroMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2;
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.0);
        Assert.assertEquals(covariance.getRowDimension(), 2);
        Assert.assertEquals(covariance.getColumnDimension(), 2);
        Assert.assertEquals(covariance, RealMatrixFactory.IDENTITY_2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCovariance_InvalidInput()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}, {3, 4}, {4, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = svd.getSingularValues()[svd.getSingularValues().length - 1] * -1;
        svd.getCovariance(minSingularValue); // Throws IllegalArgumentException
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCovariance_SingularMatrix()
            throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(svd.getSingularValues()[svd.getSingularValues().length - 1]); // Throws IllegalStateException
    }
}