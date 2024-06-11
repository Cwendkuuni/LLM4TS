import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositiveException() throws NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(-1, 2, 3);
    }

    @Test
    public void testConstructor_GetDimension() throws NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(2, mc.getDimension());
    }

    @Test
    public void testConstructor_GetSizes() throws NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{2, 3}, mc.getSizes());
    }

    @Test
    public void testConstructor_GetSize() throws NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(6, mc.getSize());
    }

    @Test
    public void testGetCounts_OutOfRangeException() throws NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        try {
            mc.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
        try {
            mc.getCounts(6);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
    }

    @Test
    public void testGetCounts() throws NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertArrayEquals(new int[]{0, 0}, mc.getCounts(0));
        assertArrayEquals(new int[]{1, 0}, mc.getCounts(1));
        assertArrayEquals(new int[]{0, 1}, mc.getCounts(2));
        assertArrayEquals(new int[]{1, 1}, mc.getCounts(3));
        assertArrayEquals(new int[]{0, 2}, mc.getCounts(4));
        assertArrayEquals(new int[]{1, 2}, mc.getCounts(5));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchException() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        mc.getCount(1, 2, 3);
    }

    @Test
    public void testGetCount_OutOfRangeException() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        try {
            mc.getCount(-1, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
        try {
            mc.getCount(2, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
        try {
            mc.getCount(0, -1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
        try {
            mc.getCount(0, 3);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // success
        }
    }

    @Test
    public void testGetCount() throws DimensionMismatchException, OutOfRangeException, NotStrictlyPositiveException {
        MultidimensionalCounter mc = new MultidimensionalCounter(2, 3);
        assertEquals(0, mc.getCount(0, 0));
        assertEquals(1, mc.getCount(1, 0));
        assertEquals(2, mc.getCount(0, 1));
        assertEquals(3, mc.getCount(1, 1));
        assertEquals(4, mc.getCount(0, 2));
        assertEquals(5, mc.getCount(1, 2));
    }

}