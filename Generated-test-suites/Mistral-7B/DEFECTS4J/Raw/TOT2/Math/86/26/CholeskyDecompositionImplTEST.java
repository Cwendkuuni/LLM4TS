package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;

import org.junit.Test;

/**
 * Test cases for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the Cholesky decomposition of a square and positive definite matrix.
     */
    @Test
    public void testCholeskyDecomposition() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {3.0, 2.0, 2.0},
            {2.0, 5.0, 1.0},
            {2.0, 1.0, 4.0}
        });
        CholeskyDecompositionImpl decomposed = new CholeskyDecompositionImpl(input);
        RealMatrix expected = new SingularValueDecompositionImpl(input).getL();
        RealMatrix result = decomposed.getL();
        assertEquals(expected, result);
    }

    /**
     * Test the Cholesky decomposition of a square and symmetric matrix.
     */
    @Test
    public void testCholeskyDecompositionSymmetric() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {3.0, 2.0, 2.0},
            {2.0, 3.0, 2.0},
            {2.0, 2.0, 3.0}
        });
        CholeskyDecompositionImpl decomposed = new CholeskyDecompositionImpl(input);
        RealMatrix expected = new SingularValueDecompositionImpl(input).getL();
        RealMatrix result = decomposed.getL();
        assertEquals(expected, result);
    }

    /**
     * Test the Cholesky decomposition of an identity matrix.
     */
    @Test
    public void testCholeskyDecompositionIdentity() {
        RealMatrix input = MatrixUtils.createIdentityMatrix(3);
        CholeskyDecompositionImpl decomposed = new CholeskyDecompositionImpl(input);
        RealMatrix expected = MatrixUtils.createIdentityMatrix(3);
        RealMatrix result = decomposed.getL();
        assertEquals(expected, result);
    }

    /**
     * Test the Cholesky decomposition of a non-square matrix.
     */
    @Test(expected = NotSquareMatrixException.class)
    public void testCholeskyDecompositionNonSquare() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {3.0, 2.0},
            {2.0, 5.0}
        });
        new CholeskyDecompositionImpl(input);
    }

    /**
     * Test the Cholesky decomposition of a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionNonSymmetric() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {3.0, 2.0, 2.0},
            {2.0, 5.0, 1.0},
            {2.0, 1.0, 4.0},
            {1.0, 2.0, 2.0}
        });
        new CholeskyDecompositionImpl(input);
    }

    /**
     * Test the Cholesky decomposition of a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionNonPositiveDefinite() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {-1.0, 0.0, 0.0},
            {0.0, -1.0, 0.0},
            {0.0, 0.0, -1.0}
        });
        new CholeskyDecompositionImpl(input);
    }

    /**
     * Test the Cholesky decomposition solver.
     */
    @Test
    public void testSolver() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {3.0, 2.0, 2.0},
            {2.0, 5.0, 1.0},
            {2.0, 1.0, 4.0}
        });
        CholeskyDecompositionImpl decomposed = new CholeskyDecompositionImpl(input);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        DecompositionSolver solver = decomposed.getSolver();
        RealVector result = solver.solve(b);
        RealVector expected = new RealVectorImpl(new double[]{1.5, -0.25, 0.5});
        assertEquals(expected, result);
    }

    /**
     * Test the Cholesky decomposition solver with a singular matrix.
     */
    @Test(expected = SingularValueDecompositionException.class)
    public void testSolverSingular() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        CholeskyDecompositionImpl decomposed = new CholeskyDecompositionImpl(input);
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        DecompositionSolver solver = decomposed.getSolver();
        solver.solve(b);
    }
}