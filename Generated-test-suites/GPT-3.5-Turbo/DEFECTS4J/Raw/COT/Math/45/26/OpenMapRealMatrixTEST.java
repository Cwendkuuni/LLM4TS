
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    // Test cases for constructor
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    // Test cases for copy()
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = matrix.copy();
        assertNotSame(matrix, copiedMatrix);
        assertEquals(matrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copiedMatrix.getColumnDimension());
    }

    // Test cases for createMatrix()
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    // Test cases for getColumnDimension()
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }

    // Test cases for add()
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sumMatrix = matrix1.add(matrix2);
        assertEquals(matrix1.getRowDimension(), sumMatrix.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), sumMatrix.getColumnDimension());
    }

    // Test cases for subtract()
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix diffMatrix = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), diffMatrix.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), diffMatrix.getColumnDimension());
    }

    // Test cases for multiply()
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        RealMatrix productMatrix = matrix1.multiply(matrix2);
        assertEquals(matrix1.getRowDimension(), productMatrix.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), productMatrix.getColumnDimension());
    }

    // Test cases for getEntry()
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 4.5);
        assertEquals(4.5, matrix.getEntry(1, 1), 0.0001);
    }

    // Test cases for setEntry()
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 4.5);
        assertEquals(4.5, matrix.getEntry(1, 1), 0.0001);
    }

    // Test cases for addToEntry()
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 4.5);
        matrix.addToEntry(1, 1, 2.5);
        assertEquals(7.0, matrix.getEntry(1, 1), 0.0001);
    }

    // Test cases for multiplyEntry()
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 4.5);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(9.0, matrix.getEntry(1, 1), 0.0001);
    }
}

