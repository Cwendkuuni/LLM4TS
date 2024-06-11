package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = new double[][]{
            {2.0, 1.0, 0.0},
            {1.0, 2.0, 1.0},
            {0.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        double small = 1e-12;
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {FastMath.sqrt(2.0), 0.0, 0.0},
                {0.5, FastMath.sqrt(2.0), 0.0},
                {0.0, 1.0, FastMath.sqrt(2.0)}
            });

        RealMatrix root = decomp.getRootMatrix();

        assertNotNull(root);
        assertEquals(expectedRoot.getRowDimension(), root.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), root.getColumnDimension());
        assertArrayEquals(expectedValues(expectedRoot.getData()), expectedValues(root.getData()));

        assertEquals(3, decomp.getRank());
    }

    @Test
    public void testNonPositiveDefiniteException() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = new double[][]{
            {2.0, 1.0, 0.0},
            {1.0, -2.0, 1.0},
            {0.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        double small = 1e-12;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-2.0, e.getDiagonalValue(), 0.0);
            assertEquals(1, e.getRowIndex());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixExceptionWithSmallRank() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = new double[][]{
            {2.0, 1.0, 0.0, 0.1},
            {1.0, 2.0, 1.0, E.EPSILON},
            {0.0, 1.0, 2.0, 0.2},
            {0.1, E.EPSILON, 0.2, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        double small = 1e-6;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(2.0, e.getDiagonalValue(), 0.0);
            assertEquals(2, e.getRowIndex());
        }
    }

    @Test
    public void testZeroMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = new double[][]{
            {},
            {}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        double small = 0.0;

        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[0][0]);
        RealMatrix root = decomp.getRootMatrix();

        assertNotNull(root);
        assertEquals(0, root.getRowDimension());
        assertEquals(0, root.getColumnDimension());
        assertEquals(expectedValues(expectedRoot.getData()), expectedValues(root.getData()));

        assertEquals(0, decomp.getRank());
    }

    @Test
    public void testIdentityMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        double small = 1e-12;

        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
            });

        RealMatrix root = decomp.getRootMatrix();

        assertNotNull(root);
        assertEquals(expectedRoot.getRowDimension(), root.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), root.getColumnDimension());
        assertArrayEquals(expectedValues(expectedRoot.getData()), expectedValues(root.getData()));

        assertEquals(2, decomp.getRank());
    }

    @Test
    public void testSingularValueMatrix() {
        double[][] data = new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data);

        double small = 0.0;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    private double[] expectedValues(double[][] expectedValues) {
        double[] result = new double[expectedValues.length * expectedValues[0].length];
        System.arraycopy(expectedValues, 0, result, 0, result.length);
        return result;
    }
}