import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertNotNull(counter);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException thrown incorrectly");
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testMultidimensionalCounterGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testMultidimensionalCounterGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 4}, sizes);
    }

    @Test
    public void testMultidimensionalCounterGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(11);
        assertArrayEquals(new int[]{1, 2, 3}, counts);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(11, counter.getCount(1, 2, 3));
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2, 5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountWrongDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 2);
    }
}