package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.FieldDimension;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private static final double EPSILON = 1e-12;
    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static final int RANK = 2;

    private RealMatrix matrix;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setUp() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testConstructorWithFullMatrix() {
        // Test constructor with a full matrix (not supported by this implementation).
        try {
            SingularValueDecompositionImpl svd =
                    new SingularValueDecompositionImpl(matrix);
            Assert.fail();
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithMatrixAndMaxSingularValues() {
        SingularValueDecompositionImpl svd =
                new SingularValueDecompositionImpl(matrix, RANK);
        RealMatrix u = svd.getU();
        RealMatrix uT = svd.getUT();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix vT = svd.getVT();
        RealVector singularValues = svd.getSingularValues();
        DecompositionSolver solver = svd.getSolver();

        // Test getting U, UT, S, V, VT, singular values and solver.
        Assert.assertEquals(ROWS, u.getRowDimension());
        Assert.assertEquals(RANK, u.getColumnDimension());
        Assert.assertEquals(ROWS, uT.getRowDimension());
        Assert.assertEquals(RANK, uT.getColumnDimension());
        Assert.assertEquals(RANK, s.getRowDimension());
        Assert.assertEquals(RANK, s.getColumnDimension());
        Assert.assertEquals(COLS, v.getRowDimension());
        Assert.assertEquals(RANK, v.getColumnDimension());
        Assert.assertEquals(ROWS, vT.getRowDimension());
        Assert.assertEquals(RANK, vT.getColumnDimension());

        Assert.assertTrue(u.isOrthogonal());
        Assert.assertTrue(uT.isOrthogonal());
        Assert.assertArrayEquals(new double[]{1.0}, singularValues, EPSILON);

        // Test solving the linear equation A * X = B with A = V * S * U'.
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector x = solver.solve(b);
        RealMatrix Ax = uT.multiply(s).multiply(v);
        RealVector AxVec = Ax.operate(new ArrayRealVector(new double[]{1.0, 1.0, 1.0}));
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, x.toArray(), EPSILON);
        Assert.assertArrayEquals(AxVec.toArray(), b.toArray(), EPSILON);

        // Test getting condition number and rank.
        Assert.assertEquals(Precision.round(MathUtils.sqrt(Math.pow(singularValues[0], 2) +
                Math.pow(singularValues[1], 2)) / Math.abs(singularValues[RANK]), EPSILON),
                svd.getConditionNumber(), EPSILON);
        Assert.assertEquals(RANK, svd.getRank());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceWithNegativeMinSingularValue() {
        svd.getCovariance(-1.0);
    }

    @Test
    public void testGetCovariance() {
        double minSingularValue = singularValues[RANK - 1];
        RealMatrix covariance = svd.getCovariance(minSingularValue);

        // Test getting covariance matrix.
        Assert.assertEquals(RANK, covariance.getRowDimension());
        Assert.assertEquals(RANK, covariance.getColumnDimension());

        // Test that the covariance matrix is symmetric.
        RealMatrix t = covariance.transpose();
        Assert.assertEquals(covariance, t);

        // Test getting eigenvalues of the covariance matrix.
        EigenDecomposition eigDecomposition = new EigenDecompositionImpl(covariance);
        double[] eigenvalues = eigDecomposition.getRealEigenvalues();
        Assert.assertTrue(Precision.isClose(eigenvalues[0], 1.0, EPSILON));
        Assert.assertTrue(Precision.isClose(eigenvalues[RANK - 1], 0.0, EPSILON));
        for (int i = 1; i < eigenvalues.length; ++i) {
            Assert.assertTrue(Precision.isClose(eigenvalues[i], 0.0, EPSILON));
        }

        // Test that the covariance matrix has the same singular values as the original matrix.
        double[] eigenvaluesImpl = svd.getSingularValues();
        for (int i = 0; i < RANK; ++i) {
            Assert.assertTrue(Precision.isClose(eigenvaluesImpl[i], eigenvalues[i], EPSILON));
        }
    }

    @Test
    public void testGetNorm() {
        double norm = svd.getNorm();
        Assert.assertEquals(singularValues[0], norm, EPSILON);
    }

    @Test
    public void testGetRank() {
        int rank = svd.getRank();
        Assert.assertEquals(RANK, rank);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSolverWhenRankIsZero() {
        svd.getRank();
        svd.getSolver();
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = svd.getSolver();
        // Test solving the linear equation A * X = B with A = V * S * U'.
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector x = solver.solve(b);
        RealMatrix Ax = uT.multiply(s).multiply(v);
        RealVector AxVec = Ax.operate(new ArrayRealVector(new double[]{1.0, 1.0, 1.0}));
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, x.toArray(), EPSILON);
        Assert.assertArrayEquals(AxVec.toArray(), b.toArray(), EPSILON);

        // Test if the solver is non-singular.
        Assert.assertTrue(solver.isNonSingular());

        // Test getting inverse matrix.
        RealMatrix inverse = solver.getInverse();
        Assert.assertEquals(ROWS, inverse.getRowDimension());
        Assert.assertEquals(COLS, inverse.getColumnDimension());
        Assert.assertTrue(inverse.isInvertible());
        RealMatrix result = inverse.multiply(svd.getSolver().solve(MatrixUtils.IDENTITY_MATRIX));
        Assert.assertEquals(MatrixUtils.IDENTITY_MATRIX, result, EPSILON);
    }
}