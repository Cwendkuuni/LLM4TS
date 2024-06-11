import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertNotNull(counter);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testCountsConversion() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] indices = counter.getCounts(23);
        assertArrayEquals(new int[]{1, 3, 2}, indices);
    }

    @Test
    public void testCountConversion() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(1, 3, 2);
        assertEquals(23, count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testCountsConversionOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] indices = counter.getCounts(30);
    }

    @Test(expected = OutOfRangeException.class)
    public void testCountConversionOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int count = counter.getCount(2, 3, 4);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}