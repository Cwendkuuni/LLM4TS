import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for OpenMapRealMatrix.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor with valid dimensions.
     */
    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testOpenMapRealMatrixCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix, copy);
    }

    /**
     * Test getEntry method with valid indices.
     */
    @Test
    public void testGetEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 1e-12);
    }

    /**
     * Test getEntry method with invalid row index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.getEntry(3, 0);
    }

    /**
     * Test getEntry method with invalid column index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.getEntry(0, 4);
    }

    /**
     * Test setEntry method with valid indices and value.
     */
    @Test
    public void testSetEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 1e-12);
    }

    /**
     * Test setEntry method with valid indices and zero value.
     */
    @Test
    public void testSetEntryValidIndicesZeroValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-12);
    }

    /**
     * Test setEntry method with invalid row index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 0, 3.14);
    }

    /**
     * Test setEntry method with invalid column index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 4, 3.14);
    }

    /**
     * Test add method with valid matrices.
     */
    @Test
    public void testAddValidMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][] {{0.5, -1.5}, {-2.5, 3.5}});
        OpenMapRealMatrix c = a.add(b);
        assertEquals(2, c.getRowDimension());
        assertEquals(2, c.getColumnDimension());
        assertEquals(1.5, c.getEntry(0, 0), 1e-12);
        assertEquals(-1.0, c.getEntry(0, 1), 1e-12);
        assertEquals(0.5, c.getEntry(1, 0), 1e-12);
        assertEquals(3.0, c.getEntry(1, 1), 1e-12);
    }

    /**
     * Test add method with matrices of different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddDifferentDimensionsMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][] {{1.0, 2.0}});
        a.add(b);
    }

    /**
     * Test multiply method with valid matrices.
     */
    @Test
    public void testMultiplyValidMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][] {{5.0, 6.0}, {7.0, 8.0}});
        OpenMapRealMatrix c = a.multiply(b);
        assertEquals(2, c.getRowDimension());
        assertEquals(2, c.getColumnDimension());
        assertEquals(19.0, c.getEntry(0, 0), 1e-12);
        assertEquals(22.0, c.getEntry(0, 1), 1e-12);
        assertEquals(31.0, c.getEntry(1, 0), 1e-12);
        assertEquals(38.0, c.getEntry(1, 1), 1e-12);
    }

    /**
     * Test multiply method with matrices of different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyDifferentDimensionsMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][] {{5.0, 6.0}});
        a.multiply(b);
    }

    /**
     * Test addToEntry method with valid indices and value.
     */
    @Test
    public void testAddToEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 1e-12);
    }

    /**
     * Test addToEntry method with invalid row index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(3, 0, 3.14);
    }

    /**
     * Test addToEntry method with invalid column index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 4, 3.14);
    }

    /**
     * Test multiplyEntry method with valid indices and value.
     */
    @Test
    public void testMultiplyEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 3.14);
        assertEquals(3.14, matrix.getEntry(0, 0), 1e-12);
    }

    /**
     * Test multiplyEntry method with invalid row index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(3, 0, 3.14);
    }

    /**
     * Test multiplyEntry method with invalid column index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 4, 3.14);
    }

    /**
     * Test entry is zero and remove it.
     */
    @Test
    public void testRemoveZeroEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
        assertEquals(6, matrix.entries.size());
        matrix.entries.remove(computeKey(0, 0));
        assertEquals(5, matrix.entries.size());
    }

    /**
     * Test exception when adding a large value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, Double.MAX_VALUE);
        matrix.addToEntry(0, 0, 1.0);
    }

    /**
     * Compute the key to access a matrix element
     * @param row row index of the matrix element
     * @param column column index of the matrix element
     * @return key within the map to access the matrix element
     */
    private int computeKey(int row, int column) {
        return row * columns + column;
    }

}