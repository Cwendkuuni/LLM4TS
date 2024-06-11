import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Test
    public void testConstructor() {
        matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(matrix1.getEntry(0, 0), matrix2.getEntry(0, 0), 0.0);
        assertEquals(matrix1.getEntry(1, 1), matrix2.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 3.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(2.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 3.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(7.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(10.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalRowAccess() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalColumnAccess() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(1, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.subtract(matrix2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.multiply(matrix2);
    }

}