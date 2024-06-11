import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_validInput_success() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            Assert.assertNotNull(counter);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testConstructor_invalidInput_ThrowsNotStrictlyPositiveException() {
        int[] size = {0, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            Assert.fail("NotStrictlyPositiveException should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testCounts_validIndex_returnsArray() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] indices = counter.getCounts(0);
            Assert.assertNotNull(indices);
            Assert.assertEquals(indices.length, 3);
            Assert.assertEquals(indices[0], 0);
            Assert.assertEquals(indices[1], 0);
            Assert.assertEquals(indices[2], 0);
        } catch (OutOfRangeException e) {
            Assert.fail("OutOfRangeException should not have been thrown");
        }
    }

    @Test
    public void testCounts_invalidIndex_ThrowsOutOfRangeException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] indices = counter.getCounts(10);
            Assert.fail("OutOfRangeException should have been thrown");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testGetCount_validInput_returnsCount() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(0, 0, 0);
            Assert.assertEquals(count, 0);
            count = counter.getCount(1, 1, 1);
            Assert.assertEquals(count, 17);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            Assert.fail("OutOfRangeException or DimensionMismatchException should not have been thrown");
        }
    }

    @Test
    public void testGetCount_invalidInput_ThrowsOutOfRangeException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(2, 3, 4);
            Assert.fail("OutOfRangeException should have been thrown");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        } catch (DimensionMismatchException e) {
            Assert.fail("DimensionMismatchException should not have been thrown");
        }
    }

    @Test
    public void testGetCount_invalidInput_ThrowsDimensionMismatchException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int count = counter.getCount(0, 0);
            Assert.fail("DimensionMismatchException should have been thrown");
        } catch ( DimensionMismatchException e) {
            // Exception thrown as expected
        } catch ( OutOfRangeException e) {
            Assert.fail("OutOfRangeException should not have been thrown");
        }
    }

    @Test
    public void testGetSize_validInput_returnsSize() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int result = counter.getSize();
            Assert.assertEquals(result, 24);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testGetSizes_validInput_returnsArray() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            int[] result = counter.getSizes();
            Assert.assertNotNull(result);
            Assert.assertEquals(result.length, 3);
            Assert.assertEquals(result[0], 2);
            Assert.assertEquals(result[1], 3);
            Assert.assertEquals(result[2], 4);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testIterator_hasNext_returnsBoolean() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            boolean result = iterator.hasNext();
            Assert.assertTrue(result);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testIterator_next_returnsInteger() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            int result = iterator.next();
            Assert.assertEquals(result, 0);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testIterator_getCount_returnsCount() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            int result = iterator.getCount();
            Assert.assertEquals(result, -1);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testIterator_getCounts_returnsArray() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            int[] result = iterator.getCounts();
            Assert.assertNotNull(result);
            Assert.assertEquals(result.length, 3);
            Assert.assertEquals(result[0], 0);
            Assert.assertEquals(result[1], 0);
            Assert.assertEquals(result[2], 0);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test
    public void testIterator_getCount_validInput_returnsCount() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            int result = iterator.getCount(0);
            Assert.assertEquals(result, 0);
            result = iterator.getCount(1);
            Assert.assertEquals(result, 0);
            result = iterator.getCount(2);
            Assert.assertEquals(result, 0);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove_throwsUnsupportedOperationException() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            iterator.remove();
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("NotStrictlyPositiveException should not have been thrown");
        }
    }
}