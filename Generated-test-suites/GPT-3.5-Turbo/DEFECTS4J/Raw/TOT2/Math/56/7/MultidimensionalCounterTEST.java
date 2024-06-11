import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterInitialization() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, (int)iterator.next());
        assertEquals(1, (int)iterator.next());
    }

    @Test
    public void testMultidimensionalCounterConversion() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] indices = counter.getCounts(10);
        assertArrayEquals(new int[] {0, 2, 3}, indices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterConversionException() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        // Index out of range
        counter.getCounts(25);
    }

    @Test
    public void testMultidimensionalCounterInverseConversion() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = counter.getCount(0, 2, 3);
        assertEquals(10, index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterInverseConversionException() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        // Dimension mismatch
        counter.getCount(1, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterInverseConversionOutOfRangeException() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        // Value out of range
        counter.getCount(0, 5, 1);
    }

}