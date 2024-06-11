import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix copied = new OpenMapRealMatrix(original);
        assertEquals(original, copied);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2))).add(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)).add(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix c = a.add(b);
        assertEquals(a, c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.add(b);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(4.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        matrix.getEntry(2, 2);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(3.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(4.0, matrix.getEntry(1, 1), 1e-12);

        matrix.setEntry(0, 0, 5.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 1e-12);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(2, 2, 1.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        a.setEntry(0, 0, 1.0);
        a.setEntry(0, 1, 2.0);
        a.setEntry(1, 0, 3.0);
        a.setEntry(1, 1, 4.0);

        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
        b.setEntry(0, 0, 5.0);
        b.setEntry(0, 1, 6.0);
        b.setEntry(1, 0, 7.0);
        b.setEntry(1, 1, 8.0);

        OpenMapRealMatrix c = a.multiply(b);

        RealMatrix expected = new BlockRealMatrix(new double[][] {
                {15.0, 18.0},
                {23.0, 28.0}
        });

        assertEquals(expected, c);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        RealMatrix result = a.multiply(b);
    }

    @Test
    public void testTranspose() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix b = a.transpose();
        OpenMapRealMatrix c = new OpenMapRealMatrix(a);
        for (int i = 0; i < c.getRowDimension(); i++) {
            for (int j = 0; j < c.getColumnDimension(); j++) {
                assertEquals(c.getEntry(j, i), b.getEntry(i, j), 1e-12);
            }
        }
    }

    @Test
    public void testMultiplyAndTranspose() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix b = a.transpose();
        OpenMapRealMatrix c = a.multiply(b);

        RealMatrix expected = new BlockRealMatrix(new double[][] {
                {14.0, 14.0},
                {14.0, 14.0}
        });

        assertEquals(expected, c);
    }

    @Test
    public void testAddIdentity() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix identity = new OpenMapRealMatrix(2, 2).identity();
        OpenMapRealMatrix b = a.add(identity);

        RealMatrix expected = new BlockRealMatrix(new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        });

        assertEquals(expected, b);
    }

    @Test
    public void testMultiplyIdentity() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix identity = new OpenMapRealMatrix(2, 2).identity();
        OpenMapRealMatrix c = a.multiply(identity);

        RealMatrix expected = a;

        assertEquals(expected, c);
    }

    @Test
    public void testMultiplyVector() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2));
        RealVector b = new ArrayRealVector(new double[] {1.0, 2.0});
        RealVector c = a.multiply(b);

        RealVector expected = new ArrayRealVector(new double[] {11.0, 16.0});

        assertEquals(expected, c);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2))).subtract(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix c = a.subtract(b);
        assertEquals(new OpenMapRealMatrix(2, 2), c);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
        a.subtract(b);
    }

    @Test
    public void testAddScalar() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix b = a.add(new OpenMapRealMatrix(2, 2).multiplyScalar(2.0));
        OpenMapRealMatrix c = new OpenMapRealMatrix(a);
        for (int i = 0; i < c.getRowDimension(); i++) {
            for (int j = 0; j < c.getColumnDimension(); j++) {
                assertEquals(c.getEntry(i, j) * 2.0, b.getEntry(i, j), 1e-12);
            }
        }
    }

    @Test
    public void testSubtractScalar() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix b = a.subtract(new OpenMapRealMatrix(2, 2).multiplyScalar(2.0));
        OpenMapRealMatrix c = new OpenMapRealMatrix(a);
        for (int i = 0; i < c.getRowDimension(); i++) {
            for (int j = 0; j < c.getColumnDimension(); j++) {
                assertEquals(c.getEntry(i, j) - 2.0, b.getEntry(i, j), 1e-12);
            }
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testToString() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        String string = a.toString();
        assertTrue(string.contains("1.0"));
        assertTrue(string.contains("2.0"));
        assertTrue(string.contains("3.0"));
        assertTrue(string.contains("4.0"));
    }
}