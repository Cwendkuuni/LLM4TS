import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {
  
  // Test getSize() method
  @Test
  public void testGetSize() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Assert.assertEquals(6, counter.getSize());
  }
  
  // Test getSizes() method
  @Test
  public void testGetSizes() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    int[] expectedSizes = {2, 3};
    int[] sizes = counter.getSizes();
    Assert.assertArrayEquals(expectedSizes, sizes);
  }
  
  // Test getDimension() method
  @Test
  public void testGetDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Assert.assertEquals(2, counter.getDimension());
  }
  
  // Test getCount() method
  @Test
  public void testGetCount() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    int count = counter.getCount(1, 2);
    Assert.assertEquals(5, count);
  }
  
  // Test getCounts() method
  @Test
  public void testGetCounts() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    int[] counts = counter.getCounts(5);
    int[] expectedCounts = {1, 2};
    Assert.assertArrayEquals(expectedCounts, counts);
  }
  
  // Test iterator
  @Test
  public void testIterator() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    MultidimensionalCounter.Iterator iterator = counter.iterator();
    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals(0, iterator.next().intValue());
    Assert.assertEquals(1, iterator.next().intValue());
    Assert.assertEquals(2, iterator.next().intValue());
    Assert.assertEquals(3, iterator.next().intValue());
    Assert.assertEquals(4, iterator.next().intValue());
    Assert.assertEquals(5, iterator.next().intValue());
    Assert.assertFalse(iterator.hasNext());
  }
  
  // Test toString() method
  @Test
  public void testToString() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Assert.assertEquals("[0][0]", counter.toString());
  }
  
  // Test getCount() with dimension mismatch exception
  @Test(expected = DimensionMismatchException.class)
  public void testGetCount_DimensionMismatchException() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    counter.getCount(1, 2, 3);
  }
  
  // Test getCount() with out of range exception
  @Test(expected = OutOfRangeException.class)
  public void testGetCount_OutOfRangeException() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    counter.getCount(1, 4);
  }
  
  // Test getCounts() with out of range exception
  @Test(expected = OutOfRangeException.class)
  public void testGetCounts_OutOfRangeException() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    counter.getCounts(6);
  }
  
  // Test MultidimensionalCounter constructor with not strictly positive exception
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructor_NotStrictlyPositiveException() {
    MultidimensionalCounter counter = new MultidimensionalCounter(-1, 3);
  }
  
}