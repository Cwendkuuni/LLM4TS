import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix, copiedMatrix);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix();
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 3);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(3, newMatrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetColumnDimensionNegative() {
        new OpenMapRealMatrix(2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRowDimensionNegative() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        leftMatrix.add(rightMatrix);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));

        assertEquals(expectedResult, leftMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIncompatibleMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        RealMatrix matrix2 = new BlockRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} });
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2)));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        leftMatrix.subtract(rightMatrix);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2)));

        assertEquals(expectedResult, leftMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIncompatibleMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        RealMatrix matrix2 = new BlockRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} });
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        RealMatrix resultMatrix = leftMatrix.multiply(rightMatrix);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(leftMatrix.multiply(rightMatrix));

        assertEquals(expectedResult, resultMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyIncompatibleMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        matrix1.multiply(matrix2);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.multiplyEntry(0, 0, 2);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).multiplyEntry(0, 0, 2));

        assertEquals(expectedResult, matrix);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.addToEntry(0, 0, 2);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).addToEntry(0, 0, 2));

        assertEquals(expectedResult, matrix);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 2);

        OpenMapRealMatrix expectedResult = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).setEntry(0, 0, 2));

        assertEquals(expectedResult, matrix);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(10, 10, 2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 2);

        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        int key = matrix.computeKey(0, 0);

        int expectedKey = 0;

        assertEquals(expectedKey, key);
    }

}