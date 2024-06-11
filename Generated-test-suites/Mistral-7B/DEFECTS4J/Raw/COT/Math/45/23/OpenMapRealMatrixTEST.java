import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        Assert.assertNotSame(matrix, copy);
        Assert.assertEquals(matrix, copy);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(0, 0, 1.0);
        other.setEntry(1, 1, 2.0);

        matrix.add(other);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(2, 3);
        matrix.add(other);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(0, 0, 1.0);
        other.setEntry(1, 1, 2.0);

        matrix.subtract(other);

        Assert.assertEquals(-1.0, matrix.getEntry(0, 0), 1e-14);
        Assert.assertEquals(-2.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 2);
        matrix.subtract(other);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
        other.setEntry(0, 0, 1.0);
        other.setEntry(0, 1, 2.0);
        other.setEntry(1, 0, 3.0);
        other.setEntry(1, 1, 4.0);

        RealMatrix result = matrix.multiply(other);

        BlockRealMatrix expected = new BlockRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0 * 1.0 + 2.0 * 3.0);
        expected.setEntry(0, 1, 1.0 * 2.0 + 2.0 * 4.0);
        expected.setEntry(0, 2, 1.0 * 0.0 + 2.0 * 0.0);
        expected.setEntry(1, 0, 3.0 * 1.0 + 4.0 * 3.0);
        expected.setEntry(1, 1, 3.0 * 2.0 + 4.0 * 4.0);
        expected.setEntry(1, 2, 3.0 * 0.0 + 4.0 * 0.0);
        expected.setEntry(2, 0, 0.0 * 1.0 + 0.0 * 3.0);
        expected.setEntry(2, 1, 0.0 * 2.0 + 0.0 * 4.0);
        expected.setEntry(2, 2, 0.0 * 0.0 + 0.0 * 0.0);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix other = new OpenMapRealMatrix(3, 2);
        matrix.multiply(other);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 1, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testComputeKey() {
        Assert.assertEquals(0, matrix.computeKey(0, 0));
        Assert.assertEquals(3, matrix.computeKey(0, 2));
    }
}