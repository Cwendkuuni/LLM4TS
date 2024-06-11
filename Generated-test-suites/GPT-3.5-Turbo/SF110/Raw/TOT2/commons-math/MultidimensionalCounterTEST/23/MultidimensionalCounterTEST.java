import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
            assertNotNull(counter);
        } catch (Exception e) {
            fail("Exception thrown during construction");
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        Iterator it = counter.iterator();
        assertNotNull(it);
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 3, 3);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 2, 2}, sizes);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int[] counts = counter.getCounts(3);
        assertArrayEquals(new int[]{1, 0, 1}, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 2);
        int count = counter.getCount(1, 1, 0);
        assertEquals(6, count);
    }

    // Add more test cases for other methods as required

}