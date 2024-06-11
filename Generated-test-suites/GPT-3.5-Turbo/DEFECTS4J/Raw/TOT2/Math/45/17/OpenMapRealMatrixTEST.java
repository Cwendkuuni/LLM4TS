import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = new OpenMapRealMatrix(matrix);
        assertEquals(matrix.getEntry(0, 0), copy.getEntry(0, 0), 0.0);
        assertEquals(matrix.getEntry(1, 1), copy.getEntry(1, 1), 0.0);
        assertEquals(matrix.getEntry(2, 2), copy.getEntry(2, 2), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix anotherMatrix = new OpenMapRealMatrix(3, 3);
        anotherMatrix.setEntry(0, 0, 2.0);
        anotherMatrix.setEntry(1, 1, 3.0);
        anotherMatrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix result = matrix.add(anotherMatrix);

        assertEquals(3.0, result.getEntry(0, 0), 0.0);
        assertEquals(5.0, result.getEntry(1, 1), 0.0);
        assertEquals(7.0, result.getEntry(2, 2), 0.0);
    }

    // Add more test cases for other methods

}