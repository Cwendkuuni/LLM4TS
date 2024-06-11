import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Test case 1
        try {
            int[] size = new int[]{2, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            Assert.assertEquals(3, counter.getDimension());
            Assert.assertArrayEquals(size, counter.getSizes());
        } catch (Exception e) {
            Assert.fail("Constructor failed");
        }

        // Test case 2 - NotStrictlyPositiveException
        try {
            int[] size = new int[]{0, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            Assert.fail("Constructor should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        } catch (Exception e) {
            Assert.fail("Constructor failed with unexpected exception");
        }
    }

    @Test
    public void testGetCounts() {
        try {
            int[] size = {2, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);

            // Test case 1
            int[] result = counter.getCounts(0);
            int[] expected = {0, 0, 0};
            Assert.assertArrayEquals(expected, result);

            // Test case 2
            result = counter.getCounts(6);
            expected = new int[]{0, 1, 2};
            Assert.assertArrayEquals(expected, result);

            // Test case 3 - OutOfRangeException
            try {
                result = counter.getCounts(24);
                Assert.fail("getCounts should throw OutOfRangeException");
            } catch (OutOfRangeException e) {
                // Exception expected
            } catch (Exception e) {
                Assert.fail("getCounts failed with unexpected exception");
            }

        } catch (Exception e) {
            Assert.fail("testGetCounts failed");
        }
    }

    @Test
    public void testGetCount() {
        try {
            int[] size = {2, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);

            // Test case 1
            int[] c = {0, 0, 0};
            int result = counter.getCount(c);
            Assert.assertEquals(0, result);

            // Test case 2
            c = new int[]{0, 1, 2};
            result = counter.getCount(c);
            Assert.assertEquals(6, result);

            // Test case 3 - DimensionMismatchException
            try {
                c = new int[]{0, 1};
                result = counter.getCount(c);
                Assert.fail("getCount should throw DimensionMismatchException");
            } catch (DimensionMismatchException e) {
                // Exception expected
            } catch (Exception e) {
                Assert.fail("getCount failed with unexpected exception");
            }

            // Test case 4 - OutOfRangeException
            try {
                c = new int[]{0, 4, 2};
                result = counter.getCount(c);
                Assert.fail("getCount should throw OutOfRangeException");
            } catch (OutOfRangeException e) {
                // Exception expected
            } catch (Exception e) {
                Assert.fail("getCount failed with unexpected exception");
            }

        } catch (Exception e) {
            Assert.fail("testGetCount failed");
        }
    }

    @Test
    public void testGetSize() {
        try {
            int[] size = {2, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            
            // Test case 1
            int result = counter.getSize();
            Assert.assertEquals(24, result);
        } catch (Exception e) {
            Assert.fail("testGetSize failed");
        }
    }

    @Test
    public void testIterator() {
        try {
            int[] size = {2, 3, 4};
            MultidimensionalCounter counter = new MultidimensionalCounter(size);

            // Test case 1
            Iterator<Integer> iterator = counter.iterator();
            int count = 0;
            int[] expectedCounts = {0, 0, 0};
            while (iterator.hasNext()) {
                int value = iterator.next();
                int[] counts = iterator.getCounts();
                Assert.assertEquals(count, value);
                Assert.assertArrayEquals(expectedCounts, counts);
                expectedCounts[2]++;
                if (expectedCounts[2] == 4) {
                    expectedCounts[2] = 0;
                    expectedCounts[1]++;
                    if (expectedCounts[1] == 3) {
                        expectedCounts[1] = 0;
                        expectedCounts[0]++;
                    }
                }
                count++;
            }
            Assert.assertEquals(24, count);

        } catch (Exception e) {
            Assert.fail("testIterator failed");
        }
    }
}