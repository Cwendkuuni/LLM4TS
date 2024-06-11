package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.SpecialFunctions;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0},
                                                                     {1.0, 2.0, 1.0},
                                                                     {0.0, 1.0, 2.0}});

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = rcd.getRootMatrix();

            assertEquals("Incorrect rank", 3, rcd.getRank());
            assertEquals("Root matrix dimensions", 3, root.getRowDimension());
            assertEquals("Root matrix columns", rcd.getRank(), root.getColumnDimension());

            // Check decomposition
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 / Math.sqrt(2.0)},
                                                                        {1.0, 1.0 / Math.sqrt(2.0)},
                                                                        {0.0, 0.0}});
            assertMatrixEquals("Unexpected decomposition", expected, root);

            // Check decomposition with small diagonal elements
            matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0},
                                                           {1.0, 1.0e-10, 1.0},
                                                           {0.0, 1.0, 2.0}});
            rcd = new RectangularCholeskyDecomposition(matrix, small);
            root = rcd.getRootMatrix();

            assertEquals("Incorrect rank", 3, rcd.getRank());
            assertEquals("Root matrix dimensions", 3, root.getRowDimension());
            assertEquals("Root matrix columns", rcd.getRank(), root.getColumnDimension());

            // Check decomposition
            expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 / Math.sqrt(2.0), 0.0},
                                                            {1.0, 1.0 / Math.sqrt(2.0) + 1e-8, 1.0 / Math.sqrt(2.0)},
                                                            {0.0, 0.0, 1.0}});
            assertMatrixEquals("Unexpected decomposition", expected, root);

            // Test exception when matrix is not positive semidefinite
            matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0},
                                                           {1.0, 1.0}});
            try {
                new RectangularCholeskyDecomposition(matrix, small);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException npdme) {
                // Expected
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    private void assertMatrixEquals(String msg, RealMatrix expected, RealMatrix actual) {
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                assertEquals(msg + " Matrix[" + i + "][" + j + "]",
                             Precision.round(expected.getEntry(i, j), 15),
                             Precision.round(actual.getEntry(i, j), 15));
            }
        }
    }

    @org.junit.Test
    public void testRandomRectangularPositiveSemidefiniteMatrix() {
        final int size = 10;
        final double small = 1e-6;
        final int iterations = 100;

        Random random = new Random();
        for (int i = 0; i < iterations; ++i) {
            RealMatrix matrix = MatrixUtils.createRealSymmetric(size);
            double determinant = 1.0;
            for (int j = 0; j < size; ++j) {
                double value = SpecialFunctions.gamma(j + 1.0);
                determinant *= value * value;
                for (int k = 0; k < j; ++k) {
                    double rand = random.nextGaussian();
                    matrix.setEntry(j, k, rand);
                    matrix.setEntry(k, j, rand);
                }
                matrix.setEntry(j, j, determinant);
            }

            try {
                RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
                RealMatrix root = rcd.getRootMatrix();

                assertEquals("Incorrect rank", size, rcd.getRank());
                assertEquals("Root matrix dimensions", size, root.getRowDimension());
                assertEquals("Root matrix columns", rcd.getRank(), root.getColumnDimension());

                // Check decomposition
                RealMatrix expected = rcd.getRootMatrix().transpose().multiply(rcd.getRootMatrix());
                assertMatrixEquals("Unexpected decomposition", expected, matrix);
            } catch (NonPositiveDefiniteMatrixException e) {
                fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
            }
        }
    }
}