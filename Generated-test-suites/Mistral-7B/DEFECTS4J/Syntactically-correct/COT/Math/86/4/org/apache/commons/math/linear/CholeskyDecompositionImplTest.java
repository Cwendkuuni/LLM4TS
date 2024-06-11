package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.special.Cholesky;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImplConstructor() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            assertNotNull(choleskyDecomposition.getL());
            assertNotNull(choleskyDecomposition.getLT());
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCholeskyDecompositionImplConstructorWithDefaultThreshold() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            assertNotNull(choleskyDecomposition.getL());
            assertNotNull(choleskyDecomposition.getLT());
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCholeskyDecompositionImplConstructorWithCustomThreshold() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        double relativeSymmetryThreshold = 1.0e-6;
        double absolutePositivityThreshold = 1.0e-8;

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix,
                    relativeSymmetryThreshold, absolutePositivityThreshold);
            assertNotNull(choleskyDecomposition.getL());
            assertNotNull(choleskyDecomposition.getLT());
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetL() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = choleskyDecomposition.getL();
            assertNotNull(l);
            assertEquals(4, l.getRowDimension());
            assertEquals(4, l.getColumnDimension());
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < i; j++) {
                    assertEquals(0.0, l.getEntry(i, j), 0.0);
                }
                assertEquals(Math.sqrt(matrix.getEntry(i, i)), l.getEntry(i, i), 0.0);
            }
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetLT() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix lt = choleskyDecomposition.getLT();
            assertNotNull(lt);
            assertEquals(4, lt.getRowDimension());
            assertEquals(4, lt.getColumnDimension());
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i == j) {
                        assertEquals(Math.sqrt(matrix.getEntry(j, j)), lt.getEntry(i, j), 0.0);
                    } else {
                        assertEquals(
                                -Cholesky.getSquareRoot(matrix.getEntry(j, i)) * Cholesky.getSquareRoot(matrix.getEntry(i, i)),
                                lt.getEntry(i, j), 0.0);
                    }
                }
            }
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeterminant() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            assertEquals(583.171382835667, choleskyDecomposition.getDeterminant(), 1e-8);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSolver() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = choleskyDecomposition.getSolver();
            assertNotNull(solver);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithIdentityMatrix() {
        double[][] data = {{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = choleskyDecomposition.getSolver();

            RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0, 4.0}, false);
            RealVector x = solver.solve(b);
            assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, x.toArray(), 0.0);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolverWithSymmetricPositiveDefiniteMatrix() {
        double[][] data = {{1.0, 2.0, 2.0, 2.0}, {2.0, 5.0, 3.0, 3.0},
                {2.0, 3.0, 6.0, 4.0}, {2.0, 3.0, 4.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = choleskyDecomposition.getSolver();

            RealVector b = new RealVectorImpl(new double[]{1.0, 1.0, 1.0, 1.0}, false);
            RealVector x = solver.solve(b);
            assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 2.0, 1.0 / 3.0, 1.0}, x.toArray(), 0.0);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException
                | NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithNonSymmetricMatrix() {
        double[][] data = {{1.0, 2.0, 2.0}, {2.0, 5.0, 3.0}, {2.0, 3.0, 6.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = choleskyDecomposition.getSolver();
            solver.solve(MatrixUtils.createRealVector(new double[]{1.0, 1.0, 1.0}, false));
            fail("Unexpected result");
        } catch (NonSquareMatrixException | NotSymmetricMatrixException e) {
            // Expected exception
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSolverWithSingularMatrix() {
        double[][] data = {{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0}, {0.0, 0.0, 1.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = choleskyDecomposition.getSolver();
            solver.solve(MatrixUtils.createRealVector(new double[]{1.0, 1.0, 1.0, 1.0}, false));
            fail("Unexpected result");
        } catch (NonSquareMatrixException | NotSymmetricMatrixException e) {
            // Expected exception
        } catch (InvalidMatrixException e) {
            // Expected exception
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}