package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NotSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.junit.Test;

/**
 * Test class for CholeskyDecompositionImpl
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix)
     */
    @Test
    public void testCholeskyDecompositionImplConstructorRealMatrix() {
        // Test identity matrix
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        // Test L and LT
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        assertNotNull(l);
        assertNotNull(lt);
        // Test determinant
        assertEquals(1.0, decomp.getDeterminant(), 1e-15);
        // Test solver
        double[] b = new double[] {1.0, 2.0, 3.0};
        RealVector x = decomp.getSolver().solve(b);
        assertArrayEquals(new double[] {1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0), 1.0}, x.toArray(), 1e-15);

        // Test exception: non-square matrix
        try {
            RealMatrix nonSquareMatrix = MatrixUtils.createRealIdentityMatrix(2).addColumn(new double[] {1.0});
            new CholeskyDecompositionImpl(nonSquareMatrix);
            fail();
        } catch (NotSquareMatrixException e) {
            // expected
        }

        // Test exception: not symmetric matrix
        try {
            RealMatrix notSymmetricMatrix = new Array2DRowRealMatrix(new double[][] {
                    {1.0, 2.0, 3.0},
                    {4.0, 1.0, 5.0}
            });
            new CholeskyDecompositionImpl(notSymmetricMatrix);
            fail();
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test exception: not positive definite matrix
        try {
            RealMatrix notPositiveDefiniteMatrix = new Array2DRowRealMatrix(new double[][] {
                    {-1.0, 0.0, 0.0},
                    {0.0, -1.0, 0.0},
                    {0.0, 0.0, -1.0}
            });
            new CholeskyDecompositionImpl(notPositiveDefiniteMatrix);
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test constructor CholeskyDecompositionImpl(RealMatrix, double, double)
     */
    @Test
    public void testCholeskyDecompositionImplConstructorRealMatrixDoubleDouble() {
        // Test identity matrix with default thresholds
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        // Test L and LT
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        assertNotNull(l);
        assertNotNull(lt);
        // Test determinant
        assertEquals(1.0, decomp.getDeterminant(), 1e-15);
        // Test solver
        double[] b = new double[] {1.0, 2.0, 3.0};
        RealVector x = decomp.getSolver().solve(b);
        assertArrayEquals(new double[] {1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0), 1.0}, x.toArray(), 1e-15);

        // Test identity matrix with custom thresholds
        double relativeSymmetryThreshold = 1e-10;
        double absolutePositivityThreshold = 1e-16;
        CholeskyDecompositionImpl decompCustom = new CholeskyDecompositionImpl(matrix, relativeSymmetryThreshold, absolutePositivityThreshold);
        // Test L and LT
        RealMatrix lCustom = decompCustom.getL();
        RealMatrix ltCustom = decompCustom.getLT();
        assertNotNull(lCustom);
        assertNotNull(ltCustom);
        // Test determinant
        assertEquals(1.0, decompCustom.getDeterminant(), 1e-16);
        // Test solver
        double[] bCustom = new double[] {1.0, 2.0, 3.0};
        RealVector xCustom = decompCustom.getSolver().solve(bCustom);
        assertArrayEquals(new double[] {1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0), 1.0}, xCustom.toArray(), 1e-16);

        // Test exception: non-square matrix
        try {
            RealMatrix nonSquareMatrix = MatrixUtils.createRealIdentityMatrix(2).addColumn(new double[] {1.0});
            new CholeskyDecompositionImpl(nonSquareMatrix, relativeSymmetryThreshold, absolutePositivityThreshold);
            fail();
        } catch (NotSquareMatrixException e) {
            // expected
        }

        // Test exception: not symmetric matrix
        try {
            RealMatrix notSymmetricMatrix = new Array2DRowRealMatrix(new double[][] {
                    {1.0, 2.0, 3.0},
                    {4.0, 1.0, 5.0}
            });
            new CholeskyDecompositionImpl(notSymmetricMatrix, relativeSymmetryThreshold, absolutePositivityThreshold);
            fail();
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test exception: not positive definite matrix
        try {
            RealMatrix notPositiveDefiniteMatrix = new Array2DRowRealMatrix(new double[][] {
                    {-1.0, 0.0, 0.0},
                    {0.0, -1.0, 0.0},
                    {0.0, 0.0, -1.0}
            });
            new CholeskyDecompositionImpl(notPositiveDefiniteMatrix, relativeSymmetryThreshold, absolutePositivityThreshold);
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

    }

    /**
     * Test method getSolver()
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomp.getSolver();
        assertNotNull(solver);
    }

    /**
     * Test method getDeterminant()
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0, decomp.getDeterminant(), 1e-15);

        RealMatrix matrixSingular = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });
        try {
            new CholeskyDecompositionImpl(matrixSingular);
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

    }

    /**
     * Test method solve(RealVector)
     */
    @Test
    public void testSolveRealVector() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealVector b = new RealVectorImpl(new double[] {1.0, 2.0, 3.0});
        RealVector x = decomp.getSolver().solve(b);
        assertArrayEquals(new double[] {1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0), 1.0}, x.toArray(), 1e-15);

        RealMatrix matrixSingular = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });
        try {
            CholeskyDecompositionImpl decompSingular = new CholeskyDecompositionImpl(matrixSingular);
            RealVector bSingular = new RealVectorImpl(new double[] {1.0, 1.0, 1.0});
            decompSingular.getSolver().solve(bSingular);
            fail();
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    /**
     * Test method solve(RealMatrix)
     */
    @Test
    public void testSolveRealMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        RealMatrix x = decomp.getSolver().solve(b);
        SingularValueDecomposition svd = new SingularValueDecomposition(b);
        RealMatrix expected = svd.getS();
        assertEquals(expected.getRowDimension(), x.getRowDimension());
        assertEquals(expected.getColumnDimension(), x.getColumnDimension());
        for (int i = 0; i < x.getRowDimension(); i++) {
            for (int j = 0; j < x.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), x.getEntry(i, j), 1e-15);
            }
        }
    }

    /**
     * Test method getInverse()
     */
    @Test
    public void testGetInverse() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = decomp.getInverse();
        assertNotNull(inverse);
        assertEquals(3, inverse.getRowDimension());
        assertEquals(3, inverse.getColumnDimension());
        for (int i = 0; i < inverse.getRowDimension(); i++) {
            for (int j = 0; j < inverse.getColumnDimension(); j++) {
                assertEquals(1.0 / matrix.getEntry(j, i), inverse.getEntry(i, j), 1e-15);
            }
        }
    }

}