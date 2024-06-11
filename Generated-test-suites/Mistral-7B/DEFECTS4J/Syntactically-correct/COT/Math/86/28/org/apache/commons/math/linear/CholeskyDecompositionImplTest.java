package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.decomposition.CholeskyDecompositionImpl;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.special.Constant;
import org.junit.Test;

/**
 * Test class for CholeskyDecompositionImpl
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test constructor with default threshold values
     */
    @Test
    public void testDefaultConstructor() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 4.0},
            {3.0, 4.0, 9.0}
        }));
        assertNotNull(decomp);
    }

    /**
     * Test constructor with custom threshold values
     */
    @Test
    public void testCustomConstructor() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(new double[][] {
            {1.0E-6, 2.0E-6, 3.0E-6},
            {2.0E-6, 5.0E-6, 4.0E-6},
            {3.0E-6, 4.0E-6, 9.0E-6}
        }), 1.0e-13, 1.0e-15);
        assertNotNull(decomp);
    }

    /**
     * Test Cholesky decomposition of identity matrix
     */
    @Test
    public void testIdentityMatrix() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(MatrixUtils.createIdentityMatrix(3));
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        assertTrue(l.isSimilarTo(lt));
        assertTrue(l.isIdentityMatrix());
    }

    /**
     * Test Cholesky decomposition of non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
    }

    /**
     * Test Cholesky decomposition of non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 2.0}
        }));
    }

    /**
     * Test Cholesky decomposition of non-positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(new double[][] {
            {1.0, 1.0},
            {1.0, -1.0}
        }));
    }

    /**
     * Test getL() method
     */
    @Test
    public void testGetL() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(MatrixUtils.createIdentityMatrix(3));
        RealMatrix l = decomp.getL();
        assertTrue(l.isSimilarTo(decomp.getLT().transpose()));
    }

    /**
     * Test getLT() method
     */
    @Test
    public void testGetLT() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(MatrixUtils.createIdentityMatrix(3));
        RealMatrix lt = decomp.getLT();
        assertTrue(decomp.getL().isSimilarTo(lt.transpose()));
    }

    /**
     * Test getDeterminant() method
     */
    @Test
    public void testGetDeterminant() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(MatrixUtils.createIdentityMatrix(3));
        assertEquals(Constant.ONE, decomp.getDeterminant(), 1.0e-13);
    }

    /**
     * Test getSolver() method
     */
    @Test
    public void testGetSolver() {
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(MatrixUtils.createIdentityMatrix(3));
        DecompositionSolver solver = decomp.getSolver();
        RealVector x = solver.solve(new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 1.0}
        }).transpose().toRealVector());
        assertEquals(new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 1.0}
        }).transpose().toVector(), x, 1.0e-13);
    }

}