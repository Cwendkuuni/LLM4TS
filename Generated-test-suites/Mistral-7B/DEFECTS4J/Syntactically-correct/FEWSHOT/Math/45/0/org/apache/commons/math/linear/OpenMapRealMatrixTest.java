package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixCreationWithNegativeDimension() {
        new OpenMapRealMatrix(-1, -1);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        assertEquals(originalMatrix, copiedMatrix);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(2.0, result.getEntry(0, 0), 0.001);
        assertEquals(6.0, result.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(-2.0, result.getEntry(0, 0), 0.001);
        assertEquals(-2.0, result.getEntry(1, 1), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);

        RealMatrix result = matrix1.multiply(matrix2);
        BlockRealMatrix expectedResult = new BlockRealMatrix(2, 3);
        expectedResult.setEntry(0, 0, 17.0);
        expectedResult.setEntry(0, 1, 23.0);
        expectedResult.setEntry(0, 2, 29.0);
        expectedResult.setEntry(1, 0, 35.0);
        expectedResult.setEntry(1, 1, 47.0);
        expectedResult.setEntry(1, 2, 59.0);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testOpenMapRealMatrixEntryAccess() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);

        matrix.setEntry(1, 0, 3.0);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixEntrySetting() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);

        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);

        matrix.setEntry(0, 1, 3.0);
        assertEquals(3.0, matrix.getEntry(0, 1), 0.001);

        matrix.setEntry(1, 0, 4.0);
        assertEquals(4.0, matrix.getEntry(1, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixEntryAddition() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixEntryMultiplication() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testOpenMapRealMatrixKeyComputation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);

        int key = matrix.computeKey(0, 0);
        assertEquals(0, key % 2);
        assertEquals(0, key / 2);

        key = matrix.computeKey(1, 1);
        assertEquals(1, key % 2);
        assertEquals(1, key / 2);
    }
}