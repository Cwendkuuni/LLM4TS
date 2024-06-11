import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeSize() {
        new OpenMapRealMatrix(-1, -1);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 3.0);
        expected.setEntry(0, 1, 3.0);
        expected.setEntry(1, 0, 4.0);
        expected.setEntry(1, 1, 6.0);

        assertEquals(expected, result);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 1.0);
        expected.setEntry(1, 0, 2.0);
        expected.setEntry(1, 1, 2.0);

        assertEquals(expected, result);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        matrix1.setEntry(2, 0, 5.0);
        matrix1.setEntry(2, 1, 6.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 6.0);

        RealMatrix result = matrix1.multiply(matrix2);
        BlockRealMatrix expected = new BlockRealMatrix(3, 3);
        expected.setEntry(0, 0, 15.0);
        expected.setEntry(0, 1, 21.0);
        expected.setEntry(0, 2, 27.0);
        expected.setEntry(1, 0, 18.0);
        expected.setEntry(1, 1, 27.0);
        expected.setEntry(1, 2, 36.0);
        expected.setEntry(2, 0, 21.0);
        expected.setEntry(2, 1, 32.0);
        expected.setEntry(2, 2, 42.0);

        assertEquals(expected, result);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 1);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
        assertEquals(4.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(2, 2);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(0, 0, 5.0);
        matrix.setEntry(1, 1, 6.0);

        assertEquals(5.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(6.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 6.0);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 2.0);
        matrix.addToEntry(1, 1, 3.0);

        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(2, 2, 6.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);

        assertEquals(2.0 * 1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0 * 2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0 * 3.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(3.0 * 4.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(2, 2, 6.0);
    }
}