import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        OpenMapRealMatrix copyMatrix = matrix.copy();
        assertEquals(5.0, copyMatrix.getEntry(1, 1), 0.0001);
        assertNotSame(matrix, copyMatrix);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(4, 4);
        assertEquals(4, createdMatrix.getRowDimension());
        assertEquals(4, createdMatrix.getColumnDimension());
        assertEquals(0.0, createdMatrix.getEntry(0, 0), 0.0001);
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        assertEquals(4, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix resultMatrix = matrix1.add(matrix2);

        assertEquals(3.0, resultMatrix.getEntry(0, 0), 0.0001);
        assertEquals(5.0, resultMatrix.getEntry(0, 1), 0.0001);
        assertEquals(7.0, resultMatrix.getEntry(1, 0), 0.0001);
        assertEquals(9.0, resultMatrix.getEntry(1, 1), 0.0001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 5.0);
        matrix1.setEntry(0, 1, 6.0);
        matrix1.setEntry(1, 0, 7.0);
        matrix1.setEntry(1, 1, 8.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix resultMatrix = matrix1.subtract(matrix2);

        assertEquals(3.0, resultMatrix.getEntry(0, 0), 0.0001);
        assertEquals(3.0, resultMatrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, resultMatrix.getEntry(1, 0), 0.0001);
        assertEquals(3.0, resultMatrix.getEntry(1, 1), 0.0001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix resultMatrix = matrix1.multiply(matrix2);

        assertEquals(10.0, resultMatrix.getEntry(0, 0), 0.0001);
        assertEquals(13.0, resultMatrix.getEntry(0, 1), 0.0001);
        assertEquals(22.0, resultMatrix.getEntry(1, 0), 0.0001);
        assertEquals(29.0, resultMatrix.getEntry(1, 1), 0.0001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(4, 4);
        assertEquals(4, matrix.getRowDimension());
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(2, 2, 7.0);
        assertEquals(7.0, matrix.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 3.0);
        assertEquals(8.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(10.0, matrix.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(1, 2);
        assertEquals(5, key);
    }

}