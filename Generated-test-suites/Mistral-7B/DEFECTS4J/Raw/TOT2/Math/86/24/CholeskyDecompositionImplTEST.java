package org.apache.commons.math.linear.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.NotPositiveDefiniteMatrixException;
import org.junit.Test;

import org.apache.commons.math.linear.CholeskyDecomposition;
import org.apache.commons.math.linear.CholeskyDecompositionImpl;

public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor without arguments
     */
    @Test
    public void testConstructorWithoutArguments() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        assertArrayEquals(new double[][] {
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 2.0},
            {1.0, 2.0, 3.0}
        }, l.getData(), 0.0);
    }

    /**
     * Test the constructor with arguments
     */
    @Test
    public void testConstructorWithArguments() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        RealMatrix l = cholesky.getL();
        assertArrayEquals(new double[][] {
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 2.0},
            {1.0, 2.0, 3.0}
        }, l.getData(), 0.0);
    }

    /**
     * Test non-square matrix exception
     */
    @Test
    public void testNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0},
            {2.0, 3.0}
        });
        assertThrows(NonSquareMatrixException.class, () -> new CholeskyDecompositionImpl(matrix));
    }

    /**
     * Test non-symmetric matrix exception
     */
    @Test
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 1.0}
        });
        assertThrows(NotSymmetricMatrixException.class, () -> new CholeskyDecompositionImpl(matrix));
    }

    /**
     * Test not positive definite matrix exception
     */
    @Test
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, -1.0}
        });
        assertThrows(NotPositiveDefiniteMatrixException.class, () -> new CholeskyDecompositionImpl(matrix));
    }

    /**
     * Test getSolver method
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new RealVectorImpl(new double[] {1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertArrayEquals(new double[] {-0.154308041, -0.433012756, -0.621616471}, x.toArray(), 0.0);
    }

    /**
     * Test determinant method
     */
    @Test
    public void testDeterminant() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        assertEquals(1.2676506e13, cholesky.getDeterminant(), 1.0e-15);
    }

    /**
     * Test getL method with L matrix already computed
     */
    @Test
    public void testGetLWithLComputed() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        RealMatrix l = MatrixUtils.createRealMatrix(new double[][] {
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 2.0},
            {1.0, 2.0, 3.0}
        });
        cholesky.setLT(l);
        RealMatrix l2 = cholesky.getL();
        assertArrayEquals(l.getData(), l2.getData(), 0.0);
    }

    /**
     * Test getLT method with LT matrix already computed
     */
    @Test
    public void testGetLTWithLTComputed() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        RealMatrix lt = MatrixUtils.createRealMatrix(new double[][] {
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 2.0},
            {1.0, 2.0, 3.0}
        });
        cholesky.setLT(lt);
        RealMatrix lt2 = cholesky.getLT();
        assertArrayEquals(lt.getData(), lt2.getData(), 0.0);
    }

    /**
     * Test getLT method with LT not computed
     */
    @Test
    public void testGetLTWithLTNotComputed() {
        RealMatrix matrix = MatrixUtils.createSymmetricMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        RealMatrix lt = MatrixUtils.createRealMatrix(new double[][] {
            {3.0, 2.0, 1.0},
            {2.0, 3.0, 2.0},
            {1.0, 2.0, 3.0}
        });
        assertFalse(cholesky.getLT() instanceof RealMatrix);
        cholesky.setLT(lt);
        RealMatrix lt2 = cholesky.getLT();
        assertArrayEquals(lt.getData(), lt2.getData(), 0.0);
    }

    /**
     * Test getLT method with wrong matrix size
     */
    @Test
    public void testGetLTWithWrongMatrixSize() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0},
            {2.0, 3.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        assertNull(cholesky.getLT());
    }

    /**
     * Test getLT method with not symmetric matrix
     */
    @Test
    public void testGetLTWithNotSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 2.0},
            {3.0, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        assertNull(cholesky.getLT());
    }

    /**
     * Test getLT method with not positive definite matrix
     */
    @Test
    public void testGetLTWithNotPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, -1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        assertNull(cholesky.getLT());
    }

    /**
     * Test solve method with identity matrix
     */
    @Test
    public void testSolveWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        RealVector b = new SingularValueDecompositionImpl(matrix).getSolver().solve(new RealVectorImpl(new double[] {1.0}));
        RealVector x = cholesky.getSolver().solve(b);
        assertEquals(1.0, x.getEntry(0), 1.0e-15);
    }

    /**
     * Test solve method with singular value decomposition
     */
    @Test
    public void testSolveWithSingularValueDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 2.0},
            {2.0, 5.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        SingularValueDecomposition svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = svd.getSolver().solve(new RealVectorImpl(new double[] {2.0}));
        RealVector x = cholesky.getSolver().solve(b);
        assertEquals(1.128379167095513, x.getEntry(0), 1.0e-15);
    }

}