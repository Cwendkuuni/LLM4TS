import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCountWithValidIndices() {
        int[] indices = {1, 2, 1};
        int expectedCount = 21;

        assertEquals(expectedCount, counter.getCount(indices));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidIndices() {
        int[] indices = {1, 2};
        counter.getCount(indices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() {
        int[] indices = {1, 6, 1};
        counter.getCount(indices);
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 4, 3};

        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetCountsValidIndex() {
        int index = 15;
        int[] expectedCounts = {1, 3, 1};

        assertArrayEquals(expectedCounts, counter.getCounts(index));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        int index = 30;
        counter.getCounts(index);
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expectedCount = 0;

        while (iterator.hasNext()) {
            assertEquals(expectedCount, (int) iterator.next());
            expectedCount++;
        }
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expectedCounts = {0, 0, 0};

        while (iterator.hasNext()) {
            assertArrayEquals(expectedCounts, iterator.getCounts());
            iterator.next();
            
            if (expectedCounts[0] < 1) {
                expectedCounts[0]++;
            } else {
                expectedCounts[0] = 0;
                if (expectedCounts[1] < 3) {
                    expectedCounts[1]++;
                } else {
                    expectedCounts[1] = 0;
                    if (expectedCounts[2] < 2) {
                        expectedCounts[2]++;
                    } else {
                        expectedCounts[2] = 0;
                    }
                }
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        counter.iterator().remove();
    }
}