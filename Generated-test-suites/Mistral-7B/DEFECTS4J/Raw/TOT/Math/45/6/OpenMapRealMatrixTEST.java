package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SparseVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

import org.junit.Test;

/**
 * Tests for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Tests the basic functionality of {@link OpenMapRealMatrix}.
     */
    @Test
    public void testBasicFunctionality() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        // Tests setting and getting entries
        matrix1.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix1.getEntry(1, 1), 1e-14);
        assertEquals(1.0, matrix1.getEntry(1, 1), 1e-14);

        // Tests adding matrices
        matrix1.setEntry(1, 1, 3.0);
        matrix1.setEntry(1, 2, 2.0);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(1, 2, 1.0);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(3.0, sum.getEntry(1, 1), 1e-14);
        assertEquals(3.0, sum.getEntry(1, 1), 1e-14);
        assertEquals(3.0, sum.getEntry(1, 2), 1e-14);
        assertEquals(2.0, sum.getEntry(0, 0), 1e-14);

        // Tests subtracting matrices
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(0.0, difference.getEntry(1, 1), 1e-14);
        assertEquals(1.0, difference.getEntry(1, 2), 1e-14);
        assertEquals(-1.0, difference.getEntry(1, 1), 1e-14);

        // Tests multiplying matrices
        RealMatrix product = matrix1.multiply(matrix2);
        assertEquals(3.0 * 2.0 + 2.0 * 3.0, product.getEntry(0, 0), 1e-14);
        assertEquals(3.0 * 1.0 + 2.0 * 2.0, product.getEntry(0, 1), 1e-14);
        assertEquals(3.0 * 1.0 + 2.0 * 3.0, product.getEntry(1, 0), 1e-14);
        assertEquals(2.0 * 3.0 + 1.0 * 2.0, product.getEntry(1, 1), 1e-14);

        // Tests the copy constructor
        OpenMapRealMatrix copied = new OpenMapRealMatrix(matrix1);
        assertEquals(3.0, copied.getEntry(1, 1), 1e-14);
        assertEquals(3.0, copied.getEntry(1, 1), 1e-14);

        // Tests the creation of a matrix with the same size
        OpenMapRealMatrix identical = new OpenMapRealMatrix(3, 3);
        assertEquals(0.0, identical.getEntry(0, 0), 1e-14);
        assertEquals(0.0, identical.getEntry(0, 0), 1e-14);

        // Tests the creation of a matrix with different sizes
        assertThrows(DimensionMismatchException.class, () -> matrix1.multiply(new Array2DRowRealMatrix(1, 3)));
        assertThrows(DimensionMismatchException.class, () -> new OpenMapRealMatrix(3, 2).multiply(matrix1));
    }

    /**
     * Tests the handling of zero entries.
     */
    @Test
    public void testHandlingOfZeroEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 0.0);
        matrix.setEntry(1, 2, 0.0);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        RealMatrix product = matrix.multiply(identity);
        assertEquals(0.0, product.getEntry(0, 0), 1e-14);
        assertEquals(0.0, product.getEntry(0, 1), 1e-14);
        assertEquals(0.0, product.getEntry(1, 0), 1e-14);
        assertEquals(0.0, product.getEntry(1, 1), 1e-14);
        assertEquals(0.0, product.getEntry(1, 2), 1e-14);
    }

    /**
     * Tests the handling of large indices.
     */
    @Test
    public void testHandlingOfLargeIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(100, 100);
        assertFalse(matrix.entries.containsKey(Integer.MAX_VALUE));
        assertThrows(NumberIsTooLargeException.class, () -> matrix.setEntry(Integer.MAX_VALUE, Integer.MAX_VALUE, 1.0));
    }

    /**
     * Tests the handling of negative indices.
     */
    @Test
    public void testHandlingOfNegativeIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(-1, 0, 1.0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(0, -1, 1.0));
    }

    /**
     * Tests the handling of out-of-bounds row indices.
     */
    @Test
    public void testHandlingOfOutOfBoundsRowIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(3, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(3, 0, 1.0));
    }

    /**
     * Tests the handling of out-of-bounds column indices.
     */
    @Test
    public void testHandlingOfOutOfBoundsColumnIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getEntry(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setEntry(0, 3, 1.0));
    }

    /**
     * Tests the handling of overlapping column indices.
     */
    @Test
    public void testHandlingOfOverlappingColumnIndices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        realMultiplyAndCheckDimensionMismatchException(matrix1, matrix2);
    }

    /**
     * Tests the handling of overlapping row indices.
     */
    @Test
    public void testHandlingOfOverlappingRowIndices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        realMultiplyAndCheckDimensionMismatchException(matrix1, matrix2);
    }

    /**
     * Helper method for testing the handling of overlapping dimensions.
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     */
    private void realMultiplyAndCheckDimensionMismatchException(RealMatrix matrix1, RealMatrix matrix2) {
        assertThrows(DimensionMismatchException.class, () -> matrix1.multiply(matrix2));
    }
}