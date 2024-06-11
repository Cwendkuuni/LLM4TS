import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] sizes;
    private final MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... sizes) {
        this.sizes = sizes;
        counter = new MultidimensionalCounter(sizes);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
                {new int[0]},
                {new int[] {1}},
                {new int[] {1, 1}},
                {new int[] {2, 3}},
                {new int[] {3, 2, 1}}
        });
    }

    @Test
    public void testGetDimension() {
        assertEquals(sizes.length, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        int totalSize = 1;
        for (int size : sizes) {
            totalSize *= size;
        }
        assertEquals(totalSize, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(sizes, counter.getSizes());
    }

    @Test
    public void testGetCounts() {
        for (int i = 0; i < sizes.length; i++) {
            int[] counts = counter.getSizes();
            int j = sizes.length - i - 1;
            assertEquals(sizes[j], counts[i]);
        }
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder();
        for (int size : sizes) {
            expected.append("[")
                    .append(MathUtils.checkedAdd(Arrays.asList(new Integer[] {0}).iterator().next(),
                            MathUtils.mult(new Integer[]{new Integer(i), new Integer(size)})))
                    .append("]");
        }
        assertEquals(expected.toString(), counter.toString());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeNegative() {
        int index = -1;
        counter.getCount(index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeTooBig() {
        int index = counter.getSize();
        counter.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension() {
        int[] invalidDimensions = new int[]{-1, 3};
        counter.getCount(invalidDimensions);
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        assertEquals(counter.getSize(), count);
    }

    @Test
    public void testIteratorNext() {
        Iterator iterator = counter.iterator();
        int[] expected = new int[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            expected[i] = i;
        }
        int count = 0;
        while (iterator.hasNext()) {
            int[] counts = iterator.getCounts();
            assertArrayEquals(expected, counts);
            expected[0]++;
            for (int i = 1; i < sizes.length; i++) {
                if (expected[i] >= sizes[i]) {
                    expected[i] = 0;
                    expected[i - 1]++;
                }
            }
            count++;
            iterator.next();
        }
        assertEquals(counter.getSize(), count);
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int countValue = iterator.getCount();
            assertEquals(count, countValue);
            count++;
            iterator.next();
        }
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int[] counts = iterator.getCounts();
            assertArrayEquals(sizes, counts);
            count++;
            iterator.next();
        }
    }

    @Test
    public void testIteratorGetCountLast() {
        Iterator iterator = counter.iterator();
        int lastCount = iterator.getCount(sizes.length - 1);
        assertEquals(0, lastCount);
        iterator.next();
        lastCount = iterator.getCount(sizes.length - 1);
        for (int i = 0; i < sizes.length - 1; i++) {
            assertEquals(i + 1, iterator.getCount(i));
        }
        assertEquals(1, lastCount);
        iterator.next();
        lastCount = iterator.getCount(sizes.length - 1);
        assertEquals(0, lastCount);
        iterator.next();
        lastCount = iterator.getCount(sizes.length - 1);
        assertEquals(1, lastCount);
        //Test end of iteration
        assertFalse(iterator.hasNext());
    }
}