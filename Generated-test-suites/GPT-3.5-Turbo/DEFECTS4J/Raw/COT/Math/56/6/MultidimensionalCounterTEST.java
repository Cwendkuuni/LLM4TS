import static org.junit.Assert.*;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;

public class MultidimensionalCounterTest {
  
  @Test
  public void testConstructor() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    assertEquals(3, counter.getDimension());
    assertArrayEquals(size, counter.getSizes());
    assertEquals(24, counter.getSize());
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructor_InvalidSize() {
    int[] size = {0, 3, -4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
  }
  
  @Test
  public void testIterator() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    MultidimensionalCounter.Iterator iterator = counter.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      int next = iterator.next();
      assertEquals(count, next);
      count++;
    }
    assertEquals(23, count);
  }
  
  @Test
  public void testGetCounts() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    int[] counts = counter.getCounts(14);
    int[] expectedCounts = {1, 2, 2};
    assertArrayEquals(expectedCounts, counts);
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testGetCounts_InvalidIndex() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    int[] counts = counter.getCounts(27);
  }
  
  @Test
  public void testGetCount() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    int count = counter.getCount(1, 2, 2);
    assertEquals(14, count);
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testGetCount_InvalidDimension() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    int count = counter.getCount(1, 2);
  }
  
  @Test(expected = OutOfRangeException.class)
  public void testGetCount_InvalidIndex() {
    int[] size = {2, 3, 4};
    MultidimensionalCounter counter = new MultidimensionalCounter(size);
    
    int count = counter.getCount(1, 2, 4);
  }

}