import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    @Test
    public void testAddMethod() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        left.setEntry(0, 0, 1.0);
        right.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = left.add(right);
        assertEquals(1.0 + 2.0, result.getEntry(0, 0), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddMethodWithDifferentSizes() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        left.add(right);
    }

    @Test
    public void testSubtractMethod() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        left.setEntry(0, 0, 1.0);
        right.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = left.subtract(right);
        assertEquals(-1.0, result.getEntry(0, 0), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractMethodWithDifferentSizes() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        left.subtract(right);
    }

    @Test
    public void testMultiplyMethod() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        left.setEntry(0, 0, 1.0);
        left.setEntry(0, 1, 2.0);
        right.setEntry(0, 0, 3.0);
        right.setEntry(1, 1, 4.0);

        RealMatrix result = left.multiply(right);

        MatrixUtils.assertEquals(new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3)), result);
        assertEquals(1.0 * 3.0 + 2.0 * 4.0, result.getEntry(0, 0), 1e-10);
        assertEquals(1.0 * 3.0, result.getEntry(0, 1), 1e-10);
        assertEquals(1.0 * 3.0, result.getEntry(0, 2), 1e-10);
        assertEquals(2.0 * 3.0 + 2.0 * 4.0, result.getEntry(1, 0), 1e-10);
        assertEquals(2.0 * 3.0, result.getEntry(1, 1), 1e-10);
        assertEquals(2.0 * 3.0, result.getEntry(1, 2), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyMethodWithDifferentSizes() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        left.multiply(right);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.getEntry(3, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.getEntry(0, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.setEntry(3, 0, 1.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.setEntry(0, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.addToEntry(3, 0, 2.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.addToEntry(0, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.multiplyEntry(3, 0, 2.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.multiplyEntry(0, 3, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddEntryThatIsTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        matrix.entries.put(-1, Double.MAX_VALUE);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddEntryThatIsTooLargeWithAddition() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        left.setEntry(0, 0, Double.MAX_VALUE);
        right.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = left.add(right);
    }
}