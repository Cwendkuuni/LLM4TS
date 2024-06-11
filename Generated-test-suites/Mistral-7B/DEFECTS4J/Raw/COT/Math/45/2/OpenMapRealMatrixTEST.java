import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    @Test
    public void testOpenMapRealMatrixCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertNotEquals(original.entries, copy.entries);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = left.add(right);
        assertEquals(left.getRowDimension(), result.getRowDimension());
        assertEquals(left.getColumnDimension(), result.getColumnDimension());
        OpenIntToDoubleHashMap leftEntries = left.entries;
        OpenIntToDoubleHashMap rightEntries = right.entries;
        OpenIntToDoubleHashMap resultEntries = result.entries;

        for (int i = 0; i < left.getRowDimension(); i++) {
            for (int j = 0; j < left.getColumnDimension(); j++) {
                double leftValue = leftEntries.get(left.computeKey(i, j));
                double rightValue = rightEntries.get(right.computeKey(i, j));
                double sum = leftValue + rightValue;
                assertEquals(sum, resultEntries.get(result.computeKey(i, j)));
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixAdditionDimensionMismatch() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 3).add(right);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = left.subtract(right);
        assertEquals(left.getRowDimension(), result.getRowDimension());
        assertEquals(left.getColumnDimension(), result.getColumnDimension());
        OpenIntToDoubleHashMap leftEntries = left.entries;
        OpenIntToDoubleHashMap rightEntries = right.entries;
        OpenIntToDoubleHashMap resultEntries = result.entries;

        for (int i = 0; i < left.getRowDimension(); i++) {
            for (int j = 0; j < left.getColumnDimension(); j++) {
                double leftValue = leftEntries.get(left.computeKey(i, j));
                double rightValue = rightEntries.get(right.computeKey(i, j));
                double difference = leftValue - rightValue;
                assertEquals(difference, resultEntries.get(result.computeKey(i, j)));
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixSubtractionDimensionMismatch() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix result = left.subtract(right);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 2);
        RealMatrix result = left.multiply(right);
        assertEquals(left.getRowDimension(), result.getRowDimension());
        assertEquals(right.getColumnDimension(), result.getColumnDimension());

        for (int i = 0; i < left.getRowDimension(); i++) {
            for (int j = 0; j < right.getColumnDimension(); j++) {
                double sum = 0.0;
                for (int k = 0; k < left.getColumnDimension(); k++) {
                    sum += left.getEntry(i, k) * right.getEntry(k, j);
                }
                assertEquals(sum, result.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrixMultiplicationDimensionMismatch() {
        OpenMapRealMatrix left = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix right = new OpenMapRealMatrix(3, 3);
        left.multiply(right);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-10);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfRangeException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.getEntry(2, 2);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfRangeException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(2, 2, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfRangeException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(2, 2, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-10);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfRangeException.class)
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(2, 2, 2.0);
    }
}