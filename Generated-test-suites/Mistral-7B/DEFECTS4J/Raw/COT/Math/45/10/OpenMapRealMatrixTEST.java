import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertEquals(original.getRowDimension(), copy.getRowDimension());
        Assert.assertEquals(original.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        m1.setEntry(0, 0, 2.0);
        m1.setEntry(0, 1, 3.0);
        m1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);
        m2.setEntry(1, 0, 1.0);
        m2.setEntry(1, 2, 2.0);
        m2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix sum = m1.add(m2);

        Assert.assertEquals(m1.getRowDimension(), sum.getRowDimension());
        Assert.assertEquals(m1.getColumnDimension(), sum.getColumnDimension());
        Assert.assertEquals(m1.getEntry(0, 0), sum.getEntry(0, 0), 0.001);
        Assert.assertEquals(m1.getEntry(0, 1), sum.getEntry(0, 1), 0.001);
        Assert.assertEquals(m1.getEntry(1, 1), sum.getEntry(1, 1), 0.001);
        Assert.assertEquals(m2.getEntry(1, 0), sum.getEntry(1, 0), 0.001);
        Assert.assertEquals(m2.getEntry(1, 1), sum.getEntry(1, 1), 0.001);
        Assert.assertEquals(m2.getEntry(1, 2), sum.getEntry(1, 2), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 2);
        m1.add(m2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        m1.setEntry(0, 0, 2.0);
        m1.setEntry(0, 1, 3.0);
        m1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);
        m2.setEntry(1, 0, 1.0);
        m2.setEntry(1, 2, 2.0);
        m2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix difference = m1.subtract(m2);

        Assert.assertEquals(m1.getRowDimension(), difference.getRowDimension());
        Assert.assertEquals(m1.getColumnDimension(), difference.getColumnDimension());
        Assert.assertEquals(m1.getEntry(0, 0), difference.getEntry(0, 0), 0.001);
        Assert.assertEquals(m1.getEntry(0, 1), difference.getEntry(0, 1), 0.001);
        Assert.assertEquals(m1.getEntry(1, 1), difference.getEntry(1, 1), 0.001);
        Assert.assertEquals(m2.getEntry(1, 0), difference.getEntry(1, 0), 0.001);
        Assert.assertEquals(m2.getEntry(1, 1), difference.getEntry(1, 1), 0.001);
        Assert.assertEquals(m2.getEntry(1, 2), difference.getEntry(1, 2), 0.001);
    }

    @Test
    public void testSubtractionWithZeroMatrix() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix zeroMatrix = new OpenMapRealMatrix(2, 3);

        OpenMapRealMatrix difference = m1.subtract(zeroMatrix);

        Assert.assertEquals(m1.getRowDimension(), difference.getRowDimension());
        Assert.assertEquals(m1.getColumnDimension(), difference.getColumnDimension());
        Assert.assertEquals(m1.getEntry(0, 0), difference.getEntry(0, 0), 0.001);
        Assert.assertEquals(m1.getEntry(0, 1), difference.getEntry(0, 1), 0.001);
        Assert.assertEquals(m1.getEntry(1, 1), difference.getEntry(1, 1), 0.001);
        Assert.assertEquals(0.0, difference.getEntry(0, 2), 0.001);
        Assert.assertEquals(0.0, difference.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.0, difference.getEntry(1, 2), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 2);
        m1.subtract(m2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 2);
        m1.setEntry(0, 0, 1.0);
        m1.setEntry(0, 1, 2.0);
        m1.setEntry(1, 0, 3.0);
        m1.setEntry(1, 1, 4.0);
        m1.setEntry(2, 0, 5.0);
        m1.setEntry(2, 1, 6.0);

        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 3);
        m2.setEntry(0, 0, 7.0);
        m2.setEntry(0, 1, 8.0);
        m2.setEntry(0, 2, 9.0);
        m2.setEntry(1, 0, 1.0);
        m2.setEntry(1, 1, 2.0);
        m2.setEntry(1, 2, 3.0);

        RealMatrix result = m1.multiply(m2);
        BlockRealMatrix expectedResult = new BlockRealMatrix(3, 3);

        expectedResult.setEntry(0, 0, 31.0);
        expectedResult.setEntry(0, 1, 34.0);
        expectedResult.setEntry(0, 2, 37.0);
        expectedResult.setEntry(1, 0, 43.0);
        expectedResult.setEntry(1, 1, 52.0);
        expectedResult.setEntry(1, 2, 57.0);
        expectedResult.setEntry(2, 0, 65.0);
        expectedResult.setEntry(2, 1, 72.0);
        expectedResult.setEntry(2, 2, 79.0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assert.assertEquals(expectedResult.getEntry(i, j), result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testMultiplicationZeroMatrix() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 2);

        RealMatrix result = m1.multiply(OpenMapRealMatrix.ZERO_MATRIX);

        BlockRealMatrix expectedResult = new BlockRealMatrix(3, 2);
        Assert.assertArrayEquals(expectedResult.getEntry(0, 0), result.getEntry(0, 0), 0.001);
        Assert.assertArrayEquals(expectedResult.getEntry(0, 1), result.getEntry(0, 1), 0.001);
        Assert.assertArrayEquals(expectedResult.getEntry(1, 0), result.getEntry(1, 0), 0.001);
        Assert.assertArrayEquals(expectedResult.getEntry(1, 1), result.getEntry(1, 1), 0.001);
        Assert.assertArrayEquals(expectedResult.getEntry(2, 0), result.getEntry(2, 0), 0.001);
        Assert.assertArrayEquals(expectedResult.getEntry(2, 1), result.getEntry(2, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 1);
        m1.multiply(m2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(1, 1, 5.5);
        Assert.assertEquals(5.5, m.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.getEntry(2, 2);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(1, 1, 5.5);
        Assert.assertEquals(5.5, m.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(2, 2, 5.5);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(1, 1, 5.5);
        m.addToEntry(1, 1, 3.3);
        Assert.assertEquals(8.8, m.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.addToEntry(2, 2, 3.3);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.setEntry(1, 1, 5.5);
        m.multiplyEntry(1, 1, 2.0);
        Assert.assertEquals(11.0, m.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        m.multiplyEntry(2, 2, 2.0);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        Assert.assertEquals(m.computeKey(1, 1), 1);
        Assert.assertEquals(m.computeKey(1, 2), 2);
        Assert.assertEquals(m.computeKey(2, 1), 3);
        Assert.assertEquals(m.computeKey(2, 2), 4);
    }
}