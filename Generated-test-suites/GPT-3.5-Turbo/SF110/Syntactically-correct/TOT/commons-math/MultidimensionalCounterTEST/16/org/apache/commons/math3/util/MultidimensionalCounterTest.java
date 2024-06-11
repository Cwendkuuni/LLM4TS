import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_ThrowsNotStrictlyPositiveException() {
        int[] size = {0};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testConstructor_ValidSize() {
        int[] size = {3, 4, 2};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(3, counter.getDimension());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected NotStrictlyPositiveException");
        }
    }

    @Test
    public void testGetCounts_OutOfRangeException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] counts = counter.getCounts(30);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testGetCounts_ValidIndex() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] counts = counter.getCounts(9);
            int[] expected = {1, 0, 1};
            assertArrayEquals(expected, counts);
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        }
    }

    @Test
    public void testGetCount_OutOfRangeException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(2, 3, 5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception caught, test passed
        } catch (DimensionMismatchException e) {
            fail("Unexpected DimensionMismatchException");
        }
    }

    @Test
    public void testGetCount_DimensionMismatchException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(2, 3);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Exception caught, test passed
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        }
    }

    @Test
    public void testGetCount_ValidInput() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(1, 2, 3);
            assertEquals(23, count);
        } catch (DimensionMismatchException e) {
            fail("Unexpected DimensionMismatchException");
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        }
    }

    @Test
    public void testGetSize() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(24, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected NotStrictlyPositiveException");
        }
    }

    @Test
    public void testGetSizes() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] sizes = counter.getSizes();
            assertArrayEquals(size, sizes);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected NotStrictlyPositiveException");
        }
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            assertTrue(iterator.hasNext());

            int count = iterator.getCount();
            assertEquals(0, count);

            int[] counts = iterator.getCounts();
            int[] expected = {0, 0, 0};
            assertArrayEquals(expected, counts);

            int countIndex = iterator.getCount(1);
            assertEquals(0, countIndex);

            iterator.next();
            assertTrue(iterator.hasNext());

            count = iterator.getCount();
            assertEquals(1, count);

            counts = iterator.getCounts();
            expected[0] = 0;
            expected[1] = 0;
            expected[2] = 1;
            assertArrayEquals(expected, counts);

            countIndex = iterator.getCount(2);
            assertEquals(1, countIndex);

            // Keep iterating to the end
            while (iterator.hasNext()) {
                iterator.next();
            }

            assertFalse(iterator.hasNext());

            count = iterator.getCount();
            assertEquals(23, count);

            counts = iterator.getCounts();
            expected[0] = 1;
            expected[1] = 2;
            expected[2] = 3;
            assertArrayEquals(expected, counts);

            countIndex = iterator.getCount(0);
            assertEquals(1, countIndex);

        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected NotStrictlyPositiveException");
        }
    }
}