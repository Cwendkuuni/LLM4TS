import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertNotNull(counter);
            assertEquals(2, counter.getDimension());
            assertArrayEquals(size, counter.getSizes());
            assertEquals(2 * 3, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositiveException() throws NotStrictlyPositiveException {
        int[] size = {0, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            Iterator<Integer> iterator = counter.iterator();
            assertNotNull(iterator);
            assertTrue(iterator.hasNext());
            for (int i = 0; i < counter.getSize(); i++) {
                assertTrue(iterator.hasNext());
                assertEquals(i, iterator.next().intValue());
            }
            assertFalse(iterator.hasNext());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test
    public void testGetDimension() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(2, counter.getDimension());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] indices = counter.getCounts(0);
            assertArrayEquals(new int[]{0, 0}, indices);
            indices = counter.getCounts(3);
            assertArrayEquals(new int[]{1, 0}, indices);
            indices = counter.getCounts(5);
            assertArrayEquals(new int[]{0, 2}, indices);
            indices = counter.getCounts(4);
            assertArrayEquals(new int[]{0, 1}, indices);
        } catch (NotStrictlyPositiveException | OutOfRangeException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeException() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = counter.getCounts(10);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeExceptionNegativeIndex() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = counter.getCounts(-1);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(0, 0);
            assertEquals(0, count);
            count = counter.getCount(1, 1);
            assertEquals(4, count);
            count = counter.getCount(1, 2);
            assertEquals(5, count);
        } catch (NotStrictlyPositiveException | OutOfRangeException | DimensionMismatchException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchException() throws DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchExceptionDifferentDimension() throws DimensionMismatchException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeException() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeExceptionNegativeIndex() throws OutOfRangeException {
        int[] size = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int count = counter.getCount(0, -1);
    }

    @Test
    public void testGetSize() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(2 * 3, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test
    public void testGetSizes() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertArrayEquals(size, counter.getSizes());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test
    public void testToString() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals("[0][0]", counter.toString());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test
    public void testIterator_GetCount() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            assertEquals(-1, iterator.getCount());
            for (int i = 0; i < counter.getSize(); i++) {
                assertTrue(iterator.hasNext());
                int count = iterator.next();
                assertEquals(i, count);
                assertEquals(i, iterator.getCount());
                int[] indices = iterator.getCounts();
                for (int j = 0; j < counter.getDimension(); j++) {
                    assertEquals(indices[j], iterator.getCount(j));
                }
            }
            assertFalse(iterator.hasNext());
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            iterator.remove();
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown");
        }
    }

}