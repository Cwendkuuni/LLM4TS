package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        RealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixCreationWithNegativeSize() {
        new OpenMapRealMatrix(-2, 3);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        RealMatrix original = new OpenMapRealMatrix(3, 3);
        OriginalMatrix copied = original.copy();
        assertEquals(original, copied);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        RealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix1.add(matrix2);
        assertEquals(4.0, matrix1.getEntry(0, 0), 1e-12);
        assertEquals(6.0, matrix1.getEntry(1, 1), 1e-12);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOpenMapRealMatrixAdditionWithIncompatibleSizes() {
        RealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        RealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        RealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(-2.0, difference.getEntry(0, 0), 1e-12);
        assertEquals(-2.0, difference.getEntry(1, 1), 1e-12);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOpenMapRealMatrixSubtractionWithIncompatibleSizes() {
        RealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        RealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);
        matrix2.setEntry(2, 0, 11.0);
        matrix2.setEntry(2, 1, 12.0);
        matrix2.setEntry(2, 2, 13.0);

        RealMatrix product = matrix1.multiply(matrix2);
        assertEquals(15.0, product.getEntry(0, 0), 1e-12);
        assertEquals(22.0, product.getEntry(0, 1), 1e-12);
        assertEquals(29.0, product.getEntry(0, 2), 1e-12);
        assertEquals(36.0, product.getEntry(1, 0), 1e-12);
        assertEquals(45.0, product.getEntry(1, 1), 1e-12);
        assertEquals(54.0, product.getEntry(1, 2), 1e-12);
        assertEquals(63.0, product.getEntry(2, 0), 1e-12);
        assertEquals(72.0, product.getEntry(2, 1), 1e-12);
        assertEquals(81.0, product.getEntry(2, 2), 1e-12);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplicationWithIncompatibleSizes() {
        RealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        RealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        RealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(4.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        RealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(0, 0, 5.0);
        matrix.setEntry(1, 1, 6.0);

        assertEquals(5.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(6.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        RealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 2.0);
        matrix.addToEntry(0, 1, 3.0);
        matrix.addToEntry(1, 0, 5.0);
        matrix.addToEntry(1, 1, 7.0);

        assertEquals(3.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(5.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(8.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(11.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        RealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(6.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(9.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(12.0, matrix.getEntry(1, 1), 1e-12);
    }
}