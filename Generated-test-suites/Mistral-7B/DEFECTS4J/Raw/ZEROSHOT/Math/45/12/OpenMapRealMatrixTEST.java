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
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
        assertEquals(1.0, copy.getEntry(0, 0), 1e-14);
        assertEquals(2.0, copy.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(2, 2, 4.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);

        assertEquals(1.0, sum.getEntry(0, 0), 1e-14);
        assertEquals(3.0, sum.getEntry(1, 1), 1e-14);
        assertEquals(4.0, sum.getEntry(2, 2), 1e-14);
    }

    @Test(expected = AssertionError.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(2, 2, 2.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);

        assertEquals(0.0, difference.getEntry(1, 1), 1e-14);
        assertEquals(1.0, difference.getEntry(0, 0), 1e-14);
        assertEquals(1.0, difference.getEntry(2, 2), 1e-14);
    }

    @Test(expected = AssertionError.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        RealMatrix matrix2 = new BlockRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 0, 2.0);
        matrix1.setEntry(2, 0, 3.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);

        RealMatrix product = matrix1.multiply(matrix2);

        BlockRealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0, 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 3.0, 1.0 * 3.0 + 2.0 * 3.0 + 3.0 * 3.0},
                {1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0, 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 3.0, 1.0 * 3.0 + 2.0 * 3.0 + 3.0 * 3.0},
                {1.0 * 1.0 + 2.0 * 2.0 + 3.0 * 3.0, 1.0 * 2.0 + 2.0 * 3.0 + 3.0 * 3.0, 1.0 * 3.0 + 2.0 * 3.0 + 3.0 * 3.0}
        });

        assertEquals(expected.getRowDimension(), product.getRowDimension());
        assertEquals(expected.getColumnDimension(), product.getColumnDimension());

        for (int i = 0; i < product.getRowDimension(); i++) {
            for (int j = 0; j < product.getColumnDimension(); j++) {
                assertEquals(expected.getEntry(i, j), product.getEntry(i, j), 1e-14);
            }
        }
    }

    @Test(expected = AssertionError.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        RealMatrix matrix2 = new BlockRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });

        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.addToEntry(3, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);

        matrix.multiplyEntry(3, 3, 2.0);
    }
}