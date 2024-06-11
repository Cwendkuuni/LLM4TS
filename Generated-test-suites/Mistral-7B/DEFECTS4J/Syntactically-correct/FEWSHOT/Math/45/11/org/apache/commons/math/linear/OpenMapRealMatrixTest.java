package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(3, 3));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(matrix1, matrix2);
        assertNotSame(matrix1.entries, matrix2.entries);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(2, 2, 4.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(2.0, result.getEntry(1, 1), 0.001);
        assertEquals(1.0, result.getEntry(0, 0));
        assertEquals(4.0, result.getEntry(2, 2));
        assertEquals(0.0, result.getEntry(0, 2));
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testAdditionMatrixDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(2, 2, 4.0);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(2, 2, 2.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(0.0, result.getEntry(0, 0), 0.001);
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
        assertEquals(1.0, result.getEntry(2, 2), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(2, 2, 2.0);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix2.setEntry(0, 0, 7.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(1, 0, 9.0);
        matrix2.setEntry(1, 1, 10.0);
        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(33.0 + 40.0 * 10.0 + 45.0 * 8.0 + 48.0 * 7.0, Precision.doubleValue(result.getEntry(0, 0), 0.001), 0.0);
        assertEquals(26.0 + 30.0 * 10.0 + 33.0 * 8.0 + 36.0 * 7.0, Precision.doubleValue(result.getEntry(0, 1), 0.001), 0.0);
        assertEquals(42.0 + 45.0 * 10.0 + 48.0 * 8.0 + 51.0 * 7.0, Precision.doubleValue(result.getEntry(1, 0), 0.001), 0.0);
        assertEquals(36.0 + 40.0 * 10.0 + 45.0 * 8.0 + 48.0 * 10.0, Precision.doubleValue(result.getEntry(1, 1), 0.001), 0.0);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplicationMatrixDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix2.setEntry(0, 0, 7.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(1, 0, 9.0);
        matrix2.setEntry(1, 1, 10.0);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGettingSettingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0));
        assertEquals(2.0, matrix.getEntry(1, 1));
        assertNull(matrix.getEntry(2, 2));
        matrix.setEntry(2, 2, 3.0);
        assertEquals(3.0, matrix.getEntry(2, 2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testGettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertNull(matrix.getEntry(3, 3));
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0));
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrixSerialization() throws Exception {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(matrix);
        OpenMapRealMatrix matrixFromStream = (OpenMapRealMatrix) new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())).readObject();
        assertEquals(1.0, matrixFromStream.getEntry(0, 0));
        assertEquals(2.0, matrixFromStream.getEntry(1, 1));
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix2.setEntry(0, 0, 7.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(1, 0, 9.0);
        matrix2.setEntry(1, 1, 10.0);
        BlockRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(33.0 + 40.0 * 10.0 + 45.0 * 8.0 + 48.0 * 7.0, Precision.doubleValue(result.getEntry(0, 0), 0.001), 0.0);
        assertEquals(26.0 + 30.0 * 10.0 + 33.0 * 8.0 + 36.0 * 7.0, Precision.doubleValue(result.getEntry(0, 1), 0.001), 0.0);
        assertEquals(42.0 + 45.0 * 10.0 + 48.0 * 8.0 + 51.0 * 7.0, Precision.doubleValue(result.getEntry(1, 0), 0.001), 0.0);
        assertEquals(36.0 + 40.0 * 10.0 + 45.0 * 8.0 + 48.0 * 10.0, Precision.doubleValue(result.getEntry(1, 1), 0.001), 0.0);
    }
}