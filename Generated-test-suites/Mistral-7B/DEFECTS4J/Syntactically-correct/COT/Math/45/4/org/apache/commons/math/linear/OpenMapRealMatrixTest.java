import static org.junit.Assert.*;
import org.apache.commons.math.linear.AbstractRealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrix_constructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    @Test
    public void testOpenMapRealMatrix_copyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        assertEquals(originalMatrix.entries.size(), copiedMatrix.entries.size());
    }

    @Test
    public void testOpenMapRealMatrix_copy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        assertEquals(originalMatrix.entries.size(), copiedMatrix.entries.size());
    }

    @Test
    public void testAddMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(1, 0, 5.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(5.0, result.getEntry(0, 0), 0.01);
        assertEquals(6.0, result.getEntry(1, 1), 0.01);
        assertEquals(5.0, result.getEntry(1, 0), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMethod_dimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
    }

    @Test
    public void testSubtractMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        matrix2.setEntry(1, 0, 5.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(-4.0, result.getEntry(0, 0), 0.01);
        assertEquals(-3.0, result.getEntry(1, 1), 0.01);
        assertEquals(-2.0, result.getEntry(1, 0), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractMethod_dimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplyMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(0, 1, 6.0);
        matrix2.setEntry(0, 2, 7.0);
        matrix2.setEntry(1, 0, 8.0);
        matrix2.setEntry(1, 1, 9.0);
        matrix2.setEntry(1, 2, 10.0);
        RealMatrix result = new BlockRealMatrix(2, 3);
        OpenMapRealMatrix openMapResult = matrix1.multiply(matrix2);
        assertEquals(matrix1.getRowDimension(), openMapResult.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), openMapResult.getColumnDimension());
        assertEquals(result.getEntry(0, 0), openMapResult.getEntry(0, 0), 0.01);
        assertEquals(result.getEntry(0, 1), openMapResult.getEntry(0, 1), 0.01);
        assertEquals(result.getEntry(0, 2), openMapResult.getEntry(0, 2), 0.01);
        assertEquals(result.getEntry(1, 0), openMapResult.getEntry(1, 0), 0.01);
        assertEquals(result.getEntry(1, 1), openMapResult.getEntry(1, 1), 0.01);
        assertEquals(result.getEntry(1, 2), openMapResult.getEntry(1, 2), 0.01);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyMethod_dimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 1);
        matrix2.setEntry(0, 0, 5.0);
        matrix2.setEntry(1, 0, 6.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.01);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.01);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryMethod_indexOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(0.0, matrix.getEntry(0, 2), 0.01);
    }

    @Test
    public void testSetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(0, 1, 3.0);
        assertEquals(3.0, matrix.getEntry(0, 1), 0.01);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryMethod_indexOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 2, 3.0);
    }

    @Test
    public void testAddToEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.01);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryMethod_indexOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 2, 3.0);
    }

    @Test
    public void testMultiplyEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.01);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryMethod_indexOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 2, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddEntryBeyondMapCapacity() {
        int initialSize = 10;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        for (int i = 0; i < initialSize * 2; i++) {
            matrix.addToEntry(0, 0, 1.0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryWithNegativeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, -1.0);
    }
}