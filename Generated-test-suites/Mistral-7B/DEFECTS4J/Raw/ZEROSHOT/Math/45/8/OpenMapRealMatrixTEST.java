import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OpenMapRealMatrix.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertNotSame(original, copy);
        Assert.assertEquals(original.getRowDimension(), copy.getRowDimension());
        Assert.assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        Assert.assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0));
        Assert.assertEquals(original.getEntry(0, 1), copy.getEntry(0, 1));
        Assert.assertEquals(original.getEntry(1, 0), copy.getEntry(1, 0));
        Assert.assertEquals(original.getEntry(1, 1), copy.getEntry(1, 1));
    }

    /**
     * Test setting and getting entries.
     */
    @Test
    public void testSettingAndGettingEntries() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 1e-14);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 1e-14);
        matrix.setEntry(1, 1, 5.0);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 1e-14);
        Assert.assertEquals(5.0, matrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test adding matrices.
     */
    @Test
    public void testAddingMatrices() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 5.0).setEntry(0, 1, 6.0).setEntry(1, 0, 7.0)
                .setEntry(1, 1, 8.0));
        OpenMapRealMatrix result = left.add(right);
        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(3, result.getColumnDimension());
        Assert.assertEquals(6.0, result.getEntry(0, 0), 1e-14);
        Assert.assertEquals(8.0, result.getEntry(0, 1), 1e-14);
        Assert.assertEquals(10.0, result.getEntry(1, 0), 1e-14);
        Assert.assertEquals(12.0, result.getEntry(1, 1), 1e-14);
    }

    /**
     * Test subtracting matrices.
     */
    @Test
    public void testSubtractingMatrices() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 5.0).setEntry(0, 1, 6.0).setEntry(1, 0, 7.0)
                .setEntry(1, 1, 8.0));
        OpenMapRealMatrix result = left.subtract(right);
        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(3, result.getColumnDimension());
        Assert.assertEquals(-4.0, result.getEntry(0, 0), 1e-14);
        Assert.assertEquals(-4.0, result.getEntry(0, 1), 1e-14);
        Assert.assertEquals(-4.0, result.getEntry(1, 0), 1e-14);
        Assert.assertEquals(-4.0, result.getEntry(1, 1), 1e-14);
    }

    /**
     * Test multiplying matrices.
     */
    @Test
    public void testMultiplyingMatrices() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2)
                .setEntry(0, 0, 5.0).setEntry(0, 1, 6.0).setEntry(1, 0, 7.0)
                .setEntry(1, 1, 8.0).setEntry(2, 0, 9.0).setEntry(2, 1, 10.0));
        OpenMapRealMatrix result = left.multiply(right);
        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(2, result.getColumnDimension());
        Assert.assertEquals(35.0, result.getEntry(0, 0), 1e-14);
        Assert.assertEquals(43.0, result.getEntry(0, 1), 1e-14);
        Assert.assertEquals(39.0, result.getEntry(1, 0), 1e-14);
        Assert.assertEquals(52.0, result.getEntry(1, 1), 1e-14);
    }

    /**
     * Test adding and multiplying matrices with different sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddingAndMultiplyingMatricesWithDifferentSizes() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2)
                .setEntry(0, 0, 5.0).setEntry(0, 1, 6.0).setEntry(1, 0, 7.0)
                .setEntry(1, 1, 8.0));
        left.add(right);

        left.multiply(right);
    }

    /**
     * Test adding a null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddingANullMatrix() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix right = null;
        left.add(right);
    }

    /**
     * Test multiplying a null matrix by a non-null matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyingANullMatrixByANonNullMatrix() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0));
        OpenMapRealMatrix right = null;
        left.multiply(right);
    }

    /**
     * Test getting entry with out-of-bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGettingEntryWithOutOfBoundsIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3)
                .setEntry(0, 0, 1.0).setEntry(0, 1, 2.0).setEntry(1, 0, 3.0)
                .setEntry(1, 1, 4.0);
        matrix.getEntry(3, 3);
    }

    /**
     * Test setting entry with out-of-bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSettingEntryWithOutOfBoundsIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    /**
     * Test adding too large entry.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddingTooLargeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, Double.MAX_VALUE);
    }
}