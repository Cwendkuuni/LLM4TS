import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertNotSame(matrix1, matrix2);
        assertEquals(matrix1.getRowDimension(), matrix2.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), matrix2.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 2.5);
        assertEquals(2.5, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 2.5);
        assertEquals(2.5, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 2.5);
        matrix2.setEntry(1, 1, 3.5);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(6.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 1, 3.5);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(1.5, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(6.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 2.5);
        matrix.addToEntry(1, 1, 1.5);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(1, 1, 1.5);
        assertEquals(3.0, matrix.getEntry(1, 1), 0.0);
    }

}