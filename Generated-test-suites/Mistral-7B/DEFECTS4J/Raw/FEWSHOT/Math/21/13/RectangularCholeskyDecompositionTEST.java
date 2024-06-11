package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    private static final double EPSILON = 1e-14;
    private static final double SMALL = 1e-10;

    @Test
    public void testDecomposition() {
        final int dimension = 5;
        final double[][] matrixData = new double[][]{
                {2.0, 1.0, 1.0, 0.1, 0.1},
                {1.0, 2.0, 1.0, 0.1, 0.1},
                {1.0, 1.0, 2.0, 0.1, 0.1},
                {0.1, 0.1, 0.1, 1.0, 1.0},
                {0.1, 0.1, 0.1, 1.0, 1.0}
        };

        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, SMALL);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertNotNull(root);
        assertEquals(5, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        System.out.println("Matrix: ");
        System.out.println(matrix);
        System.out.println("Root: ");
        System.out.println(root);

        for (int i = 0; i < rank; ++i) {
            for (int j = 0; j < rank; ++j) {
                assertEquals("Invalid root value for index [" + i + "][" + j + "]", 0.0, Precision.isClose(matrix.getEntry(i, i), root.getEntry(i, j) * root.getEntry(i, j), EPSILON));
            }
        }

        for (int i = rank; i < dimension; ++i) {
            for (int j = 0; j < rank; ++j) {
                assertNull("Invalid root value for index [" + i + "][" + j + "]", root.getEntry(i, j));
            }
        }

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                double sum = 0.0;
                for (int k = 0; k < rank; ++k) {
                    sum += root.getEntry(i, k) * root.getEntry(j, k);
                }
                assertEquals("Invalid product of root and root transpose for index [" + i + "][" + j + "]",
                        matrix.getEntry(i, j), sum, EPSILON);
            }
        }
    }

    @Test
    public void testDecompositionWithSingularMatrix() {
        final int dimension = 2;
        final double[][] matrixData = new double[][]{
                {1.0, 0.0},
                {0.0, 0.0}
        };

        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        try {
            new RectangularCholeskyDecomposition(matrix, SMALL);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDecompositionWithNegativeDiagonalElement() {
        final int dimension = 2;
        final double[][] matrixData = new double[][]{
                {1.0, -1.1},
                {-1.1, 1.0}
        };

        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        try {
            new RectangularCholeskyDecomposition(matrix, SMALL);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testPermutedDecomposition() {
        final int dimension = 4;
        final double[][] matrixData = new double[][]{
                {1.0, 0.5, 0.5, 0.5},
                {0.5, 1.0, 0.5, 0.5},
                {0.5, 0.5, 1.0, 0.5},
                {0.5, 0.5, 0.5, 1.0}
        };

        RealMatrix matrix = RealMatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, SMALL);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertNotNull(root);
        assertEquals(4, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        System.out.println("Matrix: ");
        System.out.println(matrix);
        System.out.println("Root: ");
        System.out.println(root);

        Random random = new Random();
        RealMatrix testMatrix = new Array2DRowRealMatrix(new double[][]{
                {random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian()},
                {random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian()},
                {random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian()},
                {random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), random.nextGaussian()}
        });

        RealMatrix result = root.multiply(root.transpose());
        RealMatrix actual = testMatrix.multiply(result);

        assertArrayEquals("Invalid result of the product of root and root transpose", actual.getData(), testMatrix.getData(), EPSILON);
    }
}