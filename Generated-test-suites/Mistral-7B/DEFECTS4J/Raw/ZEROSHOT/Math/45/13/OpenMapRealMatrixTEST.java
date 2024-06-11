import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assertions.assertEquals(3, matrix.getRowDimension());
        Assertions.assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        Assertions.assertNotSame(original, copy);
        Assertions.assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 2, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        Assertions.assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        Assertions.assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());

        Assertions.assertEquals(4.0, result.getEntry(0, 0), 1e-14);
        Assertions.assertEquals(3.0, result.getEntry(0, 1), 1e-14);
        Assertions.assertEquals(4.0, result.getEntry(1, 1), 1e-14);
        Assertions.assertEquals(3.0, result.getEntry(1, 2), 1e-14);
        Assertions.assertEquals(3.0, result.getEntry(1, 2), 1e-14);
        Assertions.assertEquals(3.0, result.getEntry(2, 2), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 1.0);
        matrix2.setEntry(1, 2, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        Assertions.assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        Assertions.assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());

        Assertions.assertEquals(0.0, result.getEntry(0, 0), 1e-14);
        Assertions.assertEquals(0.0, result.getEntry(0, 1), 1e-14);
        Assertions.assertEquals(2.0, result.getEntry(1, 1), 1e-14);
        Assertions.assertEquals(0.0, result.getEntry(1, 2), 1e-14);
        Assertions.assertEquals(0.0, result.getEntry(2, 2), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);
        matrix1.setEntry(2, 0, 7.0);
        matrix1.setEntry(2, 1, 8.0);
        matrix1.setEntry(2, 2, 9.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);
        matrix2.setEntry(1, 2, 6.0);
        matrix2.setEntry(2, 0, 7.0);
        matrix2.setEntry(2, 1, 8.0);
        matrix2.setEntry(2, 2, 9.0);

        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        Assertions.assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        Assertions.assertEquals(matrix2.getColumnDimension(), result.getColumnDimension());

        BlockRealMatrix expected = new BlockRealMatrix(3, 3);
        expected.setEntry(0, 0, 15.0);
        expected.setEntry(0, 1, 21.0);
        expected.setEntry(0, 2, 27.0);
        expected.setEntry(1, 0, 33.0);
        expected.setEntry(1, 1, 55.0);
        expected.setEntry(1, 2, 72.0);
        expected.setEntry(2, 0, 49.0);
        expected.setEntry(2, 1, 84.0);
        expected.setEntry(2, 2, 105.0);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);

        Assertions.assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 0, 2.0);

        Assertions.assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);

        Assertions.assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);

        Assertions.assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testOpenMapRealMatrixKeyComputation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(1, 2);

        Assertions.assertEquals(7, key);
    }
}