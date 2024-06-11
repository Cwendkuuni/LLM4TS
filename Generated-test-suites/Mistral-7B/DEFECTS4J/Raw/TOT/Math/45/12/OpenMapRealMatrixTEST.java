package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.linear.OpenMapRealMatrix;

/**
 * Unit tests for {@link OpenMapRealMatrix} class.
 */
public class OpenMapRealMatrixTest {
    /**
     * Test construction of an identity matrix.
     */
    @Test
    public void testIdentityMatrix() {
        final int size = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(size, size);
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(size);
        assertEquals(expected, matrix);
    }

    /**
     * Test addition of matrices with the same size.
     */
    @Test
    public void testAddition() {
        final int size = 3;
        OpenMapRealMatrix matA = new OpenMapRealMatrix(size, size);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(size, size);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {3.0, 3.0, 3.0},
                {3.0, 3.0, 3.0},
                {3.0, 3.0, 3.0}});

        matA.setEntry(0, 0, 1.0);
        matA.setEntry(0, 1, 2.0);
        matA.setEntry(0, 2, 3.0);
        matA.setEntry(1, 0, 4.0);
        matA.setEntry(1, 1, 5.0);
        matA.setEntry(1, 2, 6.0);
        matA.setEntry(2, 0, 7.0);
        matA.setEntry(2, 1, 8.0);
        matA.setEntry(2, 2, 9.0);

        matB.setEntry(0, 0, 1.0);
        matB.setEntry(0, 1, 2.0);
        matB.setEntry(0, 2, 3.0);
        matB.setEntry(1, 0, 4.0);
        matB.setEntry(1, 1, 5.0);
        matB.setEntry(1, 2, 6.0);
        matB.setEntry(2, 0, 7.0);
        matB.setEntry(2, 1, 8.0);
        matB.setEntry(2, 2, 9.0);

        OpenMapRealMatrix result = matA.add(matB);
        assertEquals(expected, result);
    }

    /**
     * Test addition of matrices with different sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDifferentSizes() {
        final int sizeA = 3;
        OpenMapRealMatrix matA = new OpenMapRealMatrix(sizeA, sizeA);
        final int sizeB = 2;
        OpenMapRealMatrix matB = new OpenMapRealMatrix(sizeB, sizeB);

        matA.setEntry(0, 0, 1.0);
        matA.setEntry(0, 1, 2.0);
        matA.setEntry(0, 2, 3.0);
        matA.setEntry(1, 0, 4.0);
        matA.setEntry(1, 1, 5.0);
        matA.setEntry(1, 2, 6.0);
        matA.setEntry(2, 0, 7.0);
        matA.setEntry(2, 1, 8.0);
        matA.setEntry(2, 2, 9.0);

        matB.setEntry(0, 0, 1.0);
        matB.setEntry(0, 1, 2.0);

        OpenMapRealMatrix result = matA.add(matB);
    }

    /**
     * Test subtraction of matrices with the same size.
     */
    @Test
    public void testSubtraction() {
        final int size = 3;
        OpenMapRealMatrix matA = new OpenMapRealMatrix(size, size);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(size, size);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {-2.0, -2.0, -2.0},
                {-2.0, -2.0, -2.0},
                {-2.0, -2.0, -2.0}});

        matA.setEntry(0, 0, 1.0);
        matA.setEntry(0, 1, 2.0);
        matA.setEntry(0, 2, 3.0);
        matA.setEntry(1, 0, 4.0);
        matA.setEntry(1, 1, 5.0);
        matA.setEntry(1, 2, 6.0);
        matA.setEntry(2, 0, 7.0);
        matA.setEntry(2, 1, 8.0);
        matA.setEntry(2, 2, 9.0);

        matB.setEntry(0, 0, -1.0);
        matB.setEntry(0, 1, -2.0);
        matB.setEntry(0, 2, -3.0);
        matB.setEntry(1, 0, -4.0);
        matB.setEntry(1, 1, -5.0);
        matB.setEntry(1, 2, -6.0);
        matB.setEntry(2, 0, -7.0);
        matB.setEntry(2, 1, -8.0);
        matB.setEntry(2, 2, -9.0);

        OpenMapRealMatrix result = matA.subtract(matB);
        assertEquals(expected, result);
    }

    /**
     * Test subtraction of matrices with different sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionDifferentSizes() {
        final int sizeA = 3;
        OpenMapRealMatrix matA = new OpenMapRealMatrix(sizeA, sizeA);
        final int sizeB = 2;
        OpenMapRealMatrix matB = new OpenMapRealMatrix(sizeB, sizeB);

        matA.setEntry(0, 0, 1.0);
        matA.setEntry(0, 1, 2.0);
        matA.setEntry(0, 2, 3.0);
        matA.setEntry(1, 0, 4.0);
        matA.setEntry(1, 1, 5.0);
        matA.setEntry(1, 2, 6.0);
        matA.setEntry(2, 0, 7.0);
        matA.setEntry(2, 1, 8.0);
        matA.setEntry(2, 2, 9.0);

        matB.setEntry(0, 0, 1.0);
        matB.setEntry(0, 1, 2.0);

        OpenMapRealMatrix result = matA.subtract(matB);
    }

    /**
     * Test multiplication of a matrix by a scalar.
     */
    @Test
    public void testScalarMultiplication() {
        final int size = 3;
        OpenMapRealMatrix mat = new OpenMapRealMatrix(size, size);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {2.0, 2.0, 2.0},
                {2.0, 2.0, 2.0},
                {2.0, 2.0, 2.0}});

        mat.setEntry(0, 0, 1.0);
        mat.setEntry(0, 1, 2.0);
        mat.setEntry(0, 2, 3.0);
        mat.setEntry(1, 0, 4.0);
        mat.setEntry(1, 1, 5.0);
        mat.setEntry(1, 2, 6.0);
        mat.setEntry(2, 0, 7.0);
        mat.setEntry(2, 1, 8.0);
        mat.setEntry(2, 2, 9.0);

        OpenMapRealMatrix result = mat.scalarMultiply(2.0);
        assertEquals(expected, result);
    }

    /**
     * Test multiplication of matrices.
     */
    @Test
    public void testMultiplication() {
        final int sizeA = 3;
        final int sizeB = 2;
        OpenMapRealMatrix matA = new OpenMapRealMatrix(sizeA, sizeA);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(sizeB, sizeA);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {15.0, 11.0},
                {23.0, 17.0}});

        matA.setEntry(0, 0, 1.0);
        matA.setEntry(0, 1, 2.0);
        matA.setEntry(0, 2, 3.0);
        matA.setEntry(1, 0, 4.0);
        matA.setEntry(1, 1, 5.0);
        matA.setEntry(1, 2, 6.0);
        matA.setEntry(2, 0, 7.0);
        matA.setEntry(2, 1, 8.0);
        matA.setEntry(2, 2, 9.0);

        matB.setEntry(0, 0, 1.0);
        matB.setEntry(0, 1, 2.0);
        matB.setEntry(1, 0, 3.0);
        matB.setEntry(1, 1, 4.0);

        OpenMapRealMatrix result = matA.multiply(matB);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test multiplication of matrices with different sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationDifferentSizes() {
        final int sizeA = 3;
        final int sizeB = 2;
        OpenMapRealMatrix matA = new OpenMapRealMatrix(sizeA, sizeA);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(sizeB, sizeB);

        matA.setEntry(0, 0, 1.0);
        matA.setEntry(0, 1, 2.0);
        matA.setEntry(0, 2, 3.0);
        matA.setEntry(1, 0, 4.0);
        matA.setEntry(1, 1, 5.0);
        matA.setEntry(1, 2, 6.0);
        matA.setEntry(2, 0, 7.0);
        matA.setEntry(2, 1, 8.0);
        matA.setEntry(2, 2, 9.0);

        matB.setEntry(0, 0, 1.0);
        matB.setEntry(0, 1, 2.0);

        OpenMapRealMatrix result = matA.multiply(matB);
    }

    /**
     * Test setting an entry to zero.
     */
    @Test
    public void testClearEntry() {
        final int size = 3;
        OpenMapRealMatrix mat = new OpenMapRealMatrix(size, size);
        mat.setEntry(0, 0, 1.0);
        mat.clearEntry(0, 0);
        assertEquals(0.0, mat.getEntry(0, 0), 0.0);
    }

    /**
     * Test setting an entry to a value.
     */
    @Test
    public void testSetEntry() {
        final int size = 3;
        OpenMapRealMatrix mat = new OpenMapRealMatrix(size, size);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(0, 0, 2.0);
        assertEquals(2.0, mat.getEntry(0, 0), 0.0);
    }

    /**
     * Test setting an entry to a negative value.
     */
    @Test
    public void testSetNegativeEntry() {
        final int size = 3;
        OpenMapRealMatrix mat = new OpenMapRealMatrix(size, size);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(0, 0, -2.0);
        assertEquals(-2.0, mat.getEntry(0, 0), 0.0);
    }

    /**
     * Test setting an entry to a very large value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetLargeEntry() {
        final int size = 3;
        OpenMapRealMatrix mat = new OpenMapRealMatrix(size, size);
        mat.setEntry(0, 0, Double.MAX_VALUE);
    }
}