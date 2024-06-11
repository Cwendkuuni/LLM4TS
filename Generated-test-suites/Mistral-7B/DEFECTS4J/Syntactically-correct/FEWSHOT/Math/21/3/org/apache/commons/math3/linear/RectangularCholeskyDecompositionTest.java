package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0, 1.0, 0.0},
                                                                    {1.0, 2.0, 1.0, 2.0},
                                                                    {1.0, 1.0, 3.0, 0.0},
                                                                    {0.0, 2.0, 0.0, 4.0}});

        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = cholesky.getRootMatrix();
            int rank = cholesky.getRank();

            assertEquals(3, rank);
            assertEquals(4, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());

            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{FastMath.SQRT(0.0), 1.0, 0.0, 0.0},
                                                                            {-0.5, FastMath.SQRT(2.0), -0.5, 0.0},
                                                                            {0.0, 0.0, FastMath.SQRT(3.0), 1.0},
                                                                            {0.0, 1.0, 0.0, FastMath.SQRT(4.0)}});

            assertEquals(expectedRoot, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this exception should not be thrown in this test
            assertNull(e);
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0, 1.0, 0.0},
                                                                    {1.0, -1.0, 1.0, 2.0},
                                                                    {1.0, 1.0, 3.0, 0.0},
                                                                    {0.0, 2.0, 0.0, -4.0}});

        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_2;

        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = cholesky.getRootMatrix();
            int rank = cholesky.getRank();

            assertEquals(0, rank);
            assertEquals(2, root.getRowDimension());
            assertEquals(0, root.getColumnDimension());

        } catch (NonPositiveDefiniteMatrixException e) {
            // this exception should not be thrown in this test
            assertNull(e);
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = RealMatrixFactory.IDENTITY_3;

        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = cholesky.getRootMatrix();
            int rank = cholesky.getRank();

            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());

            RealMatrix expectedRoot = matrix;

            assertEquals(expectedRoot, root);

        } catch (NonPositiveDefiniteMatrixException e) {
            // this exception should not be thrown in this test
            assertNull(e);
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 0.0}});

        try {
            RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = cholesky.getRootMatrix();
            int rank = cholesky.getRank();

            assertEquals(1, rank);
            assertEquals(2, root.getRowDimension());
            assertEquals(1, root.getColumnDimension());

            RealVector expectedRootColumn = new ArrayRealVector(new double[]{1.0});;

            RealVector actualRootColumn = root.getColumnVector(0);

            assertEquals(expectedRootColumn, actualRootColumn);

        } catch (NonPositiveDefiniteMatrixException e) {
            // this exception should not be thrown in this test
            assertNull(e);
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingularMatrixNegativeSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 0.0}});

        new RectangularCholeskyDecomposition(matrix, -0.01);
    }

    @Test
    public void testSingularMatrixZeroSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 0.0}});

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // this exception should be thrown in this test
            assertEquals(1.0, e.getDiagonalElement(), 1e-10);
            assertEquals(0, e.getRowNumber());
        }
    }
}