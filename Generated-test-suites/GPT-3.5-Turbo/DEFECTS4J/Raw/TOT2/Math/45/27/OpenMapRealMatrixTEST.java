import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 1, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 2, 3.0);
        assertEquals(3.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix sum = matrix.add(matrix2);
        assertEquals(2.0, sum.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(2, 2, 8.0);
        OpenMapRealMatrix result = matrix.subtract(matrix2);
        assertEquals(-8.0, result.getEntry(2, 2), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddDifferentDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix.add(matrix2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSubtractDifferentDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix2.setEntry(0, 0, 2.0);
        RealMatrix result = matrix.multiply(matrix2);
        assertEquals(2.0, result.getEntry(0, 0), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyIncompatibleDimensions() {
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix.multiply(matrix2);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
        assertEquals(matrix.getEntry(1, 2), copy.getEntry(1, 2), 0.0);
    }

}