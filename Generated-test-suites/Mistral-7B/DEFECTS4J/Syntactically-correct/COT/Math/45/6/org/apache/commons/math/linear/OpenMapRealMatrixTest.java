import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copyMatrix = new OpenMapRealMatrix(originalMatrix);
        Assert.assertEquals(originalMatrix, copyMatrix);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix1.setEntry(1, 1, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        Assert.assertEquals(3.0, sum.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).ident());
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix1.setEntry(1, 1, 1.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix2.setEntry(1, 1, 2.0);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        Assert.assertEquals(-1.0, difference.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).ident());
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        RealMatrix product = matrix1.multiply(matrix2);
        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(3, 3);
        expectedResult.setEntry(0, 0, 2.0 * 1.0 + 3.0 * 1.0);
        expectedResult.setEntry(0, 1, 2.0 * 1.0 + 3.0 * 1.0);
        expectedResult.setEntry(0, 2, 2.0 * 1.0 + 3.0 * 1.0);
        expectedResult.setEntry(1, 0, 4.0 * 1.0 + 5.0 * 1.0);
        expectedResult.setEntry(1, 1, 4.0 * 1.0 + 5.0 * 1.0);
        expectedResult.setEntry(1, 2, 4.0 * 1.0 + 5.0 * 1.0);
        expectedResult.setEntry(2, 0, 2.0 * 1.0 + 3.0 * 1.0);
        expectedResult.setEntry(2, 1, 2.0 * 1.0 + 3.0 * 1.0);
        expectedResult.setEntry(2, 2, 2.0 * 1.0 + 3.0 * 1.0);

        Assert.assertArrayEquals(expectedResult.getData(), product.getData(), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).ident());
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.setEntry(1, 1, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.setEntry(1, 1, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.addToEntry(1, 1, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.multiplyEntry(1, 1, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).ident());
        matrix.setEntry(0, 0, Long.MAX_VALUE);
    }

}