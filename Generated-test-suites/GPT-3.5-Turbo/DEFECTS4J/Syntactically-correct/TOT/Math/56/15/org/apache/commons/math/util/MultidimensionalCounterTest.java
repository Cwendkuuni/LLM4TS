import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 4, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 0, 3);
    }

    @Test
    public void testConstructor_ValidSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertNotNull(counter);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(3, 1, 2);
    }

    @Test
    public void testGetCount_ValidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int result = counter.getCount(1, 2, 1);
        assertEquals(21, result);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(30);
    }

    @Test
    public void testGetCounts_ValidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] result = counter.getCounts(12);
        assertArrayEquals(new int[]{1, 0, 0}, result);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int result = counter.getSize();
        assertEquals(24, result);
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] result = counter.getSizes();
        assertArrayEquals(new int[]{2, 4, 3}, result);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(24, count);
    }

}