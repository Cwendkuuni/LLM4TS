import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparseMatrix());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.add(b);

        a.setEntry(1, 1, 2.0);
        a.setEntry(1, 2, 1.0);
        b.setEntry(1, 1, 1.0);
        b.setEntry(1, 2, -1.0);

        assertEquals(3.0, c.getEntry(1, 1), 1e-14);
        assertEquals(0.0, c.getEntry(1, 2), 1e-14);
        assertEquals(0.0, c.getEntry(2, 2), 1e-14);

        c.setEntry(2, 2, 3.0);
        assertEquals(3.0, c.getEntry(2, 2), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 2);
        a.add(b);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.subtract(b);

        a.setEntry(1, 1, 2.0);
        a.setEntry(1, 2, 1.0);
        b.setEntry(1, 1, 1.0);
        b.setEntry(1, 2, -1.0);

        assertEquals(1.0, c.getEntry(1, 1), 1e-14);
        assertEquals(2.0, c.getEntry(1, 2), 1e-14);
        assertEquals(0.0, c.getEntry(2, 2), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 2);
        a.subtract(b);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        RealMatrix c = a.multiply(b);

        a.setEntry(1, 1, 2.0);
        a.setEntry(1, 2, 1.0);
        a.setEntry(2, 1, 1.0);
        a.setEntry(2, 2, 2.0);
        b.setEntry(1, 1, 1.0);
        b.setEntry(1, 2, -1.0);
        b.setEntry(2, 1, 1.0);
        b.setEntry(2, 2, 2.0);

        BlockRealMatrix expected = new BlockRealMatrix(3, 3) {
            {
                setEntry(0, 0, 2.0);
                setEntry(0, 1, -1.0);
                setEntry(0, 2, 0.0);
                setEntry(1, 0, 1.0);
                setEntry(1, 1, 1.0);
                setEntry(1, 2, -2.0);
                setEntry(2, 0, 1.0);
                setEntry(2, 1, 2.0);
                setEntry(2, 2, 4.0);
            }
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected.getEntry(i, j), c.getEntry(i, j), 1e-14);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 2);
        a.multiply(b);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-14);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 100000);
        try {
            matrix.setEntry(0, 50000, Double.POSITIVE_INFINITY);
            fail();
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }
}