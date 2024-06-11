import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(MathUtils.multiply(size[0], size[1], size[2]), counter.getSize());
        Assert.assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int[] counts = counter.getCounts(i);
            int totalIndex = i;
            for (int j = size.length - 1; j >= 0; j--) {
                totalIndex -= unidimensionalCounterOffset(j, counts);
            }
            Assert.assertArrayEquals(new int[]{i / size[size.length - 1], i / (size[size.length - 1] * size[size.length - 2]), i / (size[size.length - 1] * size[size.length - 2] * size[size.length - 3])}, counts);
        }
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int index = counter.getCount(MathUtils.copyOf(new int[]{i / size[size.length - 1], i / (size[size.length - 1] * size[size.length - 2]), i / (size[size.length - 1] * size[size.length - 2] * size[size.length - 3])}));
            Assert.assertEquals(i, index);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(MathUtils.copyOf(new int[]{3, 3, 3}));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(MathUtils.copyOf(new int[]{2, 3, 3}));
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            Assert.assertTrue(iterator.hasNext());
        }

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int next = iterator.next();
            int[] counts = counter.getCounts(next);
            int totalIndex = next;
            for (int j = size.length - 1; j >= 0; j--) {
                totalIndex -= unidimensionalCounterOffset(j, counts);
            }
            Assert.assertEquals(i, totalIndex);
        }

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int next = iterator.next();
            int count = iterator.getCount();
            Assert.assertEquals(i + 1, count);
        }

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int next = iterator.next();
            int[] counts = iterator.getCounts();
            Assert.assertEquals(new int[]{i / size[size.length - 1], i / (size[size.length - 1] * size[size.length - 2]), i / (size[size.length - 1] * size[size.length - 2] * size[size.length - 3])}, counts);
        }

        Assert.assertFalse(iterator.hasNext());
    }

    private int unidimensionalCounterOffset(int dimensionIndex, int[] multidimensionalCounts) {
        int offset = 1;
        for (int j = dimensionIndex + 1; j < multidimensionalCounts.length; j++) {
            offset *= multidimensionalCounts[j];
        }
        return offset;
    }
}