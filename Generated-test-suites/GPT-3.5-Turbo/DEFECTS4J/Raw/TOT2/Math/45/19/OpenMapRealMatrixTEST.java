import static org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 3.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);

        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 3.0);

        assertEquals(3.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(1, 1, 2.0);

        assertEquals(2.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 3.0);
        matrix.multiplyEntry(1, 1, 2.0);

        assertEquals(6.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        int key = matrix.computeKey(1, 2);

        assertEquals(5, key);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidRowEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(3, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidColumnEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(1, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidRowSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(3, 1, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidColumnSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 3, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidRowAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(3, 1, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidColumnAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(1, 3, 2.0);
    }

}