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
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeDimension() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixZeroDimension() {
        new OpenMapRealMatrix(0, 3);
    }

    @Test
    public void testOpenMapRealMatrixCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertArrayEquals(original.getEntryData(), copy.getEntryData(), 0.0);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{0.5, 0.5}, {1.5, 1.5}});
        OpenMapRealMatrix c = a.add(b);
        assertArrayEquals(new double[][]{{1.5, 2.5}, {4.5, 5.5}}, c.getEntryData(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAdditionDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{ {1.0, 2.0}, {3.0}, {4.0} });
        a.add(b);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{0.5, 0.5}, {1.5, 1.5}});
        OpenMapRealMatrix c = a.subtract(b);
        assertArrayEquals(new double[][]{{0.5, 1.5}, {1.5, 2.5}}, c.getEntryData(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractionDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{ {1.0, 2.0}, {3.0}, {4.0} });
        a.subtract(b);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}});
        RealMatrix c = a.multiply(b);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{11.0, 14.0}, {19.0, 22.0}});
        assertEquals(expected, c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplicationIncompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new double[][]{{5.0, 6.0}});
        a.multiply(b);
    }

    @Test
    public void testOpenMapRealMatrixAccessEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(-1, -1, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixOutOfBoundsIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(2, 3, 1.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixMaxCapacity() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            matrix.setEntry(0, 0, i);
        }
    }
}