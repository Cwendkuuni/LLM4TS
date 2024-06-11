package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 1.0},
                {1.0, 1.0, 1.0}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {FastMath.sqrt(3.0), 0.0, 0.0},
                {0.5 * FastMath.sqrt(21.0), FastMath.sqrt(5.0), 0.0},
                {-0.5 * FastMath.sqrt(3.0 / 2.0), 0.5 * FastMath.sqrt(3.0 / 2.0), FastMath.sqrt(1.0 + 1.0 / 3.0)}
        });

        assertArrayEquals(expectedValues(expectedRoot.getData()), root.getData(), 1e-12);
    }

    @Test
    public void testNegativeSmall() {
        double small = -1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 1.0},
                {1.0, 1.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Exception should have been thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testZeroDiagonal() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 0.0, 1.0},
                {1.0, 1.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Exception should have been thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testIdentity() {
        double small = 1e-12;
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(5, rank);
        assertEquals(5, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        RealMatrix expectedRoot = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0, 1.0, 1.0, 1.0});
        assertArrayEquals(expectedValues(expectedRoot.getData()), root.getData(), 1e-12);
    }

    @Test
    public void testRandom() {
        double small = 1e-5;
        int size = 10;
        RealMatrix matrix = new Array2DRowRealMatrix(size, size);
        Random random = new Random(12345);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                double value = random.nextGaussian();
                if (i == j) {
                    matrix.setEntry(i, j, value * value);
                    continue;
                }
                matrix.setEntry(i, j, value);
            }
        }

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(size, rank);
            assertEquals(size, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            // This can happen if the matrix generated is not positive semidefinite
        } finally {
            if (decomposition != null) {
                decomposition.getRootMatrix();
            }
        }
    }

    private double[] expectedValues(double[][] values) {
        double[] result = new double[values.length * values[0].length];
        int index = 0;
        for (double[] value : values) {
            System.arraycopy(value, 0, result, index, value.length);
            index += value.length;
        }
        return result;
    }
}