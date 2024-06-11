import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.entries.isEmpty());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        assertEquals(originalMatrix.entries.size(), copiedMatrix.entries.size());
    }

    @Test
    public void testCopyMethod() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        assertEquals(originalMatrix.entries.size(), copiedMatrix.entries.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithInvalidRows() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        OpenMapRealMatrix invalidMatrix = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copiedInvalidMatrix = originalMatrix.copy(invalidMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithInvalidColumns() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        OpenMapRealMatrix invalidMatrix = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix copiedInvalidMatrix = originalMatrix.copy(invalidMatrix);
    }

    @Test
    public void testAdditionCompatible() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertTrue(result.getRowDimension() == matrix1.getRowDimension());
        assertTrue(result.getColumnDimension() == matrix1.getColumnDimension());
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionIncompatible() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testAddMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                assertEquals(matrix1.getEntry(i, j) + matrix2.getEntry(i, j), result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testSubtractCompatible() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertTrue(result.getRowDimension() == matrix1.getRowDimension());
        assertTrue(result.getColumnDimension() == matrix1.getColumnDimension());
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractIncompatible() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        RealMatrix matrix2 = new AbstractRealMatrix(3, 3) {
            @Override
            public int getRowDimension() {
                return 2;
            }

            @Override
            public int getColumnDimension() {
                return 3;
            }

            @Override
            public double getEntry(int row, int column) {
                return 0.0;
            }
        };
        matrix1.subtract(matrix2);
    }

    @Test
    public void testSubtractMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                assertEquals(matrix1.getEntry(i, j) - matrix2.getEntry(i, j), result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testMultiplicationCompatible() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        RealMatrix result = matrix1.multiply(matrix2);
        assertTrue(result.getRowDimension() == matrix1.getRowDimension());
        assertTrue(result.getColumnDimension() == matrix2.getColumnDimension());
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationIncompatibleRow() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.multiply(matrix2);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationIncompatibleColumn() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testMultiplyMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), result.getColumnDimension());
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                double sum = 0.0;
                for (int k = 0; k < matrix1.getColumnDimension(); k++) {
                    sum += matrix1.getEntry(i, k) * matrix2.getEntry(k, j);
                }
                assertEquals(sum, result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBoundsRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(4, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBoundsColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(1, 4);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBoundsRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(4, 1, 5.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBoundsColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 4, 5.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBoundsRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(4, 1, 5.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBoundsColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(1, 4, 5.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBoundsRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(4, 1, 5.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBoundsColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(1, 4, 5.0);
    }
}