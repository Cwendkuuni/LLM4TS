package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math3.util.Precision;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 1));
        assertEquals(0, matrix.entries.size());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix copyMatrix = originalMatrix.copy();
        assertEquals(originalMatrix, copyMatrix);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix sumMatrix = matrix1.add(matrix2);

        OpenMapRealMatrix expectedSumMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        assertEquals(expectedSumMatrix, sumMatrix);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double expectedValue = matrix1.getEntry(i / 2, j / 2) + matrix2.getEntry(i / 2, j / 2);
                double actualValue = sumMatrix.getEntry(i / 2, j / 2);
                assertEquals("Index: (" + i + ", " + j + ")", expectedValue, Precision.round(actualValue, 10));
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix differenceMatrix = matrix1.subtract(matrix2);

        OpenMapRealMatrix expectedDifferenceMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2)));
        assertEquals(expectedDifferenceMatrix, differenceMatrix);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double expectedValue = matrix1.getEntry(i / 2, j / 2) - matrix2.getEntry(i / 2, j / 2);
                double actualValue = differenceMatrix.getEntry(i / 2, j / 2);
                assertEquals("Index: (" + i + ", " + j + ")", expectedValue, Precision.round(actualValue, 10));
            }
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix productMatrix = matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplicationIdentity() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix identityMatrix = matrix.copy();
        matrix.multiply(identityMatrix);
        assertEquals(matrix, identityMatrix);
    }

    @Test
    public void testOpenMapRealMatrixMultiplicationMatrix1x1() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 1).setEntry(0, 0, 3.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(1, 1).setEntry(0, 0, 2.0);
        OpenMapRealMatrix productMatrix = matrix1.multiply(matrix2);

        OpenMapRealMatrix expectedProductMatrix = new OpenMapRealMatrix(1, 1).setEntry(0, 0, 6.0);
        assertEquals(expectedProductMatrix, productMatrix);
    }

    @Test
    public void testOpenMapRealMatrixMultiplicationMatrix2x1() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 1).setEntry(0, 0, 3.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(1, 1).setEntry(0, 0, 2.0);
        OpenMapRealMatrix productMatrix = matrix1.multiply(matrix2);

        OpenMapRealMatrix expectedProductMatrix = new OpenMapRealMatrix(2, 1).setEntry(0, 0, 6.0)
                .setEntry(1, 0, 6.0);
        assertEquals(expectedProductMatrix, productMatrix);
    }

    @Test
    public void testOpenMapRealMatrixMultiplicationMatrix2x2() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 1.0)
                .setEntry(0, 1, 2.0)
                .setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 1.0)
                .setEntry(0, 1, 2.0)
                .setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0);
        OpenMapRealMatrix productMatrix = matrix1.multiply(matrix2);

        OpenMapRealMatrix expectedProductMatrix = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 15.0)
                .setEntry(0, 1, 12.0)
                .setEntry(1, 0, 12.0)
                .setEntry(1, 1, 15.0);
        assertEquals(expectedProductMatrix, productMatrix);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 3.14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(-1, -1, 3.14);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 0.0);
        assertNull(matrix.getEntry(1, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(-1, -1);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 3.14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(-1, -1, 3.14);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2).setEntry(0, 0, 3.14);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(6.28, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 2.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(-1, -1, 2.0);
    }
}