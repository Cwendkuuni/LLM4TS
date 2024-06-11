import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.MatrixDimensionMismatchException;
import org.apache.commons.math.util.OpenIntToDoubleHashMap;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());

        assertTrue(matrix instanceof SparseRealMatrix);
        assertTrue(matrix instanceof Serializable);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();

        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
        assertNotNull(newMatrix.entries);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(1, 1, 9);

        OpenMapRealMatrix result = matrix.add(other);
        assertEquals(9, result.getEntry(1, 1), 0.0);

        // Test exception for adding matrices with different dimensions
        OpenMapRealMatrix wrongDimensionsMatrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.add(wrongDimensionsMatrix);
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("The matrices are not addition compatible: 3x3 and 2x2", e.getMessage());
        }
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(1, 1, 4);

        OpenMapRealMatrix result = matrix.subtract(other);
        assertEquals(-4, result.getEntry(1, 1), 0.0);

        // Test exception for subtracting matrices with different dimensions
        OpenMapRealMatrix wrongDimensionsMatrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.subtract(wrongDimensionsMatrix);
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("The matrices are not addition compatible: 3x3 and 2x2", e.getMessage());
        }
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(1, 1, 5);

        OpenMapRealMatrix result = matrix.multiply(other);
        assertEquals(0, result.getEntry(1, 1), 0.0);

        // Test exception for multiplying matrices with incompatible dimensions
        OpenMapRealMatrix wrongDimensionsMatrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.multiply(wrongDimensionsMatrix);
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("The matrices are not multiplication compatible: 3x3 and 2x2", e.getMessage());
        }
    }

    @Test
    public void testGetEntryAndSetEntry() {
        matrix.setEntry(1, 1, 2.5);
        assertEquals(2.5, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(1, 1, 2.5);
        matrix.addToEntry(1, 1, 1.5);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(1, 1, 2.5);
        matrix.multiplyEntry(1, 1, 2);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testComputeKey() {
        assertEquals(0, matrix.computeKey(0, 0));
        assertEquals(1, matrix.computeKey(0, 1));
        assertEquals(3, matrix.computeKey(1, 0));
        assertEquals(4, matrix.computeKey(1, 1));
    }
}