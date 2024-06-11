package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(matrix1);

        assertEquals(2, matrix1.getRowDimension());
        assertEquals(3, matrix1.getColumnDimension());
        assertEquals(matrix1.getRowDimension() * matrix1.getColumnDimension(), matrix1.entries.size());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();

        original.setEntry(0, 0, 1.0);
        original.setEntry(1, 1, 2.0);

        assertArrayEquals(new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        }, copy.getDataAccess());

        copy.setEntry(0, 0, 3.0);
        copy.setEntry(1, 1, 4.0);

        assertArrayEquals(new double[][]{
                {3.0, 0.0, 0.0},
                {0.0, 4.0, 0.0}
        }, original.getDataAccess());
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(1, 0, 5.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertArrayEquals(new double[][]{
                {4.0, 5.0, 1.0},
                {2.0, 6.0, 2.0}
        }, result.getDataAccess());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOpenMapRealMatrixAddition_DimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 1.0);

        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);

        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 7.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        assertArrayEquals(new double[][]{
                {16.0, 23.0},
                {21.0, 31.0}
        }, result.getDataAccess());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplication_DimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 4);

        matrix1.setEntry(0, 0, 1.0);

        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        double value = 5.0;

        int row = 0, column = 0;
        matrix.setEntry(row, column, value);
        assertEquals(value, matrix.getEntry(row, column));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        double value = 5.0;

        int row = 3, column = 0;
        matrix.setEntry(row, column, value);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        double value = 5.0;

        int row = 0, column = 0;
        matrix.addToEntry(row, column, value);
        assertEquals(value, matrix.getEntry(row, column));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        double value = 5.0;

        int row = 3, column = 0;
        matrix.addToEntry(row, column, value);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        double value = 5.0;

        int row = 0, column = 0;
        matrix.multiplyEntry(row, column, value);
        assertEquals(value * matrix.getEntry(row, column), matrix.getEntry(row, column));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        double value = 5.0;

        int row = 3, column = 0;
        matrix.multiplyEntry(row, column, value);
    }

    @Test
    public void testOpenMapRealMatrixNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        int row = 0, column = Integer.MAX_VALUE;
        assertThrows(NumberIsTooLargeException.class, () -> {
            matrix.setEntry(row, column, 1.0);
        });
    }
}