import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
  
  // Test case for constructor
  @Test
  public void testConstructor() {
    // Typical use case
    try {
      MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
    } catch (Exception e) {
      fail("Exception thrown for valid input");
    }
    
    // Edge case: Single dimension
    try {
      MultidimensionalCounter counter = new MultidimensionalCounter(0);
      fail("No exception thrown for non-positive size");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Wrong exception thrown for non-positive size");
    }
    
    // Error scenario: Negative size
    try {
      MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
      fail("No exception thrown for negative size");
    } catch (NotStrictlyPositiveException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Wrong exception thrown for negative size");
    }
  }
  
  // Test case for iterator()
  @Test
  public void testIterator() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    
    // Typical use case
    Iterator<Integer> it = counter.iterator();
    assertTrue(it.hasNext());
    assertEquals(0, (int)it.next());
    assertTrue(it.hasNext());
    assertEquals(1, (int)it.next());
    assertTrue(it.hasNext());
    assertEquals(2, (int)it.next());
    assertTrue(it.hasNext());
    assertEquals(3, (int)it.next());
    assertFalse(it.hasNext());
  }
  
  // Test case for getDimension()
  @Test
  public void testGetDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
    
    // Typical use case
    assertEquals(3, counter.getDimension());
  }
  
  // Test case for getCounts()
  @Test
  public void testGetCounts() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    
    // Typical use case
    try {
      int[] counts = counter.getCounts(0);
      assertArrayEquals(new int[]{0, 0}, counts);
      
      counts = counter.getCounts(1);
      assertArrayEquals(new int[]{0, 1}, counts);
      
      counts = counter.getCounts(2);
      assertArrayEquals(new int[]{0, 2}, counts);
      
      counts = counter.getCounts(3);
      assertArrayEquals(new int[]{1, 0}, counts);
    } catch (Exception e) {
      fail("Exception thrown for valid input");
    }
    
    // Error scenario: Out of range index
    try {
      int[] counts = counter.getCounts(4);
      fail("No exception thrown for out of range index");
    } catch (OutOfRangeException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Wrong exception thrown for out of range index");
    }
  }
  
  // Test case for getCount()
  @Test
  public void testGetCount() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    
    // Typical use case
    try {
      int count = counter.getCount(0, 0);
      assertEquals(0, count);
      
      count = counter.getCount(0, 1);
      assertEquals(1, count);
      
      count = counter.getCount(1, 0);
      assertEquals(2, count);
    } catch (Exception e) {
      fail("Exception thrown for valid input");
    }
    
    // Error scenario: Dimension mismatch
    try {
      int count = counter.getCount(0);
      fail("No exception thrown for dimension mismatch");
    } catch (DimensionMismatchException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Wrong exception thrown for dimension mismatch");
    }
    
    // Error scenario: Out of range index
    try {
      int count = counter.getCount(2, 0);
      fail("No exception thrown for out of range index");
    } catch (OutOfRangeException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Wrong exception thrown for out of range index");
    }
  }
  
  // Test case for getSize()
  @Test
  public void testGetSize() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    
    // Typical use case
    assertEquals(6, counter.getSize());
  }
  
  // Test case for getSizes()
  @Test
  public void testGetSizes() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
    
    // Typical use case
    assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
  }
  
  // Test case for toString()
  @Test
  public void testToString() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    
    // Typical use case
    assertEquals("[0][0]", counter.toString());
  }
  
  // Test case for Iterator.hasNext() and Iterator.next()
  @Test
  public void testIteratorHasNextAndNext() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    
    // Typical use case
    Iterator<Integer> it = counter.iterator();
    assertTrue(it.hasNext());
    assertEquals(0, (int)it.next());
    assertTrue(it.hasNext());
    assertEquals(1, (int)it.next());
    assertTrue(it.hasNext());
    assertEquals(2, (int)it.next());
    assertTrue(it.hasNext());
    assertEquals(3, (int)it.next());
    assertFalse(it.hasNext());
    
    // Edge case: Single dimension
    counter = new MultidimensionalCounter(1);
    it = counter.iterator();
    assertTrue(it.hasNext());
    assertEquals(0, (int)it.next());
    assertFalse(it.hasNext());
  }
  
  // Test case for Iterator.getCount()
  @Test
  public void testIteratorGetCount() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Iterator<Integer> it = counter.iterator();
    
    // Typical use case
    assertEquals(0, it.getCount());
    it.next();
    assertEquals(1, it.getCount());
    it.next();
    assertEquals(2, it.getCount());
    
    // Edge case: Single dimension
    counter = new MultidimensionalCounter(1);
    it = counter.iterator();
    assertEquals(0, it.getCount());
    it.next();
    assertEquals(1, it.getCount());
  }
  
  // Test case for Iterator.getCounts()
  @Test
  public void testIteratorGetCounts() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Iterator<Integer> it = counter.iterator();
    
    // Typical use case
    int[] counts = it.getCounts();
    assertArrayEquals(new int[]{0, 0}, counts);
    it.next();
    counts = it.getCounts();
    assertArrayEquals(new int[]{0, 1}, counts);
    it.next();
    counts = it.getCounts();
    assertArrayEquals(new int[]{1, 0}, counts);
    
    // Edge case: Single dimension
    counter = new MultidimensionalCounter(1);
    it = counter.iterator();
    counts = it.getCounts();
    assertArrayEquals(new int[]{0}, counts);
    it.next();
    counts = it.getCounts();
    assertArrayEquals(new int[]{1}, counts);
  }
  
  // Test case for Iterator.getCount(int)
  @Test
  public void testIteratorGetCountByDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Iterator<Integer> it = counter.iterator();
    
    // Typical use case
    assertEquals(0, it.getCount(0));
    assertEquals(0, it.getCount(1));
    it.next();
    assertEquals(0, it.getCount(0));
    assertEquals(1, it.getCount(1));
    it.next();
    assertEquals(1, it.getCount(0));
    assertEquals(0, it.getCount(1));
    
    // Edge case: Single dimension
    counter = new MultidimensionalCounter(1);
    it = counter.iterator();
    assertEquals(0, it.getCount(0));
    it.next();
    assertEquals(1, it.getCount(0));
  }
}