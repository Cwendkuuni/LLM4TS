import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3,3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
    }

    @Test
    public void testGetEntry() {
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 1, 4.0);
        assertEquals(4.0, matrix.getEntry(0, 1), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(3,3);
        matrixToAdd.setEntry(0,0, 1.0);
        matrixToAdd.setEntry(1,1, 2.0);
        OpenMapRealMatrix result = matrix.add(matrixToAdd);
        assertEquals(2.0, result.getEntry(0, 0), 0.0);
        assertEquals(4.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(3,3);
        matrixToSubtract.setEntry(0,0, 0.5);
        matrixToSubtract.setEntry(1,1, 1.0);
        OpenMapRealMatrix result = matrix.subtract(matrixToSubtract);
        assertEquals(0.5, result.getEntry(0, 0), 0.0);
        assertEquals(1.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(3,3);
        matrixToMultiply.setEntry(0,0, 1.0);
        matrixToMultiply.setEntry(1,1, 2.0);
        matrixToMultiply.setEntry(2,2, 3.0);
        OpenMapRealMatrix result = matrix.multiply(matrixToMultiply);
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(4.0, result.getEntry(1, 1), 0.0);
        assertEquals(9.0, result.getEntry(2, 2), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testGetEntryWithInvalidIndex() {
        matrix.getEntry(3, 3);
    }
}