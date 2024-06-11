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
    public void testGetRowDimension() {
        Assert.assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testGetColumnDimension() {
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 2, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix.add(m);
        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        OpenMapRealMatrix result = matrix.subtract(m);
        Assert.assertEquals(-1.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix.multiply(m);
        Assert.assertEquals(0.0, result.getEntry(0, 0), 0.0);
    }

    @Test
    public void testCopy() {
        matrix.setEntry(0, 0, 1.0);
        OpenMapRealMatrix copy = matrix.copy();
        Assert.assertEquals(matrix.getEntry(0, 0), copy.getEntry(0, 0), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(4.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidRow() {
        matrix.getEntry(4, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidColumn() {
        matrix.getEntry(0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidRow() {
        matrix.setEntry(4, 0, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidColumn() {
        matrix.setEntry(0, 4, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidRow() {
        matrix.addToEntry(4, 0, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidColumn() {
        matrix.addToEntry(0, 4, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidRow() {
        matrix.multiplyEntry(4, 0, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidColumn() {
        matrix.multiplyEntry(0, 4, 1.0);
    }
}