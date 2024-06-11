package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Tests the CholeskyDecompositionImpl constructor with a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.4472, 1.0, 0.0},
                                                                           {0.3162, 0.8660, 1.0}});
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.4472, 0.3162},
                                                                            {0.0, 1.0, 0.8660},
                                                                            {0.0, 0.0, 1.0}});
        assertEquals(expectedL.getEntry(0, 0), l.getEntry(0, 0), 1e-14);
        assertEquals(expectedL.getEntry(0, 1), l.getEntry(0, 1), 1e-14);
        assertEquals(expectedL.getEntry(0, 2), l.getEntry(0, 2), 1e-14);
        assertEquals(expectedL.getEntry(1, 0), l.getEntry(1, 0), 1e-14);
        assertEquals(expectedL.getEntry(1, 1), l.getEntry(1, 1), 1e-14);
        assertEquals(expectedL.getEntry(1, 2), l.getEntry(1, 2), 1e-14);
        assertEquals(expectedL.getEntry(2, 0), l.getEntry(2, 0), 1e-14);
        assertEquals(expectedL.getEntry(2, 1), l.getEntry(2, 1), 1e-14);
        assertEquals(expectedL.getEntry(2, 2), l.getEntry(2, 2), 1e-14);
        assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-14);
    }

    /**
     * Tests the CholeskyDecompositionImpl constructor with a symmetric matrix.
     */
    @Test
    public void testSymmetric() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = null;
        try {
            cholesky = new CholeskyDecompositionImpl(matrix, 1e-14, 1e-14);
            RealMatrix l = cholesky.getL();
            assertNotNull(l);
            RealMatrix lt = cholesky.getLT();
            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                               {0.7071, 0.7071, 0.0},
                                                                               {0.7071, 0.0, 0.7071}});
            RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.7071, 0.7071},
                                                                                {0.0, 0.7071, 0.7071},
                                                                                {0.0, 0.0, 0.7071}});
            assertEquals(expectedL.getEntry(0, 0), l.getEntry(0, 0), 1e-14);
            assertEquals(expectedL.getEntry(0, 1), l.getEntry(0, 1), 1e-14);
            assertEquals(expectedL.getEntry(0, 2), l.getEntry(0, 2), 1e-14);
            assertEquals(expectedL.getEntry(1, 0), l.getEntry(1, 0), 1e-14);
            assertEquals(expectedL.getEntry(1, 1), l.getEntry(1, 1), 1e-14);
            assertEquals(expectedL.getEntry(1, 2), l.getEntry(1, 2), 1e-14);
            assertEquals(expectedL.getEntry(2, 0), l.getEntry(2, 0), 1e-14);
            assertEquals(expectedL.getEntry(2, 1), l.getEntry(2, 1), 1e-14);
            assertEquals(expectedL.getEntry(2, 2), l.getEntry(2, 2), 1e-14);
            assertArrayEquals(expectedLT.getData(), lt.getData(), 1e-14);
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests the CholeskyDecompositionImpl constructor with a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetric() {
        double[][] matrixData = {
            {1.0, 1.0},
            {0.5, 1.5}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = null;
        try {
            cholesky = new CholeskyDecompositionImpl(matrix, 1e-14, 1e-14);
            fail("Should not reach here");
        } catch (NotSymmetricMatrixException e) {
            // Expected exception
        }
    }

    /**
     * Tests the CholeskyDecompositionImpl constructor with a singular matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSingular() {
        double[][] matrixData = {
            {1.0, 0.0},
            {0.0, -1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = null;
        try {
            cholesky = new CholeskyDecompositionImpl(matrix, 1e-14, 1e-14);
            fail("Should not reach here");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    /**
     * Tests the getDeterminant method.
     */
    @Test
    public void testDeterminant() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0 * 5.0 * 10.0, cholesky.getDeterminant(), 1e-14);
    }

    /**
     * Tests the solve method with a vector.
     */
    @Test
    public void testSolveVector() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double[] b = {1.0, 2.0, 3.0};
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector x = cholesky.getSolver().solve(new RealVectorImpl(b));
        double[] xData = x.toArray();
        assertEquals(xData[0], 1.0 / 1.0, 1e-14);
        assertEquals(xData[1], (1.0 / 1.0 - 2.0 * 1.0 / 5.0) / 3.0, 1e-14);
        assertEquals(xData[2], (1.0 / 1.0 - 2.0 * 1.0 / 5.0 - 3.0 * 1.0 / 10.0) / 3.0, 1e-14);
    }

    /**
     * Tests the solve method with a matrix.
     */
    @Test
    public void testSolveMatrix() {
        double[][] matrixData1 = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        double[][] matrixData2 = {
            {1.0, 2.0},
            {3.0, 4.0}
        };
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(matrixData1);
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(matrixData2);
        RealMatrix x = new CholeskyDecompositionImpl(matrix1).getSolver().solve(matrix2);
        RealMatrix expectedX = MatrixUtils.createRealMatrix(new double[][]{{-0.1389, 0.4330},
                                                                            {-0.0811, -0.1389}});
        assertEquals(expectedX.getEntry(0, 0), x.getEntry(0, 0), 1e-14);
        assertEquals(expectedX.getEntry(0, 1), x.getEntry(0, 1), 1e-14);
        assertEquals(expectedX.getEntry(1, 0), x.getEntry(1, 0), 1e-14);
        assertEquals(expectedX.getEntry(1, 1), x.getEntry(1, 1), 1e-14);
    }
}