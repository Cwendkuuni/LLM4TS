package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.Transforms;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.createRealVector;

/**
 * Test for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        Assert.assertEquals(matrix, lt.transpose());
        RealVector b = createRealVector(new double[] {-3.0, 2.0, 0.0});
        RealVector x = cholesky.getSolver().solve(b);
        Assert.assertEquals(Precision.DEFAULT_EPSILON, b.dotProduct(x), 0.0);
    }

    /**
     * Test the constructor with custom thresholds.
     */
    @Test
    public void testCustomThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0e-10, 1.0e-10, 1.0e-10},
            {1.0e-10, 2.0, 2.0},
            {1.0e-10, 2.0, 3.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-7, 1.0e-7);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        Assert.assertEquals(matrix, lt.transpose());
        RealVector b = createRealVector(new double[] {-3.0e-7, 2.0e-7, 0.0});
        RealVector x = cholesky.getSolver().solve(b);
        Assert.assertEquals(Precision.DEFAULT_EPSILON, b.dotProduct(x), 0.0);
    }

    /**
     * Test that the constructor throws an exception when the matrix is not square.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNotSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 4.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test that the constructor throws an exception when the matrix is not symmetric.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 4.0}
        });
        CholeskyDecompositionImpl cholesky = null;
        try {
            cholesky = new CholeskyDecompositionImpl(matrix);
        } finally {
            if (cholesky != null) {
                cholesky.dispose();
            }
        }
    }

    /**
     * Test that the constructor throws an exception when the matrix is not positive definite.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, -1.0},
            {-1.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the getL() method.
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        Assert.assertEquals(matrix, lt.transpose());
        RealMatrix expectedL = createRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.707106781186548, 0.707106781186548, 0.0},
            {0.408248290463863, 0.816496580927726, 0.816496580927726}
        });
        Assert.assertEquals(expectedL, l);
    }

    /**
     * Test the getLT() method.
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        Assert.assertEquals(matrix, lt.transpose());
        RealMatrix expectedLT = new SingularValueDecomposition(matrix.transpose())
                .getU().transpose();
        Assert.assertEquals(expectedLT, lt);
    }

    /**
     * Test the getDeterminant() method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        Assert.assertEquals(1.0 * 4.0 * 9.0, cholesky.getDeterminant(), 1e-14);
    }

    /**
     * Test the Solver class.
     */
    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = createRealVector(new double[] {-3.0, 2.0, 0.0});
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[] {1.5, -0.5, 0.5}, false);
        Assert.assertEquals(expectedX, x);

        // Test that it handles singular matrices gracefully
        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1.0},
            {1.0, 1.0}
        });
        CholeskyDecompositionImpl choleskySingular = new CholeskyDecompositionImpl(singularMatrix);
        RealVector bSingular = createRealVector(new double[] {-1.0, 1.0});
        try {
            choleskySingular.getSolver().solve(bSingular);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}