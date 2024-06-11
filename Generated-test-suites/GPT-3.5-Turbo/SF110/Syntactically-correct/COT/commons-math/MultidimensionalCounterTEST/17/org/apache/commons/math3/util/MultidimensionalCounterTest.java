import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;
import org.junit.*;

public class MultidimensionalCounterTest {
  // Test case for constructor
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructor() throws NotStrictlyPositiveException {
    MultidimensionalCounter counter = new MultidimensionalCounter();
  }

  // Test case for iterator method
  @Test
  public void testIterator() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    assertNotNull(counter.iterator());
  }

  // Test case for getDimension method
  @Test
  public void testGetDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    assertEquals(0, counter.getDimension());
  }

  // Test case for getCounts method
  @Test(expected = OutOfRangeException.class)
  public void testGetCountsOutOfRange() throws OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    counter.getCounts(-1);
  }

  @Test
  public void testGetCounts() throws OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    int[] expected = new int[0];
    assertArrayEquals(expected, counter.getCounts(0));
  }

  // Test case for getCount method
  @Test(expected = DimensionMismatchException.class)
  public void testGetCountDimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    counter.getCount(1);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountOutOfRange() throws DimensionMismatchException, OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    counter.getCount(0);
  }

  @Test
  public void testGetCount() throws DimensionMismatchException, OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    assertEquals(0, counter.getCount());
  }

  // Test case for getSize method
  @Test
  public void testGetSize() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    assertEquals(0, counter.getSize());
  }

  // Test case for getSizes method
  @Test
  public void testGetSizes() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    int[] expected = new int[0];
    assertArrayEquals(expected, counter.getSizes());
  }

  // Test case for toString method
  @Test
  public void testToString() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    assertEquals("", counter.toString());
  }

  // Test case for Iterator class
  @Test
  public void testIteratorHasNext() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    Iterator iterator = counter.iterator();
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testIteratorNext() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    Iterator iterator = counter.iterator();
    assertEquals(null, iterator.next());
  }

  @Test
  public void testIteratorGetCount() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    Iterator iterator = counter.iterator();
    assertEquals(-1, iterator.getCount());
  }

  @Test
  public void testIteratorGetCounts() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    Iterator iterator = counter.iterator();
    int[] expected = new int[0];
    assertArrayEquals(expected, iterator.getCounts());
  }

  @Test
  public void testIteratorGetCountWithDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    Iterator iterator = counter.iterator();
    assertEquals(0, iterator.getCount(0));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIteratorRemove() {
    MultidimensionalCounter counter = new MultidimensionalCounter();
    Iterator iterator = counter.iterator();
    iterator.remove();
  }
}