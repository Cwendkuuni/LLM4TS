package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3},
                {2.2, 4.4, 5.5},
                {3.3, 5.5, 6.6}
        });
        try {
            RectangularCholeskyDecomposition decompositions = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decompositions.getRootMatrix();
            int rank = decompositions.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertNotNull(root);
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    assertEquals(Precision.isClose(matrix.getEntry(i, j), root.getEntry(i, j) * root.getEntry(i, j), small), true);
                    assertEquals(Precision.isClose(matrix.getEntry(j, i), root.getEntry(j, i) * root.getEntry(j, i), small), true);
                }
            }
            for (int i = rank; i < 3; ++i) {
                for (int j = rank; j < 3; ++j) {
                    assertNull(root.getEntry(i, j));
                    assertNull(root.getEntry(j, i));
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix should be positive semidefinite");
        }
    }

    @Test
    public void testDecompositionSmallMatrix() {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3},
                {2.2, 1.1, 1.1},
                {3.3, 1.1, 1.1}
        });
        try {
            RectangularCholeskyDecomposition decompositions = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decompositions.getRootMatrix();
            int rank = decompositions.getRank();
            assertEquals(2, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertNotNull(root);
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    assertEquals(Precision.isClose(matrix.getEntry(i, j), root.getEntry(i, j) * root.getEntry(i, j), small), true);
                    assertEquals(Precision.isClose(matrix.getEntry(j, i), root.getEntry(j, i) * root.getEntry(j, i), small), true);
                }
            }
            for (int i = rank; i < 3; ++i) {
                for (int j = rank; j < 3; ++j) {
                    assertNull(root.getEntry(i, j));
                    assertNull(root.getEntry(j, i));
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix should be positive semidefinite");
        }
    }

    @Test
    public void testDecompositionNegativeMatrix() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {-1.1, 2.2, 3.3},
                {2.2, -4.4, 5.5},
                {3.3, 5.5, -6.6}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Matrix should not be positive semidefinite");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDecompositionZeroMatrix() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0}, {0}});
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Matrix should not be positive semidefinite");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testRandomDecomposition() {
        double small = 1e-12;
        int n = 10;
        int p = 5;
        RealMatrix matrix = new Array2DRowRealMatrix(n, p);
        Random random = new Random();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < p; ++j) {
                matrix.setEntry(i, j, random.nextGaussian());
            }
        }
        try {
            RectangularCholeskyDecomposition decompositions = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decompositions.getRootMatrix();
            int rank = decompositions.getRank();
            assertEquals(p, rank);
            assertEquals(n, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertNotNull(root);
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    assertEquals(Precision.isClose(matrix.getEntry(i, j), root.getEntry(i, j) * root.getEntry(i, j), small), true);
                }
            }
            for (int i = rank; i < n; ++i) {
                for (int j = rank; j < p; ++j) {
                    assertNull(root.getEntry(i, j));
                    assertNull(root.getEntry(j, i));
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix should be positive semidefinite");
        }
    }
}