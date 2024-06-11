import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        assertEquals(rowDimension, matrix.getRowDimension());
        assertEquals(columnDimension, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    @Test
    public void testCopy() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix original = new OpenMapRealMatrix(rowDimension, columnDimension);
        original.setEntry(0, 0, 1.0);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAddMatrix() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rowDimension, columnDimension);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);

        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(1.0, result.getEntry(0, 0), 0.001);
        assertEquals(4.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(1, 0), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAddMatrixDimensionMismatch() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rowDimension - 1, columnDimension + 1);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.add(matrix2);
    }

    @Test
    public void testMultiplyMatrix() {
        final int rowDimensionA = 3;
        final int columnDimensionA = 2;
        final int columnDimensionB = 3;
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(rowDimensionA, columnDimensionA);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(columnDimensionA, columnDimensionB);

        matrixA.setEntry(0, 0, 1.0);
        matrixA.setEntry(1, 0, 2.0);
        matrixA.setEntry(2, 0, 3.0);

        matrixB.setEntry(0, 0, 4.0);
        matrixB.setEntry(1, 0, 5.0);
        matrixB.setEntry(2, 0, 6.0);
        matrixB.setEntry(0, 1, 7.0);
        matrixB.setEntry(1, 1, 8.0);
        matrixB.setEntry(2, 1, 9.0);

        RealMatrix result = matrixA.multiply(matrixB);
        BlockRealMatrix expected = new BlockRealMatrix(rowDimensionA, columnDimensionB);
        expected.setEntry(0, 0, 4.0 * 1.0 + 5.0 * 2.0 + 6.0 * 3.0);
        expected.setEntry(0, 1, 4.0 * 7.0 + 5.0 * 8.0 + 6.0 * 9.0);
        expected.setEntry(1, 0, 5.0 * 1.0 + 8.0 * 2.0 + 9.0 * 3.0);
        expected.setEntry(1, 1, 5.0 * 7.0 + 8.0 * 8.0 + 9.0 * 9.0);
        expected.setEntry(2, 0, 6.0 * 1.0 + 8.0 * 2.0 + 9.0 * 3.0);
        expected.setEntry(2, 1, 6.0 * 7.0 + 8.0 * 8.0 + 9.0 * 9.0);

        assertEquals(expected, result);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplyMatrixDimensionMismatch() {
        final int rowDimensionA = 3;
        final int columnDimensionA = 2;
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(rowDimensionA, columnDimensionA);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(columnDimensionA - 1, columnDimensionA + 1);
        matrixA.multiply(matrixB);
    }

    @Test
    public void testGetEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(0.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(0.0, matrix.getEntry(2, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(2, 1), 0.001);
    }

    @Test
    public void testSetEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(0.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(0.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testAddToEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        matrix.addToEntry(1, 1, 3.0);
        assertEquals(0.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testMultiplyEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
        matrix.multiplyEntry(1, 1, 3.0);
        assertEquals(0.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0 * 3.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test
    public void testRemoveEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        matrix.removeEntry(0, 0);
        assertEquals(0.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testSetEntryTooLarge() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 1, Long.MAX_VALUE);
    }
}