import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertFalse(matrix.isFullMatrix());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);
        OpenMapRealMatrix expectedSum = new OpenMapRealMatrix(2, 3);
        expectedSum.setEntry(0, 0, 3.0);
        expectedSum.setEntry(0, 1, 3.0);
        expectedSum.setEntry(1, 1, 7.0);

        Assert.assertEquals(expectedSum, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        OpenMapRealMatrix expectedDifference = new OpenMapRealMatrix(2, 3);
        expectedDifference.setEntry(0, 0, 0.0);
        expectedDifference.setEntry(0, 1, 1.0);
        expectedDifference.setEntry(1, 1, -1.0);

        Assert.assertEquals(expectedDifference, difference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(1, 0, 6.0);
        matrix2.setEntry(1, 1, 7.0);

        RealMatrix product = matrix1.multiply(matrix2);
        double[][] expectedProduct = new double[][]{
                {11.0, 17.0},
                {23.0, 31.0}
        };

        Assert.assertArrayEquals(expectedValues(expectedProduct), product.getData(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 4);
        matrix1.multiply(matrix2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryWithTooLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, 3.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(3.0, value, Precision.SAFE_EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.getEntry(1, 1);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, 3.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(3.0, value, Precision.SAFE_EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(1, 1, 3.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, 3.0);
        matrix.addToEntry(0, 0, 2.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(5.0, value, Precision.SAFE_EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.addToEntry(1, 1, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, 3.0);
        matrix.multiplyEntry(0, 0, 2.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(6.0, value, Precision.SAFE_EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.multiplyEntry(1, 1, 2.0);
    }

    private double[][] expectedValues(double[][] values) {
        double[][] result = new double[values.length][];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].clone();
        }
        return result;
    }
}