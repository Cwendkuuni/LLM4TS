package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.junit.Test;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix)}
     */
    @Test
    public void testCholeskyDecompositionImpl() {
        double[][] matrixData = { { 4.0, 0.0, 0.0 }, { 0.0, 9.0, 0.0 }, { 0.0, 0.0, 16.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][] { { 2.0, 0.0, 0.0 }, { 0.0, 3.0, 0.0 },
                    { 0.0, 0.0, 4.0 } });
            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] { { 2.0, 0.0, 0.0 }, { 0.0, 3.0, 0.0 },
                    { 0.0, 0.0, 4.0 } }).transpose();

            assertTrue(l.equals(expectedL));
            assertTrue(lt.equals(expectedLT));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)}
     */
    @Test
    public void testCholeskyDecompositionImplWithCustomThresholds() {
        double[][] matrixData = { { 4.0, 1.0e-16, 0.0 }, { 1.0e-16, 9.0, 0.0 }, { 0.0, 0.0, 16.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-13, 1.0e-12);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][] { { 2.0, 0.0, 0.0 }, { 0.0, 3.0, 0.0 },
                    { 0.0, 0.0, 4.0 } });
            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] { { 2.0, 0.0, 0.0 }, { 0.0, 3.0, 0.0 },
                    { 0.0, 0.0, 4.0 } }).transpose();

            assertTrue(l.equals(expectedL));
            assertTrue(lt.equals(expectedLT));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getDeterminant()}
     */
    @Test
    public void testGetDeterminant() {
        double[][] matrixData = { { 1.0, 0.0, 0.0 }, { 0.0, 2.0, 0.0 }, { 0.0, 0.0, 3.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            double determinant = decomposition.getDeterminant();
            double expectedDeterminant = 1.0 * 2.0 * 3.0;

            assertEquals(expectedDeterminant, determinant, 1e-14);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver()}
     */
    @Test
    public void testGetSolver() {
        double[][] matrixData = { { 4.0, 0.0, 0.0 }, { 0.0, 9.0, 0.0 }, { 0.0, 0.0, 16.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = decomposition.getSolver();

            RealVector vector = new RealVectorImpl(new double[] { 1.0, 0.0, 1.0 });
            RealVector solution = solver.solve(vector);

            RealVector expectedSolution = new RealVectorImpl(new double[] { -0.5, 0.14142135625, 0.25859813889 });

            assertArrayEquals(expectedSolution.getDataRef(), solution.getDataRef(), 1e-14);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getL()}
     */
    @Test
    public void testGetL() {
        double[][] matrixData = { { 4.0, 0.0, 0.0 }, { 0.0, 9.0, 0.0 }, { 0.0, 0.0, 16.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomposition.getL();

            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] { { 2.0, 0.0, 0.0 }, { 0.0, 3.0, 0.0 },
                    { 0.0, 0.0, 4.0 } });

            assertTrue(l.equals(expectedL));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getLT()}
     */
    @Test
    public void testGetLT() {
        double[][] matrixData = { { 4.0, 0.0, 0.0 }, { 0.0, 9.0, 0.0 }, { 0.0, 0.0, 16.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix lt = decomposition.getLT();

            RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][] { { 2.0, 0.0, 0.0 }, { 0.0, 3.0, 0.0 },
                    { 0.0, 0.0, 4.0 } });

            assertTrue(lt.equals(expectedLT));
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)} with
     * non-positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionImplWithNonPositiveDefiniteMatrix() {
        double[][] matrixData = { { 4.0, 0.0, 0.0 }, { 0.0, -9.0, 0.0 }, { 0.0, 0.0, 16.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)} with
     * non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionImplWithNonSymmetricMatrix() {
        double[][] matrixData = { { 4.0, 1.0 }, { 1.0, 9.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)} with
     * non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyDecompositionImplWithNonSquareMatrix() {
        double[][] matrixData = { { 4.0, 0.0 }, { 0.0, 9.0 }, { 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getDeterminant()} with singular matrix
     */
    @Test(expected = SingularMatrixException.class)
    public void testGetDeterminantWithSingularMatrix() {
        double[][] matrixData = { { 0.0, 1.0 }, { 1.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
        decomposition.getDeterminant();
    }
}