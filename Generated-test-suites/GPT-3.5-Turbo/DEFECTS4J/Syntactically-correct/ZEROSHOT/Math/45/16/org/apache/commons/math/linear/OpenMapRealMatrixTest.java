import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        OpenMapRealMatrix matrix2 = matrix1.copy();
        assertEquals(1.0, matrix2.getEntry(0, 0), 0.0001);
        matrix1.setEntry(0, 0, 2.0);
        assertEquals(1.0, matrix2.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(0.0, newMatrix.getEntry(0, 0), 0.0001);
        assertEquals(0.0, newMatrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(3, matrix.getColumnDimension());
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
        matrix1.setEntry(0, 0, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 1.0);
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
        matrix.setEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.addToEntry(0, 0, 3.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 0.0001);
    }
}