import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructorWithValidArguments() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        assertEquals(originalMatrix.entries.size(), copiedMatrix.entries.size());
    }

    @Test
    public void testAddMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(2.0, result.getEntry(0, 0), 0.001);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(1, 0), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtractMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(-1.0, result.getEntry(0, 0), 0.001);
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(1, 0), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplyMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        RealMatrix result = matrix1.multiply(matrix2);
        BlockRealMatrix expectedResult = new BlockRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expectedResult.setEntry(i, j, 3.0 * 1.0 + 4.0 * 2.0);
            }
        }
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyWithIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
        assertNull(matrix.getEntry(1, 0));
        assertNull(matrix.getEntry(0, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidArguments() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidArguments() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);

        matrix.addToEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidArguments() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, 1.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);

        matrix.multiplyEntry(1, 1, 3.0);
        assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidArguments() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.multiplyEntry(3, 3, 1.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryWithNegativeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        matrix.setEntry(0, 0, -1.0);
    }

}