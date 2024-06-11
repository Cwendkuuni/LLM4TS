import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    // Test case for the copy method
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(1.0, matrix2.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix2.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix2.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix2.getEntry(1, 1), 0.0001);
    }

    // Test case for the add method
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0001);
        assertEquals(8.0, result.getEntry(0, 1), 0.0001);
        assertEquals(10.0, result.getEntry(1, 0), 0.0001);
        assertEquals(12.0, result.getEntry(1, 1), 0.0001);
    }

    // Test case for the subtract method
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(1, 0, 7.0);
        matrix2.setEntry(1, 1, 8.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(-4.0, result.getEntry(0, 0), 0.0001);
        assertEquals(-4.0, result.getEntry(0, 1), 0.0001);
        assertEquals(-4.0, result.getEntry(1, 0), 0.0001);
        assertEquals(-4.0, result.getEntry(1, 1), 0.0001);
    }

    // Test case for the multiply method
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 2.0);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0001);
        assertEquals(6.0, result.getEntry(0, 1), 0.0001);
        assertEquals(14.0, result.getEntry(1, 0), 0.0001);
        assertEquals(14.0, result.getEntry(1, 1), 0.0001);
    }

    // Test case for the getEntry method
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    // Test case for the setEntry method
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.0001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0001);
    }

    // Test case for the addToEntry method
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.addToEntry(0, 1, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(5.0, matrix.getEntry(0, 1), 0.0001);
    }

    // Test case for the multiplyEntry method
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.multiplyEntry(0, 1, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.0001);
        assertEquals(6.0, matrix.getEntry(0, 1), 0.0001);
    }

    // Test cases for error scenarios
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(0, 2);
    }

    // Edge case for empty matrix
    @Test
    public void testEmptyMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(0, 0);
        assertEquals(0, matrix.getRowDimension());
        assertEquals(0, matrix.getColumnDimension());
    }

}