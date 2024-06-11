import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIteratorHasNext() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Act & Assert
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Act & Assert
        assertEquals(iterator.next().intValue(), 0);
        assertEquals(iterator.next().intValue(), 1);
        assertEquals(iterator.next().intValue(), 2);
        assertEquals(iterator.next().intValue(), 3);
        assertEquals(iterator.next().intValue(), 4);
        assertEquals(iterator.next().intValue(), 5);
        assertEquals(iterator.next().intValue(), 6);
        assertEquals(iterator.next().intValue(), 7);
        assertEquals(iterator.next().intValue(), 8);
        assertEquals(iterator.next().intValue(), 9);
        assertEquals(iterator.next().intValue(), 10);
        assertEquals(iterator.next().intValue(), 11);
        assertEquals(iterator.next().intValue(), 12);
        assertEquals(iterator.next().intValue(), 13);
        assertEquals(iterator.next().intValue(), 14);
        assertEquals(iterator.next().intValue(), 15);
        assertEquals(iterator.next().intValue(), 16);
        assertEquals(iterator.next().intValue(), 17);
        assertEquals(iterator.next().intValue(), 18);
        assertEquals(iterator.next().intValue(), 19);
        assertEquals(iterator.next().intValue(), 20);
        assertEquals(iterator.next().intValue(), 21);
        assertEquals(iterator.next().intValue(), 22);
        assertEquals(iterator.next().intValue(), 23);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        assertArrayEquals(counter.getCounts(0), new int[]{0, 0, 0});
        assertArrayEquals(counter.getCounts(1), new int[]{0, 0, 1});

        assertArrayEquals(counter.getCounts(2), new int[]{0, 0, 2});
        assertArrayEquals(counter.getCounts(3), new int[]{0, 1, 0});

        assertArrayEquals(counter.getCounts(12), new int[]{1, 0, 0});
        assertArrayEquals(counter.getCounts(23), new int[]{1, 3, 2});
    }

    @Test
    public void testGetCount() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        assertEquals(counter.getCount(0, 0, 0), 0);
        assertEquals(counter.getCount(0, 0, 1), 1);

        assertEquals(counter.getCount(0, 0, 2), 2);
        assertEquals(counter.getCount(0, 1, 0), 3);

        assertEquals(counter.getCount(1, 0, 0), 12);
        assertEquals(counter.getCount(1, 3, 2), 23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountsInvalidIndex() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        counter.getCounts(50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountInvalidIndices() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        counter.getCount(5, 5, 5);
    }

    @Test
    public void testGetSize() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        assertEquals(counter.getSize(), 24);
    }

    @Test
    public void testGetSizes() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        assertArrayEquals(counter.getSizes(), new int[]{2, 3, 4});
    }

    @Test
    public void testToString() {
        // Arrange
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        // Act & Assert
        assertEquals(counter.toString(), "[0][0][0]");
    }
}