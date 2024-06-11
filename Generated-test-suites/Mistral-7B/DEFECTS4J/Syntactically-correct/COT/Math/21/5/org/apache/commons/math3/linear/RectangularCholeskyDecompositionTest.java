package org.apache.commons.math3.linear.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeSmall() {
        new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(2, 2), -1.0);
    }

    @Test
    public void testConstructorIdentityMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(3, 3).transpose();
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructorSymmetricPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 2.0, 3.0},
            {2.0, 3.0, 4.0}
        });
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 1e-14);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.543080634815243, 1.12112322982331},
            {1.0, 1.12112322982331, 1.954282669222345}
        });
        assertEquals(expectedRoot, decompositon.getRootMatrix());
        assertEquals(3, decompositon.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 3.0},
            {2.0, 3.0, 4.0}
        });
        new RectangularCholeskyDecomposition(matrix, 1e-14);
    }

    @Test
    public void testConstructorWithSwappedColumns() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {2.0, 1.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(2, 2);
        expectedRoot.setEntry(0, 0, 1.0);
        expectedRoot.setEntry(0, 1, -1.0);
        expectedRoot.setEntry(1, 0, -1.0);
        expectedRoot.setEntry(1, 1, 1.0);
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testConstructorZeroMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {});
        new RectangularCholeskyDecomposition(matrix, 0.0);
        assertNull(MatrixUtils.createRealMatrix(1, 1));
        assertEquals(0, 0);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorSingularMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 0.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 0.0);
        matrix.setEntry(0, 2, 0.0);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 0.0);
        matrix.setEntry(2, 2, 1.0);
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 1e-14);
        assertEquals(2, decompositon.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 0.0);
        matrix.setEntry(0, 2, 0.0);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 0.0);
        matrix.setEntry(2, 2, 1.0);

        double small = 1e-14;
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(3, 2);
        expectedRoot.setEntry(0, 0, 1.0);
        expectedRoot.setEntry(0, 1, 0.0);
        expectedRoot.setEntry(1, 0, 0.0);
        expectedRoot.setEntry(1, 1, 1.0);
        expectedRoot.setEntry(2, 0, 0.0);
        expectedRoot.setEntry(2, 1, 1.0/Math.sqrt(2.0));

        assertArrayEquals(expectedRoot.getData(), decompositon.getRootMatrix().getData(), 1e-14);
    }

    @Test
    public void testGetRootMatrixSingularCase() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 0.0}});
        double small = 1e-14;
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
        assertNull(decompositon.getRootMatrix());
        assertEquals(0, decompositon.getRank());
    }

    @Test
    public void testGetRootMatrixIdentityCase() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        double small = 1e-14;
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(3, 3).transpose();
        assertArrayEquals(expectedRoot.getData(), decompositon.getRootMatrix().getData(), 1e-14);
        assertEquals(3, decompositon.getRank());
    }

    @Test
    public void testGetRootMatrixRank2() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0},
            {2.0, 3.0},
            {3.0, 4.0}
        });
        double small = 1e-14;
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1.0, 0.0},
            {1.0, -0.433012701892219, 0.888888888888889},
            {0.0, 0.888888888888889, 1.414213562373095}
        });
        assertArrayEquals(expectedRoot.getData(), decompositon.getRootMatrix().getData(), 1e-14);
        assertEquals(2, decompositon.getRank());
    }

    @Test
    public void testGetRootMatrixRank1() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0}, {2.0}});
        double small = 1e-14;
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
        assertArrayEquals(new double[][] {{1.0}}, decompositon.getRootMatrix().getData());
        assertEquals(1, decompositon.getRank());
    }

    @Test
    public void testGetRootMatrixIdentityWithSwappedColumns() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}});
        double small = 1e-14;
        RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(2, 2).transpose();
        assertArrayEquals(expectedRoot.getData(), decompositon.getRootMatrix().getData(), 1e-14);
        assertEquals(2, decompositon.getRank());
    }

}