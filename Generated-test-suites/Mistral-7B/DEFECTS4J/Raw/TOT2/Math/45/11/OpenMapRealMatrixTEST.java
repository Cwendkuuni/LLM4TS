package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SparseRealMatrix;

/**
 * Tests for {@link OpenMapRealMatrix} class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor with valid dimensions.
     */
    @Test
    public void testConstructorValidDimensions() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(4, matrix.getColumnDimension());
    }

    /**
     * Test constructor with invalid dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidDimensions() {
        new OpenMapRealMatrix(-1, 4);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    /**
     * Test copy method.
     */
    @Test
    public void testCopyMethod() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 4);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    /**
     * Test creation of identity matrix.
     */
    @Test
    public void testCreateIdentityMatrix() {
        OpenMapRealMatrix identity = OpenMapRealMatrix.IDENTITY_3;
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                         {0.0, 1.0, 0.0},
                                                                         {0.0, 0.0, 1.0}});
        assertEquals(expected, identity);
    }

    /**
     * Test adding two matrices with the same size.
     */
    @Test
    public void testAddSameSizeMatrices() {
        OpenMapRealMatrix matA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matA.add(matB);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{3.0, 3.0, 3.0},
                                                                       {3.0, 3.0, 3.0},
                                                                       {3.0, 3.0, 3.0}});
        assertEquals(expected, result);
    }

    /**
     * Test adding two matrices with different sizes.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddDifferentSizeMatrices() {
        OpenMapRealMatrix matA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(3, 2);
        matA.add(matB);
    }

    /**
     * Test subtracting two matrices with the same size.
     */
    @Test
    public void testSubtractSameSizeMatrices() {
        OpenMapRealMatrix matA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matA.subtract(matB);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0, 0.0},
                                                                       {0.0, 0.0, 0.0},
                                                                       {0.0, 0.0, 0.0}});
        assertEquals(expected, result);
    }

    /**
     * Test subtracting two matrices with different sizes.
     */
    @Test
    public void testSubtractDifferentSizeMatrices() {
        OpenMapRealMatrix matA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix result = matA.subtract(matB);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0},
                                                                      {-1.0, -1.0},
                                                                      {-1.0, -1.0}});
        assertEquals(expected, result);
    }

    /**
     * Test multiplying two matrices.
     */
    @Test
    public void testMultiplyMatrices() {
        OpenMapRealMatrix matA = new OpenMapRealMatrix(3, 3);
        matA.setEntry(1, 1, 2.0);
        matA.setEntry(1, 2, 3.0);
        matA.setEntry(2, 1, 4.0);
        matA.setEntry(2, 2, 5.0);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(3, 3);
        matB.setEntry(1, 1, 1.0);
        matB.setEntry(1, 2, 2.0);
        matB.setEntry(2, 1, 3.0);
        matB.setEntry(2, 2, 4.0);
        OpenMapRealMatrix result = matA.multiply(matB);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{11.0, 13.0},
                                                                       {15.0, 17.0},
                                                                       {19.0, 21.0}});
        assertEquals(expected, result);
    }

    /**
     * Test attempting to multiply matrices with different numbers of columns.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDifferentColumnCounts() {
        OpenMapRealMatrix matA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matB = new OpenMapRealMatrix(3, 2);
        matA.multiply(matB);
    }

    /**
     * Test getting entry from matrix.
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(2, 2);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        assertEquals(1.0, mat.getEntry(0, 0), 0.001);
        assertEquals(2.0, mat.getEntry(1, 1), 0.001);
    }

    /**
     * Test setting entry in matrix.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(2, 2);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(0, 0, 3.0);
        assertEquals(3.0, mat.getEntry(0, 0), 0.001);

        try {
            mat.setEntry(3, 3, 4.0);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Test adding to entry in matrix.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(2, 2);
        mat.setEntry(0, 0, 1.0);
        mat.addToEntry(0, 0, 2.0);
        assertEquals(3.0, mat.getEntry(0, 0), 0.001);
    }

    /**
     * Test multiplying entry in matrix.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(2, 2);
        mat.setEntry(0, 0, 1.0);
        mat.multiplyEntry(0, 0, 3.0);
        assertEquals(3.0, mat.getEntry(0, 0), 0.001);

        try {
            mat.multiplyEntry(3, 3, 4.0);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Test invalid add operation with NumberIsTooLargeException.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddInvalidEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(2, 2);
        mat.setEntry(0, 0, Long.MAX_VALUE);
        mat.addToEntry(0, 0, 1);
    }
}