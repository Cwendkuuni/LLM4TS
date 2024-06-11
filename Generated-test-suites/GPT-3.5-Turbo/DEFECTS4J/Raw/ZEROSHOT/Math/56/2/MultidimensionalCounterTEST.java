import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            Assert.assertEquals(count++, (int) iterator.next());
        }
        
        Assert.assertEquals(counter.getSize(), count);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        Assert.assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(2, counter.getCount(0, 0, 2));
        Assert.assertEquals(3, counter.getCount(0, 1, 0));
        Assert.assertEquals(12, counter.getCount(1, 0, 0));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfBoundsException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 0, 4);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals("[0][0][0]", counter.toString());
    }

}