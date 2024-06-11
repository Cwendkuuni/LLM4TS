package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealArrayRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(matrix1);

        assertEqualsSizes(matrix1, matrix2);
        assertEqualsSizes(matrix1, matrix3);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.5);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEqualsSizes(matrix1, result);

        assertArrayEquals(new double[]{3.0, 4.5, 6.0, 5.0, 5.5, 6.0}, result.getData(), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.5);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEqualsSizes(matrix1, result);

        assertArrayEquals(new double[]{1.0, 1.5, 1.5, 2.0, 1.5, 2.0}, result.getData(), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.5);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 2.0);

        RealMatrix result = matrix1.multiply(matrix2);

        assertEqualsSizes(matrix1, result);
        assertEqualsSizes(matrix2, result);

        RealArrayRealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{
                {6.0, 7.5},
                {12.0, 10.5}
        });

        assertMatrixEquals(expectedResult, result, 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixMultiplicationWithIncompatibleSize() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(4, 4);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.5);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 2.0);

        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(2.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.setEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(2.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.addToEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(2.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.multiplyEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(4.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixEmpty() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        assertTrue(matrix.isEmpty());

        matrix.setEntry(0, 0, 1.0);

        assertFalse(matrix.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        matrix3.setEntry(0, 0, 2.1);
        matrix3.setEntry(0, 1, 3.1);
        matrix3.setEntry(1, 1, 4.1);

        assertTrue(matrix1.equals(matrix2));

        assertFalse(matrix1.equals(matrix3));
    }

    @Test
    public void testOpenMapRealMatrixHashCode() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        matrix3.setEntry(0, 0, 2.1);
        matrix3.setEntry(0, 1, 3.1);
        matrix3.setEntry(1, 1, 4.1);

        int hashCode1 = matrix1.hashCode();
        int hashCode2 = matrix2.hashCode();
        int hashCode3 = matrix3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testOpenMapRealMatrixToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        String string = matrix.toString();

        assertTrue(string.contains("OpenMapRealMatrix"));
        assertTrue(string.contains("[2 x 3]"));
        assertTrue(string.contains("(sparse)"));
        assertTrue(string.contains("entries ="));
    }

    @Test
    public void testOpenMapRealMatrixClone() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        OpenMapRealMatrix clone = matrix.clone();

        assertEqualsSizes(matrix, clone);
    }

    @Test
    public void testOpenMapRealMatrixZeroMatrix() {
        OpenMapRealMatrix matrix = OpenMapRealMatrix.ZERO_MATRIX;

        assertEquals(0, matrix.getRowDimension());
        assertEquals(0, matrix.getColumnDimension());
    }

    @Test
    public void testOpenMapRealMatrixIdentityMatrix() {
        OpenMapRealMatrix matrix = OpenMapRealMatrix.IDENTITY_MATRIX_2;

        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    assertEquals(1.0, matrix.getEntry(i, j), 0.0);
                } else {
                    assertEquals(0.0, matrix.getEntry(i, j), 0.0);
                }
            }
        }
    }

    private void assertEqualsSizes(OpenMapRealMatrix matrix1, OpenMapRealMatrix matrix2) {
        assertEquals(matrix1.getRowDimension(), matrix2.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), matrix2.getColumnDimension());
    }

    private void assertMatrixEquals(RealMatrix expected, RealMatrix actual, double epsilon) {
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());

        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                double expectedValue = expected.getEntry(i, j);
                double actualValue = actual.getEntry(i, j);
                double difference = Math.abs(expectedValue - actualValue);

                if (difference > epsilon) {
                    fail("Expected value[" + i + "][" + j + "] = " + expectedValue
                            + ", but actual value[" + i + "][" + j + "] = " + actualValue);
                }
            }
        }
    }

    private void assertArrayEquals(double[] expected, OpenMapRealMatrix actual, double epsilon) {
        OpenMapRealMatrix expectedMatrix = new OpenMapRealMatrix(actual.getRowDimension(), actual.getColumnDimension());

        for (int i = 0; i < expected.length; i++) {
            expectedMatrix.setEntry(i / actual.getColumnDimension(), i % actual.getColumnDimension(), expected[i]);
        }

        assertMatrixEquals(expectedMatrix, actual, epsilon);
    }

    private void assertArrayEquals(OpenMapRealMatrix expected, double[] actual, double epsilon) {
        OpenMapRealMatrix expectedMatrix = new OpenMapRealMatrix(expected.getRowDimension(), expected.getColumnDimension());

        for (int i = 0; i < expected.getSize(); i++) {
            expectedMatrix.setEntry(i / expected.getColumnDimension(), i % expected.getColumnDimension(), expected.getEntry(i));
        }

        assertMatrixEquals(expectedMatrix, new OpenMapRealMatrix(actual), epsilon);
    }

    @Test
    public void testOpenMapRealMatrixGetKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        int key = matrix.computeKey(0, 0);

        assertEquals(0, key % columns);
        assertEquals(0, key / columns);
    }

    @Test
    public void testOpenMapRealMatrixEntryExists() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        boolean exists = matrix.entries.containsKey(computeKey(0, 0));

        assertFalse(exists);
    }

    @Test
    public void testOpenMapRealMatrixEntryDoesNotExist() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        boolean exists = matrix.entries.containsKey(computeKey(1, 1) + 1);

        assertFalse(exists);
    }

    private int computeKey(int row, int column) {
        return row * columns + column;
    }

    @Test
    public void testOpenMapRealMatrixEntryRemoval() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.setEntry(0, 0, 2.0);

        boolean existsBeforeRemoval = matrix.entries.containsKey(computeKey(0, 0));

        matrix.entries.remove(computeKey(0, 0));

        boolean existsAfterRemoval = matrix.entries.containsKey(computeKey(0, 0));

        assertFalse(existsBeforeRemoval);
        assertFalse(existsAfterRemoval);
    }

    @Test
    public void testOpenMapRealMatrixEntrySet() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.setEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(2.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixEntryAddition() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.addToEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(2.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixEntryMultiplication() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.multiplyEntry(0, 0, 2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(4.0, value, 0.0);
    }

    @Test
    public void testOpenMapRealMatrixEntryNegativeNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);

        matrix.setEntry(0, 0, -2.0);

        double value = matrix.getEntry(0, 0);

        assertEquals(-2.0, value, 0.0);
    }
}