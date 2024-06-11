import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(0), iterator.next());
        Assert.assertEquals(0, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertEquals(1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 1}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(1, iterator.getCount(1));

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertEquals(2, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 2}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(2, iterator.getCount(1));

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(3), iterator.next());
        Assert.assertEquals(3, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 0}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(4), iterator.next());
        Assert.assertEquals(4, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 1}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(1, iterator.getCount(1));

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());
        Assert.assertEquals(5, iterator.getCount());
        Assert.assertArrayEquals(new int[]{1, 2}, iterator.getCounts());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(2, iterator.getCount(1));

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Assert.assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{0, 2}, counter.getCounts(2));
        Assert.assertArrayEquals(new int[]{1, 0}, counter.getCounts(3));
        Assert.assertArrayEquals(new int[]{1, 1}, counter.getCounts(4));
        Assert.assertArrayEquals(new int[]{1, 2}, counter.getCounts(5));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Assert.assertEquals(0, counter.getCount(0, 0));
        Assert.assertEquals(1, counter.getCount(0, 1));
        Assert.assertEquals(2, counter.getCount(0, 2));
        Assert.assertEquals(3, counter.getCount(1, 0));
        Assert.assertEquals(4, counter.getCount(1, 1));
        Assert.assertEquals(5, counter.getCount(1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(0, 3);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Assert.assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        Assert.assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }
}