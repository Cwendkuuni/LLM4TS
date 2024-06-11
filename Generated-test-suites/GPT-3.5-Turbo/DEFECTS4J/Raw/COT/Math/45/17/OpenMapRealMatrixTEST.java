import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(result.getEntry(0, 0), 3.0, 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(result.getEntry(0, 0), 1.0, 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(result.getEntry(0, 0), 6.0, 0.0001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        double entry = matrix.getEntry(0, 0);
        assertEquals(entry, 2.0, 0.0001);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        double entry = matrix.getEntry(0, 0);
        assertEquals(entry, 2.0, 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.addToEntry(0, 0, 3.0);
        double entry = matrix.getEntry(0, 0);
        assertEquals(entry, 5.0, 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        double entry = matrix.getEntry(0, 0);
        assertEquals(entry, 6.0, 0.0001);
    }

    // Testing edge cases and exception handling scenarios

    @Test(expected = org.apache.commons.math.exception.NumberIsTooLargeException.class)
    public void testCreateMatrixLargeDimensions() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testAddSizeMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testSubtractSizeMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testMultiplySizeMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
    }

    @Test(expected = org.apache.commons.math.exception.DimensionMismatchException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        double entry = matrix.getEntry(2, 2);
    }

    @Test(expected = org.apache.commons.math.exception.NumberIsTooLargeException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 2.0);
    }

    @Test(expected = org.apache.commons.math.exception.NumberIsTooLargeException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 2.0);
    }

    @Test(expected = org.apache.commons.math.exception.NumberIsTooLargeException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 2.0);
    }

}