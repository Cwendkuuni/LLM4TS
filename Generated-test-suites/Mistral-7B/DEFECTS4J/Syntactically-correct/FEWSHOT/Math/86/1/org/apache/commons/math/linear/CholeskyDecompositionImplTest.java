package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            double determinant = decomposition.getDeterminant();

            assertEquals(l.getRowDimension(), lt.getColumnDimension(), 0);
            assertEquals(l.getRowDimension(), l.getColumnDimension(), 0);
            assertEquals(lt.getRowDimension(), lt.getColumnDimension(), 0);
            assertArrayEquals(new double[]{}, l.getData()[0], 0.0);
            assertArrayEquals(new double[]{}, l.getData()[1], 0.0);
            assertArrayEquals(new double[]{}, l.getData()[2], 0.0);

            assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.4494, 0.8989, 0.0},
                    {0.2667, -0.4441, 0.9355}}, l.getData(), 0.0001);

            assertArrayEquals(new double[][]{
                    {1.0, 0.4494, 0.2667},
                    {0.1, 0.8989, -0.4441},
                    {0.2, 0.0, 0.9355}}, lt.getData(), 0.0001);

            assertEquals(1.0, determinant, 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCholeskyDecompositionImplWithNegativeDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, -1.0, 0.3}, {0.2, 0.3, 1.0}});

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testCholeskyDecompositionImplWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0, 0.3}});

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test
    public void testCholeskyDecompositionImplWithNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}, {0.2, 0.3, 1.0, 1.0}});

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = decomposition.getSolver();

            double[] b = new double[]{1.0, 2.0, 3.0};
            double[] x = solver.solve(b);

            assertArrayEquals(new double[]{1.0 / 1.0, (1.0 / 1.0 - 0.4494) / 0.4495, (1.0 / 1.0 - 0.2667) / 0.4441}, x, 0.0001);

            RealVector bVector = new ArrayRealVector(b);
            RealVector xVector = solver.solve(bVector);

            assertArrayEquals(x, xVector.toArray(), 0.0001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}