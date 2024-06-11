import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(8, counter.getSize());
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());

        for (int i = 0; i < 24; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i + 1, iterator.next().intValue());
        }

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] counts = counter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        for (int i = 1; i < counter.getSize(); i++) {
            counts = counter.getCounts(i);
            for (int j = 0; j < 3; j++) {
                int expected = j == 2 ? i / (size[j] * size[j - 1]) : i / MathUtils.multiply(size[j], MathUtils.multiply(size[j - 1], size[j - 2]));
                assertEquals(expected, counts[j]);
            }
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfSize() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(counter.getSize());
    }

    @Test
    public void testGetCountsInvalidDimension() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        try {
            counter.getCounts(4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        for (int i = 0; i < counter.getSize(); i++) {
            String expected = "[";
            for (int j = 0; j < size.length; j++) {
                expected += i / MathUtils.multiply(size[j], MathUtils.multiply(size[j - 1], size[j - 2])) + (j < size.length - 1 ? "," : "]");
            }
            assertEquals(expected, counter.toString());
            counter.next();
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] size = {-2, 4, 3};
        new MultidimensionalCounter(size);
    }
}