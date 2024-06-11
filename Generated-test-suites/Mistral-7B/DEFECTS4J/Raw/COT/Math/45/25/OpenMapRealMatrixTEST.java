import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        mat1.setEntry(0, 0, 1.0);
        mat1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(3, 3);
        mat2.setEntry(1, 1, 3.0);
        mat2.setEntry(2, 2, 4.0);

        OpenMapRealMatrix result = mat1.add(mat2);
        assertEquals(mat1.getRowDimension(), result.getRowDimension());
        assertEquals(mat1.getColumnDimension(), result.getColumnDimension());
        assertEquals(1.0, result.getEntry(0, 0), 0.001);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
        assertEquals(4.0, result.getEntry(1, 2), 0.001);
        assertEquals(0.0, result.getEntry(2, 2), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        mat1.setEntry(0, 0, 1.0);
        mat1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(2, 3);
        mat2.setEntry(0, 0, 3.0);
        mat2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix result = mat1.add(mat2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        mat1.setEntry(0, 0, 1.0);
        mat1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(3, 3);
        mat2.setEntry(1, 1, 3.0);
        mat2.setEntry(2, 2, 4.0);

        OpenMapRealMatrix result = mat1.subtract(mat2);
        assertEquals(mat1.getRowDimension(), result.getRowDimension());
        assertEquals(mat1.getColumnDimension(), result.getColumnDimension());
        assertEquals(-2.0, result.getEntry(1, 1), 0.001);
        assertEquals(1.0, result.getEntry(1, 2), 0.001);
        assertEquals(0.0, result.getEntry(2, 2), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        mat1.setEntry(0, 0, 1.0);
        mat1.setEntry(1, 1, 2.0);

        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(2, 2);
        mat2.setEntry(0, 0, 3.0);
        mat2.setEntry(0, 1, 4.0);

        OpenMapRealMatrix result = mat1.subtract(mat2);
    }

    @Test
    public void testMultiplicationMatrixWithMatrix() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        mat1.setEntry(0, 0, 1.0);
        mat1.setEntry(1, 0, 2.0);
        mat1.setEntry(0, 1, 3.0);
        mat1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(3, 3);
        mat2.setEntry(0, 0, 5.0);
        mat2.setEntry(0, 1, 6.0);
        mat2.setEntry(1, 0, 7.0);
        mat2.setEntry(1, 1, 8.0);

        RealMatrix result = mat1.multiply(mat2);
        BlockRealMatrix expectedResult = new BlockRealMatrix(3, 3);
        expectedResult.setEntry(0, 0, 15.0);
        expectedResult.setEntry(0, 1, 18.0);
        expectedResult.setEntry(0, 2, 12.0);
        expectedResult.setEntry(1, 0, 21.0);
        expectedResult.setEntry(1, 1, 28.0);
        expectedResult.setEntry(1, 2, 16.0);
        expectedResult.setEntry(2, 0, 12.0);
        expectedResult.setEntry(2, 1, 16.0);
        expectedResult.setEntry(2, 2, 12.0);

        assertEquals(expectedResult, result);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationMatrixWithNonSquareMatrix() {
        OpenMapRealMatrix mat1 = new OpenMapRealMatrix(3, 3);
        mat1.setEntry(0, 0, 1.0);
        mat1.setEntry(1, 0, 2.0);
        mat1.setEntry(0, 1, 3.0);
        mat1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix mat2 = new OpenMapRealMatrix(2, 3);
        mat2.setEntry(0, 0, 5.0);
        mat2.setEntry(0, 1, 6.0);
        mat2.setEntry(1, 0, 7.0);
        mat2.setEntry(1, 2, 8.0);

        OpenMapRealMatrix result = mat1.multiply(mat2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        assertEquals(1.0, mat.getEntry(0, 0), 0.001);
        assertEquals(2.0, mat.getEntry(1, 1), 0.001);
        assertEquals(3.0, mat.getEntry(2, 2), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.setEntry(0, 0, 4.0);
        assertEquals(4.0, mat.getEntry(0, 0), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.setEntry(3, 3, 4.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.addToEntry(0, 0, 2.0);
        assertEquals(3.0, mat.getEntry(0, 0), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.addToEntry(3, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, mat.getEntry(0, 0), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.setEntry(1, 1, 2.0);
        mat.setEntry(2, 2, 3.0);

        mat.multiplyEntry(3, 3, 2.0);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, Long.MAX_VALUE);
    }

    @Test
    public void testEmptyMatrix() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        assertTrue(mat.isEmpty());
    }

    @Test
    public void testNonEmptyMatrix() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        assertFalse(mat.isEmpty());
    }

    @Test
    public void testEmptyMatrixAfterClear() {
        OpenMapRealMatrix mat = new OpenMapRealMatrix(3, 3);
        mat.setEntry(0, 0, 1.0);
        mat.clear();
        assertTrue(mat.isEmpty());
    }
}