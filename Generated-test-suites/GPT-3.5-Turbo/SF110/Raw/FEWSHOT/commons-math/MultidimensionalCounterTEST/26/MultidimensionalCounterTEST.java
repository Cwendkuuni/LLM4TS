
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(3, counter.getDimension());
            assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
            assertEquals(24, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testConstructorWithNegativeSize() {
        int[] size = {2, -3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            fail("Exception should be thrown");
        } catch (NotStrictlyPositiveException e) {
            //pass
        }
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            assertTrue(iterator.hasNext());
            assertEquals(0, (int) iterator.next());
            assertEquals(1, (int) iterator.next());
            assertEquals(2, (int) iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(3, (int) iterator.next());
            assertEquals(4, (int) iterator.next());
            assertEquals(5, (int) iterator.next());
            assertFalse(iterator.hasNext());
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIteratorWithZeroSize() {
        int[] size = {2, 0};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            assertFalse(iterator.hasNext());
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIteratorWithNegativeSize() {
        int[] size = {2, -3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            fail("Exception should be thrown");
        } catch (NotStrictlyPositiveException e) {
            //pass
        }
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
            assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
            assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
            assertArrayEquals(new int[]{0, 0, 3}, counter.getCounts(3));
            assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(4));
            assertArrayEquals(new int[]{1, 0, 1}, counter.getCounts(5));
            assertArrayEquals(new int[]{1, 0, 2}, counter.getCounts(6));
            assertArrayEquals(new int[]{1, 0, 3}, counter.getCounts(7));
            assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(8));
            assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(9));
            assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(10));
            assertArrayEquals(new int[]{1, 1, 3}, counter.getCounts(11));
            assertArrayEquals(new int[]{0, 2, 0}, counter.getCounts(12));
            assertArrayEquals(new int[]{0, 2, 1}, counter.getCounts(13));
            assertArrayEquals(new int[]{0, 2, 2}, counter.getCounts(14));
            assertArrayEquals(new int[]{0, 2, 3}, counter.getCounts(15));
            assertArrayEquals(new int[]{1, 2, 0}, counter.getCounts(16));
            assertArrayEquals(new int[]{1, 2, 1}, counter.getCounts(17));
            assertArrayEquals(new int[]{1, 2, 2}, counter.getCounts(18));
            assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(19));
        } catch (NotStrictlyPositiveException | OutOfRangeException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(0, counter.getCount(0, 0, 0));
            assertEquals(1, counter.getCount(0, 0, 1));
            assertEquals(2, counter.getCount(0, 0, 2));
            assertEquals(3, counter.getCount(0, 0, 3));
            assertEquals(4, counter.getCount(1, 0, 0));
            assertEquals(5, counter.getCount(1, 0, 1));
            assertEquals(6, counter.getCount(1, 0, 2));
            assertEquals(7, counter.getCount(1, 0, 3));
            assertEquals(8, counter.getCount(1, 1, 0));
            assertEquals(9, counter.getCount(1, 1, 1));
            assertEquals(10, counter.getCount(1, 1, 2));
            assertEquals(11, counter.getCount(1, 1, 3));
            assertEquals(12, counter.getCount(0, 2, 0));
            assertEquals(13, counter.getCount(0, 2, 1));
            assertEquals(14, counter.getCount(0, 2, 2));
            assertEquals(15, counter.getCount(0, 2, 3));
            assertEquals(16, counter.getCount(1, 2, 0));
            assertEquals(17, counter.getCount(1, 2, 1));
            assertEquals(18, counter.getCount(1, 2, 2));
            assertEquals(19, counter.getCount(1, 2, 3));
        } catch (NotStrictlyPositiveException | OutOfRangeException | DimensionMismatchException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetCountWithOutOfRangeIndex() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            counter.getCount(2, 0, 0);
            fail("Exception should be thrown");
        } catch (DimensionMismatchException | OutOfRangeException e) {
            //pass
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetCountWithDimensionMismatch() {
        int[] size = {2, 3};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            counter.getCount(0, 0, 0);
            fail("Exception should be thrown");
        } catch (OutOfRangeException | DimensionMismatchException e) {
            //pass
        } catch (NotStrictlyPositiveException e) {
            fail("Exception should not be thrown");
        }
    }
}
