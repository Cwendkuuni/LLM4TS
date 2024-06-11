import static org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(matrix1.getEntry(0, 0), matrix2.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(result.getEntry(0, 0), 3.0, 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(result.getEntry(0, 0), 2.0, 0.0);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(matrix.getEntry(1, 1), 5.0, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testGetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(matrix.getEntry(1, 1), 5.0, 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(3, 3, 5.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 3.0);
        assertEquals(matrix.getEntry(0, 0), 3.0, 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(matrix.getEntry(0, 0), 6.0, 0.0);
    }
}