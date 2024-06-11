import org.apache.commons.math.linear.OpenMapRealMatrix;
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
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);
        m.setEntry(0, 0, 10.0);
        m.setEntry(1, 1, 20.0);
        m.setEntry(2, 2, 30.0);
        OpenMapRealMatrix result = matrix.add(m);
        assertEquals(11.0, result.getEntry(0, 0), 0.001);
        assertEquals(2.0, result.getEntry(0, 1), 0.001);
        assertEquals(3.0, result.getEntry(1, 1), 0.001);
        assertEquals(4.0, result.getEntry(2, 2), 0.001);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);
        m.setEntry(0, 0, 10.0);
        m.setEntry(1, 1, 20.0);
        m.setEntry(2, 2, 30.0);
        OpenMapRealMatrix result = matrix.subtract(m);
        assertEquals(-9.0, result.getEntry(0, 0), 0.001);
        assertEquals(2.0, result.getEntry(0, 1), 0.001);
        assertEquals(-3.0, result.getEntry(1, 1), 0.001);
        assertEquals(-26.0, result.getEntry(2, 2), 0.001);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);
        m.setEntry(0, 0, 10.0);
        m.setEntry(1, 1, 20.0);
        m.setEntry(2, 2, 30.0);
        OpenMapRealMatrix result = matrix.multiply(m);
        assertEquals(10.0, result.getEntry(0, 0), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
        assertEquals(0.0, result.getEntry(1, 0), 0.001);
        assertEquals(60.0, result.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(4.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(4.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }
}