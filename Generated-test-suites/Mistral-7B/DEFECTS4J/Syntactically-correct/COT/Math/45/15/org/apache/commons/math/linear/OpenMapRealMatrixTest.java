import static org.junit.Assert.*;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;

public class OpenMapRealMatrixTest {

    @org.junit.Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEmpty(matrix);
    }

    @org.junit.Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix, copiedMatrix);
    }

    @org.junit.Test
    public void testAdd() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = matrixA.add(matrixB);
        verifyAddition(matrixA, matrixB, sum);
    }

    private void verifyAddition(OpenMapRealMatrix matrixA, OpenMapRealMatrix matrixB, OpenMapRealMatrix sum) {
        assertEquals(matrixA.getRowDimension(), sum.getRowDimension());
        assertEquals(matrixA.getColumnDimension(), sum.getColumnDimension());
        for (int i = 0; i < matrixA.getRowDimension(); i++) {
            for (int j = 0; j < matrixA.getColumnDimension(); j++) {
                double expectedValue = matrixA.getEntry(i, j) + matrixB.getEntry(i, j);
                assertEquals(expectedValue, sum.getEntry(i, j), 1e-14);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddWithIncompatibleSize() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = matrixA.add(matrixB);
    }

    @org.junit.Test
    public void testSubtract() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = matrixA.subtract(matrixB);
        verifySubtraction(matrixA, matrixB, difference);
    }

    private void verifySubtraction(OpenMapRealMatrix matrixA, OpenMapRealMatrix matrixB, OpenMapRealMatrix difference) {
        assertEquals(matrixA.getRowDimension(), difference.getRowDimension());
        assertEquals(matrixA.getColumnDimension(), difference.getColumnDimension());
        for (int i = 0; i < matrixA.getRowDimension(); i++) {
            for (int j = 0; j < matrixA.getColumnDimension(); j++) {
                double expectedValue = matrixA.getEntry(i, j) - matrixB.getEntry(i, j);
                assertEquals(expectedValue, difference.getEntry(i, j), 1e-14);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubtractWithIncompatibleSize() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = matrixA.subtract(matrixB);
    }

    @org.junit.Test
    public void testMultiply() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        RealMatrix product = matrixA.multiply(matrixB);
        verifyMultiplication(matrixA, matrixB, product);
    }

    private void verifyMultiplication(OpenMapRealMatrix matrixA, OpenMapRealMatrix matrixB, RealMatrix product) {
        assertEquals(matrixA.getRowDimension(), product.getRowDimension());
        assertEquals(matrixB.getColumnDimension(), product.getColumnDimension());
        for (int i = 0; i < matrixA.getRowDimension(); i++) {
            for (int j = 0; j < matrixB.getColumnDimension(); j++) {
                double expectedValue = 0.0;
                for (int k = 0; k < matrixB.getRowDimension(); k++) {
                    expectedValue += matrixA.getEntry(i, k) * matrixB.getEntry(k, j);
                }
                assertEquals(expectedValue, product.getEntry(i, j), 1e-14);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMultiplyWithIncompatibleSize() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix product = matrixA.multiply(matrixB);
    }

    @org.junit.Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 1e-14);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 3);
    }

    @org.junit.Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 1e-14);
    }

    @org.junit.Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
        matrix.setEntry(1, 1, 2.0);
        matrix.addToEntry(1, 1, 3.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 1e-14);
        matrix.setEntry(2, 2, 3.0);
        matrix.addToEntry(2, 2, 4.0);
        assertEquals(7.0, matrix.getEntry(2, 2), 1e-14);
    }

    @org.junit.Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);
        assertEquals(6.0, matrix.getEntry(1, 1), 1e-14);
        matrix.setEntry(2, 2, 3.0);
        matrix.multiplyEntry(2, 2, 4.0);
        assertEquals(12.0, matrix.getEntry(2, 2), 1e-14);
    }

    @org.junit.Test
    public void testEmpty() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEmpty(matrix);

        matrix.setEntry(0, 0, 1.0);
        assertFalse(matrix.isEmpty());
    }

    private void assertEmpty(OpenMapRealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(0.0, matrix.getEntry(i, j), 1e-14);
            }
        }
    }

    @org.junit.Test
    public void testNegativeEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, -1.0);
        assertEquals(-1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(Integer.MAX_VALUE, Integer.MAX_VALUE, 1.0);
    }

}