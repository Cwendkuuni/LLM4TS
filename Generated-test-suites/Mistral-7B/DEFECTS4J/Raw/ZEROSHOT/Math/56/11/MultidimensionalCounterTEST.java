import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = new int[]{-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSizes() {
        int[] sizes = new int[]{0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        Iterator expectedIterator = new Iterator() {
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                for (int i = 0; i < counter.getDimension(); i++) {
                    if (currentCounts[i] != sizes[i] - 1) {
                        return true;
                    }
                }
                return false;
            }

            int[] currentCounts = new int[counter.getDimension()];

            @Override
            public Integer next() {
                for (int i = counter.getDimension() - 1; i >= 0; i--) {
                    if (currentCounts[i] == sizes[i] - 1) {
                        currentCounts[i] = 0;
                    } else {
                        currentCounts[i]++;
                        break;
                    }
                }

                return currentIndex++;
            }
        };

        for (int i = 0; i < counter.getSize(); i++) {
            assertTrue(iterator.hasNext());
            assertEquals(iterator.next().intValue(), i);
        }

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        iterator.remove();
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());

        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            int actual = counter.getCount(counts);
            int[] expected = new int[]{i / (counter.getSize() / sizes[counter.getDimension() - 1]),
                    i % (counter.getSize() / sizes[counter.getDimension() - 1])};
            assertEquals(counter.getCount(expected), actual);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());

        counter.getCount(new int[]{5, 5, 5});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());

        counter.getCount(new int[]{2});
    }

    @Test
    public void testToString() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes.clone());

        for (int i = 0; i < counter.getSize(); i++) {
            String actual = counter.toString();
            String expected = "[";
            for (int j = 0; j < counter.getDimension(); j++) {
                expected += String.valueOf(counter.getCount(new int[]{j})).replaceAll("\\D", "") + "]";
                if (j != counter.getDimension() - 1) {
                    expected += ", ";
                }
            }
            assertEquals(expected, actual);
        }
    }
}

class Iterator {
    int currentIndex;
    int[] currentCounts;

    @Override
    public boolean hasNext() {
        for (int i = 0; i < counter.getDimension(); i++) {
            if (currentCounts[i] != sizes[i] - 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer next() {
        for (int i = counter.getDimension() - 1; i >= 0; i--) {
            if (currentCounts[i] == sizes[i] - 1) {
                currentCounts[i] = 0;
            } else {
                currentCounts[i]++;
                break;
            }
        }

        return currentIndex++;
    }
}