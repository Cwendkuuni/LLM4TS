import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        assertEquals(6, counter.getDimension());
        assertEquals(3 * 2 * 1, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        new MultidimensionalCounter(new int[]{-1, 2, 1});
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        int[] counts = counter.getCounts(12);
        assertEquals(3, counts.length);
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        counter.getCounts(100);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 1, 0));
        assertEquals(3, counter.getCount(1, 0, 0));
        assertEquals(4, counter.getCount(1, 0, 1));
        assertEquals(5, counter.getCount(1, 1, 0));
        assertEquals(6, counter.getCount(1, 1, 1));
        assertEquals(7, counter.getCount(1, 2, 0));
        assertEquals(8, counter.getCount(2, 0, 0));
        assertEquals(9, counter.getCount(2, 0, 1));
        assertEquals(10, counter.getCount(2, 1, 0));
        assertEquals(11, counter.getCount(2, 1, 1));
        assertEquals(12, counter.getCount(2, 2, 0));
        try {
            counter.getCount(3, 2, 1);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        counter.getCount(2, 3, 2);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        Iterator iterator = counter.iterator();
        int multidimensionalCount = 0;
        while (iterator.hasNext()) {
            int unidimensionalCount = iterator.next();
            multidimensionalCount++;
            int[] counts = iterator.getCounts();
            assertEquals(3, counts.length);
            assertEquals(counts[0], iterator.getCount(0));
            assertEquals(counts[1], iterator.getCount(1));
            assertEquals(counts[2], iterator.getCount(2));
        }
        assertEquals(6 * 6 * 6, multidimensionalCount);
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
        Iterator iterator = counter.iterator();
        int multidimensionalCount = 0;
        String expectedString = "[0]";
        while (iterator.hasNext()) {
            int unidimensionalCount = iterator.next();
            multidimensionalCount++;
            String actualString = iterator.toString();
            assertEquals(expectedString, actualString);
            expectedString = "[1]";
        }
        assertEquals(6 * 6 * 6, multidimensionalCount);
    }
}